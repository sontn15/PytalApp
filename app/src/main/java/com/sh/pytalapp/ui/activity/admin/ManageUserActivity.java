package com.sh.pytalapp.ui.activity.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.sh.pytalapp.adapter.UserAdapter;
import com.sh.pytalapp.config.RecyclerItemTouchHelper;
import com.sh.pytalapp.database.FirebaseResource;
import com.sh.pytalapp.model.User;
import com.sh.pytalapp.utils.Const;
import com.sh.pytalapp.utils.NetworkUtils;
import com.sh.pytalapp.utils.StringFormatUtils;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ManageUserActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private NiceSpinner spnStatus;
    private EditText edtSearch;
    private Button btnSearch;
    private FloatingActionButton fabUser;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvTotalQuantity, tvTotalUsed, tvTotalFree;

    private RecyclerView rcvUser;
    private List<User> listUsers, listUserClone;
    private UserAdapter userAdapter;
    private ProgressDialog progressDialog;

    private View viewDialog;
    private AlertDialog userDlg;
    private Button btnCancelDlg, btnAgreeDlg;
    private EditText edtQuantityDlg;
    private RadioButton radioUser, radioAdmin;

    private String statusSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
        initView();
        initAdapter();
        initUserDialog();
    }

    private void initView() {
        fabUser = this.findViewById(R.id.fabUser);
        edtSearch = this.findViewById(R.id.edtSearchUser);
        rcvUser = this.findViewById(R.id.rcvUser);
        tvTotalFree = this.findViewById(R.id.tvTotalUserFreeManage);
        tvTotalUsed = this.findViewById(R.id.tvTotalUserUsedManage);
        tvTotalQuantity = this.findViewById(R.id.tvTotalUserManage);
        spnStatus = this.findViewById(R.id.spinnerStatusManageUser);
        refreshLayout = this.findViewById(R.id.refreshUser);
        btnSearch = this.findViewById(R.id.btnSearchManageUser);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ManageUserActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvUser.setItemAnimator(new DefaultItemAnimator());
        rcvUser.setLayoutManager(layoutManager);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rcvUser);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        fabUser.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    private void initAdapter() {
        listUsers = new ArrayList<>();
        listUserClone = new ArrayList<>();
        userAdapter = new UserAdapter(ManageUserActivity.this, listUsers, user -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copy code OTP", user.getCodeOTP());
            clipboard.setPrimaryClip(clip);
        });
        rcvUser.setAdapter(userAdapter);
        buildAllUsers();

        buildAllListStatus();
        spnStatus.setOnSpinnerItemSelectedListener((parent, view, position, id) ->
                statusSearch = parent.getItemAtPosition(position).toString());
    }

    private void initUserDialog() {
        userDlg = new AlertDialog.Builder(ManageUserActivity.this, R.style.CustomAlertDialog).create();
        viewDialog = getLayoutInflater().inflate(R.layout.dialog_new_user, null);

        edtQuantityDlg = viewDialog.findViewById(R.id.edtQuantityNewCodeUserDlg);
        radioUser = viewDialog.findViewById(R.id.radioUserDlg);
        radioAdmin = viewDialog.findViewById(R.id.radioManageUserDlg);
        btnCancelDlg = viewDialog.findViewById(R.id.btnCancelUserDlg);
        btnAgreeDlg = viewDialog.findViewById(R.id.btnAgreeUserDlg);
        userDlg.setView(viewDialog);

        btnCancelDlg.setOnClickListener(this);
        btnAgreeDlg.setOnClickListener(this);

        btnCancelDlg.setTransformationMethod(null);
        btnAgreeDlg.setTransformationMethod(null);
    }

    private void buildAllUsers() {
        if (NetworkUtils.haveNetwork(ManageUserActivity.this)) {
            showProgressDialog();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Const.FIREBASE_REF.USER);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<User> response = new ArrayList<>();
                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                        User user = dataSnap.getValue(User.class);
                        if (user != null) {
                            response.add(user);
                        }
                    }
                    if (response.size() > 0) {
                        listUsers.clear();
                        listUserClone.clear();
                        listUsers.addAll(response);
                        listUserClone.addAll(response);
                        calculatorUsers(listUsers);
                        userAdapter.notifyDataSetChanged();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(ManageUserActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void calculatorUsers(List<User> listUsers) {
        int totalUsed = 0;
        int totalFree = 0;
        int totalQuantity = 0;
        if (listUsers.size() > 0) {
            totalQuantity = listUsers.size();
            for (int i = 0; i < listUsers.size(); i++) {
                User user = listUsers.get(i);
                if (user.getSerialDevice() != null && !user.getSerialDevice().isEmpty()) {
                    totalUsed++;
                } else {
                    totalFree++;
                }
            }
        }
        tvTotalFree.setText(totalFree + "");
        tvTotalUsed.setText(totalUsed + "");
        tvTotalQuantity.setText(totalQuantity + "");
    }

    private void buildAllListStatus() {
        edtSearch.setText("");
        spnStatus.attachDataSource(new LinkedList<>(Arrays.asList(Const.STATUS_USER.ALL, Const.STATUS_USER.ACTIVE, Const.STATUS_USER.INACTIVE)));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof UserAdapter.UserViewHolder) {
            User userDelete = listUsers.get(viewHolder.getAdapterPosition());
            onSwipeDeleteUser(userDelete);
        }
    }

    private void onSwipeDeleteUser(final User userDelete) {
        String message = "Bạn chắc chắn xóa mã code " + userDelete.getCodeOTP() + "?";
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageUserActivity.this);
        builder.setTitle("Xác nhận");
        builder.setMessage(message);
        builder.setPositiveButton("Đồng ý", (dialog, which) -> {
            if (NetworkUtils.haveNetwork(ManageUserActivity.this)) {
                FirebaseResource.deleteUser(userDelete);
                buildAllUsers();
                buildAllListStatus();
                edtSearch.setText("");
                Toast.makeText(ManageUserActivity.this, "Xóa mã code thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ManageUserActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy bỏ", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(ManageUserActivity.this.getResources().getColor(R.color.colorVinID));
    }

    @Override
    public void onRefresh() {
        buildAllUsers();
        buildAllListStatus();
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearchManageUser: {
                onClickSearchUser();
                break;
            }
            case R.id.fabUser: {
                onClickFabUser();
                break;
            }
            case R.id.btnCancelUserDlg: {
                onClickCancelDialog();
                break;
            }
            case R.id.btnAgreeUserDlg: {
                onClickAgreeDialog();
                break;
            }
        }
    }

    private void onClickSearchUser() {
        showProgressDialog();
        String textSearch = StringFormatUtils.convertUTF8ToString(edtSearch.getText().toString().toLowerCase());
        if (Const.STATUS_USER.ACTIVE.equalsIgnoreCase(statusSearch)) {
            listUsers.clear();
            for (User user : listUserClone) {
                String codeOTP = StringFormatUtils.convertUTF8ToString(user.getCodeOTP().toLowerCase());
                String role = StringFormatUtils.convertUTF8ToString(user.getRole().toLowerCase());
                if (!StringFormatUtils.isNullOrEmpty(user.getSerialDevice())) {
                    if (role != null && codeOTP != null && textSearch != null
                            && (textSearch.isEmpty() || (codeOTP.contains(textSearch)) || role.contains(textSearch))) {
                        listUsers.add(user);
                    }
                }
            }
        } else if (Const.STATUS_USER.INACTIVE.equalsIgnoreCase(statusSearch)) {
            listUsers.clear();
            for (User user : listUserClone) {
                String codeOTP = StringFormatUtils.convertUTF8ToString(user.getCodeOTP().toLowerCase());
                String role = StringFormatUtils.convertUTF8ToString(user.getRole().toLowerCase());
                if (StringFormatUtils.isNullOrEmpty(user.getSerialDevice())) {
                    if (role != null && codeOTP != null && textSearch != null
                            && (textSearch.isEmpty() || (codeOTP.contains(textSearch)) || role.contains(textSearch))) {
                        listUsers.add(user);
                    }
                }
            }
        } else {
            listUsers.clear();
            for (User user : listUserClone) {
                String codeOTP = StringFormatUtils.convertUTF8ToString(user.getCodeOTP().toLowerCase());
                String role = StringFormatUtils.convertUTF8ToString(user.getRole().toLowerCase());
                if (role != null && codeOTP != null && textSearch != null
                        && (textSearch.isEmpty() || (codeOTP.contains(textSearch)) || role.contains(textSearch))) {
                    listUsers.add(user);
                }
            }
        }
        userAdapter.notifyDataSetChanged();
        hiddenProgressDialog();
    }

    private void onClickFabUser() {
        clearDataDialog();
        showUserDialog();
    }

    private void onClickCancelDialog() {
        clearDataDialog();
        hiddenUserDialog();
    }

    private void onClickAgreeDialog() {
        String quantityStr = edtQuantityDlg.getText().toString();
        if (StringFormatUtils.isNullOrEmpty(quantityStr) || quantityStr.startsWith("0")) {
            Toast.makeText(ManageUserActivity.this, getResources().getString(R.string.vui_long_nhap_day_du_thong_tin), Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkUtils.haveNetwork(ManageUserActivity.this)) {
                String role;
                if (radioUser.isChecked()) {
                    role = Const.USER_ROLE.USER;
                } else {
                    role = Const.USER_ROLE.ADMIN;
                }
                try {
                    showProgressDialog();
                    int quantity = Integer.parseInt(quantityStr);
                    FirebaseResource.createRandomUser(quantity, role);
                    buildAllUsers();
                    hiddenUserDialog();
                    hiddenProgressDialog();
                    Toast.makeText(ManageUserActivity.this, getResources().getString(R.string.themmoithanhcong), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    hiddenProgressDialog();
                    Toast.makeText(ManageUserActivity.this, getResources().getString(R.string.vui_long_nhap_dung_thong_tin), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ManageUserActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearDataDialog() {
        edtSearch.setText("");
        edtQuantityDlg.requestFocus();
        edtQuantityDlg.setText("");
        radioUser.setChecked(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        edtSearch.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (userDlg != null) {
            userDlg.dismiss();
            userDlg = null;
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

    private void showUserDialog() {
        if (userDlg != null && !userDlg.isShowing()) {
            userDlg.show();
        }
    }

    private void hiddenUserDialog() {
        if (userDlg != null && userDlg.isShowing()) {
            userDlg.dismiss();
        }
    }
}
