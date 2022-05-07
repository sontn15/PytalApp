package com.sh.pytalapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.pytalapp.R;


public class DashboardXocDiaActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnKuCasino, btnWmCasino, btnBBinCasino, btnXocDia3D;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_xocdia);
        initView();
    }

    private void initView() {
        btnKuCasino = this.findViewById(R.id.btnKuCasino);
        btnWmCasino = this.findViewById(R.id.btnWmCasino);
        btnBBinCasino = this.findViewById(R.id.btnBBinCasino);
        btnXocDia3D = this.findViewById(R.id.btnXocDia3D);

        btnKuCasino.setTransformationMethod(null);
        btnWmCasino.setTransformationMethod(null);
        btnBBinCasino.setTransformationMethod(null);
        btnXocDia3D.setTransformationMethod(null);

        btnKuCasino.setOnClickListener(this);
        btnWmCasino.setOnClickListener(this);
        btnBBinCasino.setOnClickListener(this);
        btnXocDia3D.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnKuCasino: {
                onClickKuCasino();
                break;
            }
            case R.id.btnWmCasino: {
                onClickWmCasino();
                break;
            }
            case R.id.btnBBinCasino: {
                onClickBBinCasino();
                break;
            }
            case R.id.btnXocDia3D: {
                onClickXocDia3D();
                break;
            }
        }
    }

    private void onClickKuCasino() {
        Intent mIntent = new Intent(DashboardXocDiaActivity.this, GameXocDiaKuCasinoActivity.class);
        startActivity(mIntent);
    }

    private void onClickWmCasino() {
        Intent mIntent = new Intent(DashboardXocDiaActivity.this, GameXocDiaActivity.class);
        startActivity(mIntent);
    }

    private void onClickBBinCasino() {
        Intent mIntent = new Intent(DashboardXocDiaActivity.this, GameXocDiaActivity.class);
        startActivity(mIntent);
    }

    private void onClickXocDia3D() {
        Intent mIntent = new Intent(DashboardXocDiaActivity.this, GameXocDiaActivity.class);
        startActivity(mIntent);
    }

}
