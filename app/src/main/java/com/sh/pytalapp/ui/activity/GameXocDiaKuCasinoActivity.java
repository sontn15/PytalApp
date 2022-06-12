package com.sh.pytalapp.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.pytalapp.R;
import com.sh.pytalapp.database.MySharedPreferences;
import com.sh.pytalapp.database.ResourceData;
import com.sh.pytalapp.model.ChanLe;
import com.sh.pytalapp.model.SettingModel;
import com.sh.pytalapp.model.ToBe;
import com.sh.pytalapp.utils.Const;
import com.sh.pytalapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameXocDiaKuCasinoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAnalyzer;
    private EditText edtTableInput;
    private ProgressDialog progressDialog;
    private TextView tvHuongDanAnalyzer;
    private TextView tvTableName, tvResultForToBe, tvResultForChanLe;
    private TextView tvBeResult, tvChanResult, tvLeResult, tvToResult;

    private Long soLanBamHienTai;
    private ToBe toBeSelected;
    private List<ChanLe> listChanLe;
    private List<ToBe> listToBe;
    private MySharedPreferences preferences;
    private SettingModel settingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_xocdia_kucasino);
        intView();
        initData();
        clearData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        preferences.clearDataByKey(Const.KEY_LIST_TO_BE);
        preferences.clearDataByKey(Const.KEY_TO_BE_SELECTED);
        preferences.clearDataByKey(Const.KEY_SO_LAN_BAM_HIEN_TAI);
        preferences.clearDataByKey(Const.KEY_INDEX_CHAN_LE_RANDOM);
    }

    private void intView() {
        btnAnalyzer = this.findViewById(R.id.btnAnalyzerXocDia);
        edtTableInput = this.findViewById(R.id.edtTableCodeXocDia);
        tvHuongDanAnalyzer = this.findViewById(R.id.tvHuongDanAnalyzerXocDia);

        tvTableName = this.findViewById(R.id.tvNameTableXocDia);
        tvResultForChanLe = this.findViewById(R.id.tvResultForChanLeXocDia);
        tvResultForToBe = this.findViewById(R.id.tvResultForToBeXocDia);

        tvBeResult = this.findViewById(R.id.tvBeResultXocDia);
        tvChanResult = this.findViewById(R.id.tvChanResultXocDia);
        tvLeResult = this.findViewById(R.id.tvLeResultXocDia);
        tvToResult = this.findViewById(R.id.tvToResultXocDia);

        progressDialog = new ProgressDialog(GameXocDiaKuCasinoActivity.this);
        progressDialog.setMessage("Đang phân tích kết quả...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnAnalyzer.setOnClickListener(this);
        btnAnalyzer.setTransformationMethod(null);
    }

    private void clearData() {
        tvToResult.setText("");
        tvTableName.setText("");
        tvBeResult.setText("");
        tvChanResult.setText("");
        tvLeResult.setText("");
        tvResultForToBe.setText("");
        tvResultForChanLe.setText("");
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

    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void initData() {
        preferences = new MySharedPreferences(GameXocDiaKuCasinoActivity.this);
        listChanLe = new ArrayList<>(ResourceData.buildAllListTaiXiu());

        soLanBamHienTai = preferences.getLong(Const.KEY_SO_LAN_BAM_HIEN_TAI);
        buildRandomDataForToBe();

        //Set text guide
        if (NetworkUtils.haveNetwork(GameXocDiaKuCasinoActivity.this)) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Const.FIREBASE_REF.SETTINGS);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                        settingModel = dataSnap.getValue(SettingModel.class);
                        break;
                    }
                    if (settingModel != null) {
                        tvHuongDanAnalyzer.setText(settingModel.getHuongDanXocDia());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvHuongDanAnalyzer.setText(getResources().getString(R.string.huongdan));
                }
            });
        } else {
            tvHuongDanAnalyzer.setText(getResources().getString(R.string.huongdan));
        }
    }

    private void buildRandomDataForToBe() {
        listToBe = preferences.getListChanLe(Const.KEY_LIST_TO_BE);
        if (listToBe == null || listToBe.isEmpty()) {
            listToBe = new ArrayList<>(ResourceData.buildAllListChanLe());
        }
        Random random = new Random();
        int index = random.nextInt(listToBe.size());
        toBeSelected = listToBe.get(index);

        preferences.putChanLe(Const.KEY_TO_BE_SELECTED, toBeSelected);
        preferences.putListChanLe(Const.KEY_LIST_TO_BE, listToBe);

        listToBe.remove(index);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAnalyzerXocDia) {
            if (NetworkUtils.haveNetwork(GameXocDiaKuCasinoActivity.this)) {
                hideKeyboard();
                showProgressDialog();
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    hiddenProgressDialog();
                    onClickAnalyzerButton();
                }, 3000);
            } else {
                Toast.makeText(GameXocDiaKuCasinoActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Ham xu ly phan tich
     */
    private void onClickAnalyzerButton() {
        String tableCode = edtTableInput.getText().toString();
        boolean isCheck = validateTableCodeInput(tableCode);
        if (isCheck) {
            analyzerChanLe();
            analyzerToBe();
            tvTableName.setText(tableCode);
        }
    }

    private boolean validateTableCodeInput(String tableCode) {
        if (tableCode.isEmpty()) {
            Toast.makeText(GameXocDiaKuCasinoActivity.this, "Vui lòng nhập tên bàn trước", Toast.LENGTH_SHORT).show();
            hideKeyboard();
            return false;
        }
        return true;
    }

    /**
     * Ham xu ly voi To Be
     */
    private void analyzerToBe() {
        Random random = new Random();
        int tiLeBe = random.nextInt(99);
        int tiLeTo = 100 - tiLeBe;

        tvBeResult.setText(tiLeBe + "%");
        tvToResult.setText(tiLeTo + "%");
        tvResultForToBe.setText(tiLeTo > tiLeBe ? "Tài" : "Xỉu");
    }

    /**
     * Ham xu ly voi Chan Le
     */
    private void analyzerChanLe() {
        int indexNew;
        int indexOld = preferences.getInt(Const.KEY_INDEX_CHAN_LE_RANDOM);

        Random random = new Random();
        do {
            indexNew = random.nextInt(listChanLe.size());
        } while (indexOld == indexNew);

        ChanLe obj = listChanLe.get(indexNew);
        tvLeResult.setText(obj.getTiLeLe() + "%");
        tvChanResult.setText(obj.getTiLeChan() + "%");
        tvResultForChanLe.setText(obj.getTiLeChan() > obj.getTiLeLe() ? "Chẵn" : "Lẻ");

        preferences.putInt(Const.KEY_INDEX_CHAN_LE_RANDOM, indexNew);
    }
}