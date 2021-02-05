package com.sh.pytalapp.ui.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sh.pytalapp.R;
import com.sh.pytalapp.model.SoiCau;
import com.sh.pytalapp.utils.Const;
import com.sh.pytalapp.utils.NetworkUtils;
import com.sh.pytalapp.utils.StringFormatUtils;

public class GameSoiCauActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnCopy;
    private TextView tvDateCurrent, tvBachThuLo, tvSongThuLo, tvDanDe6X, tvTitleSoiCau;

    private SoiCau soiCau;
    private String mienSelected, soiCauRef, titleSoiCau;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_soicau);
        initView();
        initData();
    }

    private void initView() {
        tvTitleSoiCau = this.findViewById(R.id.tvSoiCauGame);
        tvDateCurrent = this.findViewById(R.id.tvDateCurrentSoiCauGame);
        tvBachThuLo = this.findViewById(R.id.tvBachthuLoValueGame);
        tvSongThuLo = this.findViewById(R.id.tvSongThuLoValueGame);
        tvDanDe6X = this.findViewById(R.id.tvDanDe6xValueGame);
        btnCopy = this.findViewById(R.id.btnCopyDanso6XGame);

        btnCopy.setOnClickListener(this);
    }

    private void initData() {
        Intent mIntent = getIntent();
        mienSelected = mIntent.getExtras().getString(Const.KEY_OPTION_SOICAU);
        if (Const.FIREBASE_REF.SOI_CAU_MB.equalsIgnoreCase(mienSelected)) {
            soiCauRef = Const.FIREBASE_REF.SOI_CAU_MB;
            titleSoiCau = "Soi cầu MB";
        } else if (Const.FIREBASE_REF.SOI_CAU_MT.equalsIgnoreCase(mienSelected)) {
            soiCauRef = Const.FIREBASE_REF.SOI_CAU_MT;
            titleSoiCau = "Soi cầu MT";
        } else {
            soiCauRef = Const.FIREBASE_REF.SOI_CAU_MN;
            titleSoiCau = "Soi cầu MN";
        }
        if (NetworkUtils.haveNetwork(GameSoiCauActivity.this)) {
            showProgressDialog();
            Query mDatabaseNewest = FirebaseDatabase.getInstance().getReference().child(soiCauRef).orderByChild("id").limitToLast(1);
            mDatabaseNewest.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        soiCau = childSnapshot.getValue(SoiCau.class);
                    }
                    if (soiCau != null && StringFormatUtils.getCurrentDateNotTimeStr().equalsIgnoreCase(soiCau.getCreatedDate())) {
                        tvBachThuLo.setText(soiCau.getBachThuLo());
                        tvDateCurrent.setText(soiCau.getCreatedDate());
                        tvSongThuLo.setText(soiCau.getSongThuLo1() + "  " + soiCau.getSongThuLo2());
                        tvDanDe6X.setText(StringFormatUtils.getTextFromSoiCauMbStringToView(soiCau.getListDanDe6X()));

                        if (soiCau.getNhaDai() != null && !soiCau.getNhaDai().isEmpty()) {
                            titleSoiCau += "\nĐài " + soiCau.getNhaDai();
                        }
                        tvTitleSoiCau.setText(titleSoiCau);
                    } else {
                        clearData();
                        Toast.makeText(GameSoiCauActivity.this, getResources().getString(R.string.chuacoketquahientai), Toast.LENGTH_SHORT).show();
                    }
                    hiddenProgressDialog();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    hiddenProgressDialog();
                }
            });
        } else {
            Toast.makeText(GameSoiCauActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
        tvTitleSoiCau.setText(titleSoiCau);
    }

    private void clearData() {
        tvDanDe6X.setText("...");
        tvBachThuLo.setText("...");
        tvSongThuLo.setText("...");
        tvDateCurrent.setText(StringFormatUtils.getCurrentDateNotTimeStr());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCopyDanso6XGame) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copy dàn đề 6x", soiCau.getListDanDe6X());
            clipboard.setPrimaryClip(clip);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
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


}
