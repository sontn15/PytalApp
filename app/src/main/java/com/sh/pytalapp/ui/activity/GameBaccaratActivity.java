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
import android.widget.LinearLayout;
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
import com.sh.pytalapp.model.SettingModel;
import com.sh.pytalapp.model.ToBe;
import com.sh.pytalapp.utils.Const;
import com.sh.pytalapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameBaccaratActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAnalyzer;
    private EditText edtTableInput;
    private ProgressDialog progressDialog;
    private TextView tvHuongDanAnalyzer, tvTitleNhapTenBan;
    private LinearLayout llActivityAnalyzer, llBorderAnalyzer;
    private TextView tvTableName, tvResultForPlayerBankerBaccarat;
    private TextView tvBankerResultBaccarat, tvPlayerResultBaccarat;

    private Long soLanBamHienTai;
    private ToBe toBeSelected;
    private List<ToBe> listToBe;
    private MySharedPreferences preferences;
    private SettingModel settingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_baccarat);
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
        btnAnalyzer = this.findViewById(R.id.btnAnalyzerBaccarat);
        edtTableInput = this.findViewById(R.id.edtTableCodeBaccarat);
        tvHuongDanAnalyzer = this.findViewById(R.id.tvHuongDanAnalyzerBaccarat);

        tvTableName = this.findViewById(R.id.tvNameTableBaccarat);
        tvResultForPlayerBankerBaccarat = this.findViewById(R.id.tvResultForPlayerBankerBaccarat);
        tvTitleNhapTenBan = this.findViewById(R.id.tvTitleNhapTenBanBaccarat);
        tvBankerResultBaccarat = this.findViewById(R.id.tvBankerResultBaccarat);
        tvPlayerResultBaccarat = this.findViewById(R.id.tvPlayerResultBaccarat);

        llBorderAnalyzer = this.findViewById(R.id.llBorderAnalyzerBaccarat);
        llActivityAnalyzer = this.findViewById(R.id.llActivityAnalyzerBaccarat);

        progressDialog = new ProgressDialog(GameBaccaratActivity.this);
        progressDialog.setMessage("Đang phân tích kết quả...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnAnalyzer.setOnClickListener(this);
        btnAnalyzer.setTransformationMethod(null);
    }

    private void clearData() {
        tvTableName.setText("");
        tvBankerResultBaccarat.setText("");
        tvPlayerResultBaccarat.setText("");
        tvResultForPlayerBankerBaccarat.setText("");
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
        preferences = new MySharedPreferences(GameBaccaratActivity.this);
        soLanBamHienTai = preferences.getLong(Const.KEY_SO_LAN_BAM_HIEN_TAI);

        buildRandomDataForPlayerBanker();

        //Set text guide
        if (NetworkUtils.haveNetwork(GameBaccaratActivity.this)) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Const.FIREBASE_REF.SETTINGS);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                        settingModel = dataSnap.getValue(SettingModel.class);
                        break;
                    }
                    if (settingModel != null) {
                        tvHuongDanAnalyzer.setText(settingModel.getHuongDanBaccarat());
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

    private void buildRandomDataForPlayerBanker() {
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
        if (v.getId() == R.id.btnAnalyzerBaccarat) {
            if (NetworkUtils.haveNetwork(GameBaccaratActivity.this)) {
                hideKeyboard();
                showProgressDialog();
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    hiddenProgressDialog();
                    onClickAnalyzerButton();
                }, 3000);
            } else {
                Toast.makeText(GameBaccaratActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
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
            analyzerPlayerBanker();
            tvTableName.setText(tableCode);
        }
    }

    private boolean validateTableCodeInput(String tableCode) {
        if (tableCode.isEmpty()) {
            Toast.makeText(GameBaccaratActivity.this, "Vui lòng nhập tên bàn trước", Toast.LENGTH_SHORT).show();
            hideKeyboard();
            return false;
        }
        return true;
    }

    /**
     * Ham xu ly voi Player Banker
     */
    private void analyzerPlayerBanker() {
        toBeSelected = preferences.getChanLe(Const.KEY_TO_BE_SELECTED);
        soLanBamHienTai = preferences.getLong(Const.KEY_SO_LAN_BAM_HIEN_TAI);

        int tongSoLanBam = toBeSelected.getTongSoLanQuay();
        soLanBamHienTai = soLanBamHienTai + 1;

        if (soLanBamHienTai > tongSoLanBam) {
            soLanBamHienTai = 1L;
            buildRandomDataForPlayerBanker();
        }
        int tiLePlayer = toBeSelected.getListTiLeBe().get(Integer.parseInt((soLanBamHienTai - 1) + ""));
        int tiLeBanker = toBeSelected.getListTileTo().get(Integer.parseInt((soLanBamHienTai - 1) + ""));

        tvPlayerResultBaccarat.setText(tiLePlayer + "%");
        tvBankerResultBaccarat.setText(tiLeBanker + "%");
        tvResultForPlayerBankerBaccarat.setText(tiLeBanker >= tiLePlayer ? "Banker" : "Player");

        preferences.putLong(Const.KEY_SO_LAN_BAM_HIEN_TAI, soLanBamHienTai);
        preferences.putChanLe(Const.KEY_TO_BE_SELECTED, toBeSelected);
        preferences.putListChanLe(Const.KEY_LIST_TO_BE, listToBe);
    }
}