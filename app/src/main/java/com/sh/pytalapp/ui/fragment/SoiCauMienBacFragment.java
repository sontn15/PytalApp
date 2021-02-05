package com.sh.pytalapp.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.pytalapp.R;
import com.sh.pytalapp.adapter.NumberAdapter;
import com.sh.pytalapp.adapter.SoiCauAdapter;
import com.sh.pytalapp.database.ResourceData;
import com.sh.pytalapp.model.SoiCau;
import com.sh.pytalapp.model.dto.NumberDTO;
import com.sh.pytalapp.utils.Const;
import com.sh.pytalapp.utils.NetworkUtils;
import com.sh.pytalapp.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SoiCauMienBacFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View mView;

    private FloatingActionButton fabSoiCauMb;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvDateNewest, tvSongThuLoNewest, tvBachThuLoNewest;
    private RecyclerView rcvSoiCauMb;
    private List<SoiCau> listSoiCaus, listSoiCausClone;
    private SoiCauAdapter soiCauAdapter;
    private ProgressDialog progressDialog;

    private View viewDialog;
    private AlertDialog soiCauMbDlg;
    private List<NumberDTO> listNumbers;
    private List<NumberDTO> listNumberChooses;
    private NumberAdapter numberAdapter;
    private RecyclerView rcvNumber;
    private Button btnCancelDlg, btnAgreeDlg;
    private TextView tvDateCurrentDlg, tvTotalNumberChooseDlg;
    private EditText edtBachThuLoDlg, edtSongThuLo1Dlg, edtSongThuLo2Dlg, edtTenDaiDlg;

    private View viewListDialog;
    private TextView tvListNumberViewDlg, tvCreatedDateNumberViewDlg;
    private AlertDialog listNumberViewDlg;

    private int quantitySoiCau = 0;
    private boolean isEdit = false;
    private SoiCau soiCauEdit = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_manager_soicau_mb, container, false);
        initView();
        initAdapter();
        initSoiCauMbDialog();
        initViewListNumberDialog();
        return mView;
    }

    private void initView() {
        fabSoiCauMb = mView.findViewById(R.id.fabSoiCauMb);
        rcvSoiCauMb = mView.findViewById(R.id.rcvSoiCauMb);
        tvDateNewest = mView.findViewById(R.id.tvCreatedNewestSoiCauMb);
        tvBachThuLoNewest = mView.findViewById(R.id.tvBachThuLoNewestSoiCauMb);
        tvSongThuLoNewest = mView.findViewById(R.id.tvSongThuLoNewestSoiCauMb);
        refreshLayout = mView.findViewById(R.id.refreshSoiCauMb);
        refreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvSoiCauMb.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        fabSoiCauMb.setOnClickListener(this);
    }

    private void initAdapter() {
        listSoiCaus = new ArrayList<>();
        listSoiCausClone = new ArrayList<>();
        soiCauAdapter = new SoiCauAdapter(requireActivity(), listSoiCaus, new SoiCauAdapter.OnSoiCauItemClickListener() {
            @Override
            public void onClickItem(SoiCau soiCau) {
                clearDataListNumberDialog();
                tvCreatedDateNumberViewDlg.setText(soiCau.getCreatedDate());
                tvListNumberViewDlg.setText(StringFormatUtils.getTextFromSoiCauMbStringToView(soiCau.getListDanDe6X()));
                showListNumberDialog();
            }

            @Override
            public void onClickButtonEditItem(SoiCau soiCau) {
                isEdit = true;
                soiCauEdit = soiCau;
                edtTenDaiDlg.setText(soiCau.getNhaDai());
                tvDateCurrentDlg.setText(soiCau.getCreatedDate());
                edtBachThuLoDlg.setText(soiCau.getBachThuLo());
                edtSongThuLo1Dlg.setText(soiCau.getSongThuLo1());
                edtSongThuLo2Dlg.setText(soiCau.getSongThuLo2());
                edtBachThuLoDlg.setSelection(soiCau.getBachThuLo().length());
                edtSongThuLo1Dlg.setSelection(soiCau.getSongThuLo1().length());
                edtSongThuLo2Dlg.setSelection(soiCau.getSongThuLo2().length());
                StringFormatUtils.convertStringToListNumber(listNumbers, listNumberChooses, soiCau.getListDanDe6X());
                tvTotalNumberChooseDlg.setText("Bạn đã chọn: " + calculatorTotalChooseNumber());
                btnAgreeDlg.setText(getResources().getString(R.string.capnhat));
                numberAdapter.notifyDataSetChanged();
                showSoiCauMbDialog();
            }
        });
        rcvSoiCauMb.setAdapter(soiCauAdapter);
        buildAllSoiCau();
        getQuantitySoiCauByMien();
    }


    private void buildAllSoiCau() {
        if (NetworkUtils.haveNetwork(requireActivity())) {
            showProgressDialog();
            listSoiCaus.clear();
            listSoiCausClone.clear();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Const.FIREBASE_REF.SOI_CAU_MB);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<SoiCau> response = new ArrayList<>();
                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                        SoiCau soiCau = dataSnap.getValue(SoiCau.class);
                        if (soiCau != null) {
                            response.add(soiCau);
                        }
                    }
                    if (response.size() > 0) {
                        listSoiCaus.clear();
                        listSoiCausClone.clear();
                        listSoiCaus.addAll(response);
                        listSoiCausClone.addAll(response);
                        Collections.sort(listSoiCaus, (o1, o2) -> o2.getId() - o1.getId());
                        SoiCau soiCau = listSoiCaus.get(0);
                        tvDateNewest.setText(soiCau.getCreatedDate());
                        tvBachThuLoNewest.setText(soiCau.getBachThuLo());
                        tvSongThuLoNewest.setText(soiCau.getSongThuLo1() + " " + soiCau.getSongThuLo2());
                        soiCauAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireActivity(), getResources().getString(R.string.khongcodulieu), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    hiddenProgressDialog();
                }
            });
            soiCauAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(requireActivity(), getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void initSoiCauMbDialog() {
        soiCauMbDlg = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog).create();
        viewDialog = getLayoutInflater().inflate(R.layout.dialog_new_soicau_mb, null);

        tvDateCurrentDlg = viewDialog.findViewById(R.id.tvDateTimeSoiCauMb);
        tvTotalNumberChooseDlg = viewDialog.findViewById(R.id.tvTotalNumberChooseSoiCauDlg);
        edtBachThuLoDlg = viewDialog.findViewById(R.id.edtBachThuLoSoiCauDlg);
        edtSongThuLo1Dlg = viewDialog.findViewById(R.id.edtSongThuLo1SoiCauDlg);
        edtSongThuLo2Dlg = viewDialog.findViewById(R.id.edtSongThuLo2SoiCauDlg);
        btnCancelDlg = viewDialog.findViewById(R.id.btnCancelSoiCauDlg);
        btnAgreeDlg = viewDialog.findViewById(R.id.btnAgreeSoiCauDlg);
        edtTenDaiDlg = viewDialog.findViewById(R.id.edtNhaDaiSoiCau);

        rcvNumber = viewDialog.findViewById(R.id.rcvNumberSoiCauDlg);
        listNumberChooses = new ArrayList<>();
        listNumbers = new ArrayList<>(ResourceData.buildAllNumberLoDe());
        numberAdapter = new NumberAdapter(viewDialog.getContext(), listNumbers, numberDTO -> {
            if (numberDTO.isSelected()) {
                numberDTO.setSelected(false);
                listNumberChooses.remove(numberDTO);
            } else {
                numberDTO.setSelected(true);
                listNumberChooses.add(numberDTO);
            }
            tvTotalNumberChooseDlg.setText("Bạn đã chọn: " + calculatorTotalChooseNumber());
            numberAdapter.notifyDataSetChanged();
        });
        rcvNumber.setAdapter(numberAdapter);

        GridLayoutManager manager = new GridLayoutManager(requireActivity(), 10, GridLayoutManager.VERTICAL, false);
        rcvNumber.setLayoutManager(manager);
        soiCauMbDlg.setView(viewDialog);

        btnCancelDlg.setOnClickListener(this);
        btnAgreeDlg.setOnClickListener(this);
        btnCancelDlg.setTransformationMethod(null);
        btnAgreeDlg.setTransformationMethod(null);

        edtTenDaiDlg.setText("");
    }

    private void initViewListNumberDialog() {
        listNumberViewDlg = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog).create();
        viewListDialog = getLayoutInflater().inflate(R.layout.dialog_list_number, null);
        tvListNumberViewDlg = viewListDialog.findViewById(R.id.tvDanDe6xValueViewList);
        tvCreatedDateNumberViewDlg = viewListDialog.findViewById(R.id.tvCreatedViewListDlg);
        listNumberViewDlg.setView(viewListDialog);
    }

    private int calculatorTotalChooseNumber() {
        int i = 0;
        for (NumberDTO obj : listNumbers) {
            if (obj.isSelected()) {
                i++;
            }
        }
        return i;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabSoiCauMb: {
                onClickFabSoiCauMb();
                break;
            }
            case R.id.btnCancelSoiCauDlg: {
                onClickCancelDialog();
                break;
            }
            case R.id.btnAgreeSoiCauDlg: {
                onClickAgreeDialog();
                break;
            }
        }
    }

    private void onClickFabSoiCauMb() {
        clearDataDialog();
        showSoiCauMbDialog();
    }

    private void onClickCancelDialog() {
        clearDataDialog();
        hiddenSoiCauMbDialog();
    }

    private void onClickAgreeDialog() {
        String bachThuLo = edtBachThuLoDlg.getText().toString();
        String songThuLo1 = edtSongThuLo1Dlg.getText().toString();
        String songThuLo2 = edtSongThuLo2Dlg.getText().toString();
        String dateTime = tvDateCurrentDlg.getText().toString().trim();
        String nhaDai = edtTenDaiDlg.getText().toString();

        int totalNumbers = listNumberChooses.size();
        if (StringFormatUtils.isNullOrEmpty(bachThuLo)
                || StringFormatUtils.isNullOrEmpty(songThuLo1)
                || StringFormatUtils.isNullOrEmpty(songThuLo2)) {
            Toast.makeText(requireActivity(), getResources().getString(R.string.vui_long_nhap_day_du_thong_tin), Toast.LENGTH_SHORT).show();
            return;
        }
        if (totalNumbers < 60 || totalNumbers > 64) {
            Toast.makeText(requireActivity(), getResources().getString(R.string.dande6x_60_64), Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetworkUtils.haveNetwork(requireActivity())) {
            showProgressDialog();
            getQuantitySoiCauByMien();
            final SoiCau soiCauCreated = SoiCau.builder()
                    .id(quantitySoiCau + 1)
                    .nhaDai(nhaDai)
                    .bachThuLo(bachThuLo)
                    .songThuLo1(songThuLo1)
                    .songThuLo2(songThuLo2)
                    .createdDate(dateTime)
                    .mien(Const.SOI_CAU_BA_MIEN.MIEN_BAC)
                    .keyDate(StringFormatUtils.getCurrentDateNotTimeKeyStr())
                    .listDanDe6X(StringFormatUtils.convertListNumberToString(listNumberChooses))
                    .build();
            if (isEdit) {
                soiCauCreated.setId(soiCauEdit.getId());
                soiCauCreated.setKeyDate(soiCauEdit.getKeyDate());
            }
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_REF.SOI_CAU_MB);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String message = getResources().getString(R.string.themmoithanhcong);
                    if (snapshot.child(soiCauCreated.getKeyDate()).exists()) {
                        if (!isEdit) {
                            hiddenProgressDialog();
                            Toast.makeText(requireActivity(), "Đã tồn tại dữ liệu cho ngày " + StringFormatUtils.getCurrentDateNotTimeStr(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SoiCau soiCauDB = null;
                        for (DataSnapshot dataSnap : snapshot.getChildren()) {
                            soiCauDB = dataSnap.getValue(SoiCau.class);
                        }
                        if (soiCauDB != null) {
                            message = getResources().getString(R.string.capnhatthanhcong);
                        }
                    }
                    mDatabase.child(soiCauCreated.getKeyDate()).setValue(soiCauCreated);
                    buildAllSoiCau();
                    hiddenProgressDialog();
                    hiddenSoiCauMbDialog();
                    isEdit = false;
                    soiCauEdit = null;
                    Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    hiddenProgressDialog();
                    Toast.makeText(requireActivity(), getResources().getString(R.string.capnhatthatbai), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireActivity(), getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }

    }

    private void getQuantitySoiCauByMien() {
        if (NetworkUtils.haveNetwork(requireActivity())) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Const.FIREBASE_REF.SOI_CAU_MB);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<SoiCau> response = new ArrayList<>();
                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                        SoiCau soiCau = dataSnap.getValue(SoiCau.class);
                        if (soiCau != null) {
                            response.add(soiCau);
                        }
                    }
                    quantitySoiCau = response.size();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Toast.makeText(requireActivity(), getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        buildAllSoiCau();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (soiCauMbDlg != null) {
            soiCauMbDlg.dismiss();
            soiCauMbDlg = null;
        }
        if (listNumberViewDlg != null) {
            listNumberViewDlg.dismiss();
            listNumberViewDlg = null;
        }
    }

    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hiddenProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void clearDataDialog() {
        isEdit = false;
        soiCauEdit = null;
        edtTenDaiDlg.setText("");
        edtBachThuLoDlg.setText("");
        edtSongThuLo1Dlg.setText("");
        edtSongThuLo2Dlg.setText("");
        edtBachThuLoDlg.requestFocus();
        btnAgreeDlg.setText(getResources().getString(R.string.them_moi));
        tvTotalNumberChooseDlg.setText("Bạn đã chọn: 0");
        tvDateCurrentDlg.setText(StringFormatUtils.getCurrentDateNotTimeStr());

        listNumbers.clear();
        listNumberChooses.clear();
        listNumbers.addAll(ResourceData.buildAllNumberLoDe());
        numberAdapter.notifyDataSetChanged();
    }

    private void showSoiCauMbDialog() {
        if (soiCauMbDlg != null && !soiCauMbDlg.isShowing()) {
            soiCauMbDlg.show();
        }
    }

    private void hiddenSoiCauMbDialog() {
        if (soiCauMbDlg != null && soiCauMbDlg.isShowing()) {
            soiCauMbDlg.dismiss();
        }
    }

    private void showListNumberDialog() {
        if (listNumberViewDlg != null && !listNumberViewDlg.isShowing()) {
            listNumberViewDlg.show();
        }
    }

    private void clearDataListNumberDialog() {
        tvListNumberViewDlg.setText("");
        tvCreatedDateNumberViewDlg.setText("");
    }
}
