package com.sh.pytalapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.pytalapp.R;
import com.sh.pytalapp.utils.Const;

public class OptionSoiCauActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnMienBac, btnMienTrung, btnMienNam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_soicau);
        initViews();
    }

    private void initViews() {
        btnMienBac = this.findViewById(R.id.btnMienBacOptionSoiCau);
        btnMienTrung = this.findViewById(R.id.btnMienTrungOptionSoiCau);
        btnMienNam = this.findViewById(R.id.btnMienNamOptionSoiCau);

        btnMienBac.setOnClickListener(this);
        btnMienTrung.setOnClickListener(this);
        btnMienNam.setOnClickListener(this);
        btnMienBac.setTransformationMethod(null);
        btnMienTrung.setTransformationMethod(null);
        btnMienNam.setTransformationMethod(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMienBacOptionSoiCau: {
                onClickButtonMienBac();
                break;
            }
            case R.id.btnMienTrungOptionSoiCau: {
                onClickButtonMienTrung();
                break;
            }
            case R.id.btnMienNamOptionSoiCau: {
                onClickButtonMienNam();
                break;
            }
        }
    }

    private void onClickButtonMienBac() {
        Intent mIntent = new Intent(OptionSoiCauActivity.this, GameSoiCauActivity.class);
        mIntent.putExtra(Const.KEY_OPTION_SOICAU, Const.FIREBASE_REF.SOI_CAU_MB);
        startActivity(mIntent);
    }

    private void onClickButtonMienTrung() {
        Intent mIntent = new Intent(OptionSoiCauActivity.this, GameSoiCauActivity.class);
        mIntent.putExtra(Const.KEY_OPTION_SOICAU, Const.FIREBASE_REF.SOI_CAU_MT);
        startActivity(mIntent);
    }

    private void onClickButtonMienNam() {
        Intent mIntent = new Intent(OptionSoiCauActivity.this, GameSoiCauActivity.class);
        mIntent.putExtra(Const.KEY_OPTION_SOICAU, Const.FIREBASE_REF.SOI_CAU_MN);
        startActivity(mIntent);
    }
}
