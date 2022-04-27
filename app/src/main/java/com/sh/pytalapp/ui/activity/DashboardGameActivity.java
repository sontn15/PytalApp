package com.sh.pytalapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.pytalapp.R;
import com.sh.pytalapp.ui.activity.admin.DashboardAdminActivity;


public class DashboardGameActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnWMXocDia, btnWMBaccarat, btnWMTaiXiu, btnWMSoiCau, btnRongHo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_game);
        initView();
    }

    private void initView() {
        btnWMBaccarat = this.findViewById(R.id.btnWMBaccarat);
        btnWMXocDia = this.findViewById(R.id.btnWMXocDia);
        btnWMTaiXiu = this.findViewById(R.id.btnWMTaiXiu);
        btnWMSoiCau = this.findViewById(R.id.btnWMSoiCau);
        btnRongHo = this.findViewById(R.id.btnRongHo);

        btnWMBaccarat.setTransformationMethod(null);
        btnWMXocDia.setTransformationMethod(null);
        btnWMTaiXiu.setTransformationMethod(null);
        btnWMSoiCau.setTransformationMethod(null);
        btnRongHo.setTransformationMethod(null);

        btnWMBaccarat.setOnClickListener(this);
        btnWMXocDia.setOnClickListener(this);
        btnWMTaiXiu.setOnClickListener(this);
        btnWMSoiCau.setOnClickListener(this);
        btnRongHo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnWMXocDia: {
                onClickXocDia();
                break;
            }
            case R.id.btnWMBaccarat: {
                onClickBaccarat();
                break;
            }
            case R.id.btnWMTaiXiu: {
                onClickTaiXiu();
                break;
            }
            case R.id.btnRongHo: {
                onClickRongHo();
                break;
            }
            case R.id.btnWMSoiCau: {
                onClickSoiCau();
                break;
            }
        }
    }

    private void onClickXocDia() {
        Intent mIntent = new Intent(DashboardGameActivity.this, DashboardXocDiaActivity.class);
        startActivity(mIntent);
    }

    private void onClickBaccarat() {
        Intent mIntent = new Intent(DashboardGameActivity.this, GameBaccaratActivity.class);
        startActivity(mIntent);
    }

    private void onClickTaiXiu() {
        Intent mIntent = new Intent(DashboardGameActivity.this, GameTaiXiuActivity.class);
        startActivity(mIntent);
    }

    private void onClickRongHo() {
        Intent mIntent = new Intent(DashboardGameActivity.this, GameRongHoActivity.class);
        startActivity(mIntent);
    }

    private void onClickSoiCau() {
        Intent mIntent = new Intent(DashboardGameActivity.this, OptionSoiCauActivity.class);
        startActivity(mIntent);
    }

}
