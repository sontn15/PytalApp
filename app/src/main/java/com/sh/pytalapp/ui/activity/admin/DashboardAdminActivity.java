package com.sh.pytalapp.ui.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.pytalapp.R;
import com.sh.pytalapp.ui.activity.DashboardGameActivity;


public class DashboardAdminActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnUser, btnSetting, btnSoiCauMb, btnPlayGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        initView();
    }

    private void initView() {
        btnUser = this.findViewById(R.id.btnUserAdmin);
        btnSetting = this.findViewById(R.id.btnSetting);
        btnSoiCauMb = this.findViewById(R.id.btnSoiCauMbAdmin);
        btnPlayGame = this.findViewById(R.id.btnGameAdmin);

        btnUser.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnSoiCauMb.setOnClickListener(this);
        btnPlayGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUserAdmin: {
                onClickUserManage();
                break;
            }
            case R.id.btnSetting: {
                onClickSetting();
                break;
            }
            case R.id.btnSoiCauMbAdmin: {
                onClickSoiCauMb();
                break;
            }
            case R.id.btnGameAdmin: {
                onClickPlayGame();
                break;
            }
        }
    }

    private void onClickUserManage() {
        Intent mIntent = new Intent(DashboardAdminActivity.this, ManageUserActivity.class);
        startActivity(mIntent);
    }

    private void onClickSetting() {
        Intent mIntent = new Intent(DashboardAdminActivity.this, ManageSettingActivity.class);
        startActivity(mIntent);
    }

    private void onClickSoiCauMb() {
        Intent mIntent = new Intent(DashboardAdminActivity.this, ManageSoiCau3MienActivity.class);
        startActivity(mIntent);
    }

    private void onClickPlayGame() {
        Intent mIntent = new Intent(DashboardAdminActivity.this, DashboardGameActivity.class);
        startActivity(mIntent);
    }
}
