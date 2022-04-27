package com.sh.pytalapp.ui.activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.sh.pytalapp.R;
import com.sh.pytalapp.utils.NetworkUtils;

import java.util.Random;

public class GameRongHoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAnalyzer;
    private EditText edtTableInput;
    private ProgressDialog progressDialog;
    private TextView tvTableName, tvResultForRongHo;
    private TextView tvRongResult, tvHoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rongho);
        intView();
        clearData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void intView() {
        btnAnalyzer = this.findViewById(R.id.btnAnalyzerTaiXiu);
        edtTableInput = this.findViewById(R.id.edtTableCodeTaiXiu);

        tvTableName = this.findViewById(R.id.tvNameTableTaiXiu);
        tvResultForRongHo = this.findViewById(R.id.tvResultForRongHo);

        tvRongResult = this.findViewById(R.id.tvRongResult);
        tvHoResult = this.findViewById(R.id.tvHoResult);

        progressDialog = new ProgressDialog(GameRongHoActivity.this);
        progressDialog.setMessage("Đang phân tích kết quả...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnAnalyzer.setOnClickListener(this);
        btnAnalyzer.setTransformationMethod(null);
    }

    private void clearData() {
        tvRongResult.setText("");
        tvTableName.setText("");
        tvHoResult.setText("");
        tvResultForRongHo.setText("");
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
        if (v.getId() == R.id.btnAnalyzerTaiXiu) {
            if (NetworkUtils.haveNetwork(GameRongHoActivity.this)) {
                hideKeyboard();
                showProgressDialog();
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    hiddenProgressDialog();
                    onClickAnalyzerButton();
                }, 3000);
            } else {
                Toast.makeText(GameRongHoActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
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
            analyzerRongHo();
            tvTableName.setText(tableCode);
        }
    }

    private boolean validateTableCodeInput(String tableCode) {
        if (tableCode.isEmpty()) {
            Toast.makeText(GameRongHoActivity.this, "Vui lòng nhập tên bàn trước", Toast.LENGTH_SHORT).show();
            hideKeyboard();
            return false;
        }
        return true;
    }

    /**
     * Ham xu ly voi Rong Ho
     */
    private void analyzerRongHo() {
        Random rand = new Random();
        int rong = rand.nextInt(100) + 1;
        int ho = 100 - rong;

        tvRongResult.setText(rong + "%");
        tvHoResult.setText(ho + "%");
        tvResultForRongHo.setText(rong > ho ? "Rồng" : "Hổ");
    }
}