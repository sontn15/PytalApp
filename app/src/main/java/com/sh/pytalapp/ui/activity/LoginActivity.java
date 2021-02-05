package com.sh.pytalapp.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.pytalapp.R;
import com.sh.pytalapp.database.MySharedPreferences;
import com.sh.pytalapp.model.User;
import com.sh.pytalapp.ui.activity.admin.DashboardAdminActivity;
import com.sh.pytalapp.utils.Const;
import com.sh.pytalapp.utils.NetworkUtils;
import com.sh.pytalapp.utils.StringFormatUtils;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private TextView tvGuide;
    private PinView pinViewOTP;
    private RelativeLayout rlLogin;
    private ProgressDialog progressDialog;

    private String phoneInformation;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @SuppressLint("HardwareIds")
    private void initData() {
        preferences = new MySharedPreferences(this);
        phoneInformation = "[" + Build.ID + "||" + Build.MANUFACTURER + "||" + Build.MODEL + "||" + Build.PRODUCT + "]";

        String codeOTPSaved = preferences.getString(Const.KEY_CODE_OTP);
        if (codeOTPSaved != null && !codeOTPSaved.isEmpty()) {
            pinViewOTP.setText(codeOTPSaved);
        }
        preferences.clearDataByKey(Const.KEY_LIST_TO_BE);
        preferences.clearDataByKey(Const.KEY_TO_BE_SELECTED);
        preferences.clearDataByKey(Const.KEY_SO_LAN_BAM_HIEN_TAI);
        preferences.clearDataByKey(Const.KEY_INDEX_CHAN_LE_RANDOM);
    }

    private void initView() {
        pinViewOTP = this.findViewById(R.id.pinViewLogin);
        tvGuide = this.findViewById(R.id.tvGuideLogin);
        btnLogin = this.findViewById(R.id.btnLogin);
        rlLogin = this.findViewById(R.id.rlLogin);
        btnLogin.setTransformationMethod(null);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnLogin.setOnClickListener(this);
        tvGuide.setOnClickListener(this);
        btnLogin.setTransformationMethod(null);
        tvGuide.setTransformationMethod(null);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin: {
                onClickLogin();
                break;
            }
            case R.id.tvGuideLogin: {
                onClickGuide();
                break;
            }
        }
    }

    /**
     * Ham xu ly button huong dan
     */
    private void onClickGuide() {
        Intent mIntent = new Intent(this, GuideActivity.class);
        startActivity(mIntent);
    }

    /**
     * Ham xu ly login button
     */
    private void onClickLogin() {
        final String codeOTP = Objects.requireNonNull(pinViewOTP.getText()).toString();
        if (codeOTP.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập mã phần mềm", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetworkUtils.haveNetwork(LoginActivity.this)) {
            hideKeyboard();
            showProgressDialog();
            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users").child(codeOTP);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        if (user.getSerialDevice() == null || user.getSerialDevice().isEmpty()) {
                            mDatabase.child("serialDevice").setValue(phoneInformation);
                            mDatabase.child("lastLogin").setValue(StringFormatUtils.getCurrentDateStr());
                            loginSuccessfully(user, codeOTP);
                        } else if (user.getSerialDevice() != null && !user.getSerialDevice().isEmpty()
                                && !phoneInformation.equalsIgnoreCase(user.getSerialDevice())) {
                            hiddenProgressDialog();
                            Toast.makeText(LoginActivity.this, "Mã phần mềm đã hết hiệu lực, thử lại với mã khác", Toast.LENGTH_SHORT).show();
                        } else if (phoneInformation.equalsIgnoreCase(user.getSerialDevice())) {
                            mDatabase.child("lastLogin").setValue(StringFormatUtils.getCurrentDateStr());
                            loginSuccessfully(user, codeOTP);
                        } else {
                            hiddenProgressDialog();
                            Toast.makeText(LoginActivity.this, "Mã phần mềm không đúng, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hiddenProgressDialog();
                        Toast.makeText(LoginActivity.this, "Mã phần mềm không đúng, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    hiddenProgressDialog();
                    Toast.makeText(LoginActivity.this, "Mã phần mềm không đúng, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void loginSuccessfully(User user, String codeOTP) {
        preferences.putString(Const.KEY_CODE_OTP, codeOTP);
        preferences.putUser(Const.KEY_USER_LOGIN, user);
        hiddenProgressDialog();

        Intent mIntent;
        if (Const.USER_ROLE.ADMIN.equalsIgnoreCase(user.getRole())) {
            mIntent = new Intent(LoginActivity.this, DashboardAdminActivity.class);
        } else {
            mIntent = new Intent(LoginActivity.this, DashboardGameActivity.class);
        }
        startActivity(mIntent);
        finish();
    }
}
