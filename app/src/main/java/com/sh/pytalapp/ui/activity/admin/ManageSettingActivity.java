package com.sh.pytalapp.ui.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.pytalapp.R;
import com.sh.pytalapp.model.SettingModel;
import com.sh.pytalapp.utils.Const;
import com.sh.pytalapp.utils.NetworkUtils;

public class ManageSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnUpdate;
    private SettingModel settingModel;
    private EditText edtPhoneNumber, edtHuongDanXocDia, edtHuongDanTaiXiu, edtHuongDanBaccarat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_settings);
        initViews();
        setData();
    }

    private void initViews() {
        edtPhoneNumber = this.findViewById(R.id.edtSDTSetting);
        edtHuongDanXocDia = this.findViewById(R.id.edtHuongDanXocDiaSetting);
        edtHuongDanTaiXiu = this.findViewById(R.id.edtHuongDanTaiXiuSetting);
        edtHuongDanBaccarat = this.findViewById(R.id.edtHuongDanBaccaratSetting);
        btnUpdate = this.findViewById(R.id.btnUpdateSettings);

        btnUpdate.setOnClickListener(this);
        btnUpdate.setTransformationMethod(null);
    }

    private void setData() {
        if (NetworkUtils.haveNetwork(ManageSettingActivity.this)) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Const.FIREBASE_REF.SETTINGS);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                        settingModel = dataSnap.getValue(SettingModel.class);
                        break;
                    }
                    if (settingModel != null) {
                        edtPhoneNumber.setText(settingModel.getPhoneNumber());
                        edtHuongDanTaiXiu.setText(settingModel.getHuongDanTaiXiu());
                        edtHuongDanXocDia.setText(settingModel.getHuongDanXocDia());
                        edtHuongDanBaccarat.setText(settingModel.getHuongDanBaccarat());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(ManageSettingActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdateSettings) {
            onClickUpdateData();
        }
    }

    private void onClickUpdateData() {
        String phoneNumber = edtPhoneNumber.getText().toString();
        String huongDanTaiXiu = edtHuongDanTaiXiu.getText().toString();
        String huongDanXocDia = edtHuongDanXocDia.getText().toString();
        String huongDanBaccarat = edtHuongDanBaccarat.getText().toString();

        if (phoneNumber.isEmpty() || huongDanTaiXiu.isEmpty() || huongDanXocDia.isEmpty() || huongDanBaccarat.isEmpty()) {
            Toast.makeText(ManageSettingActivity.this, getResources().getString(R.string.nhap_daydy_thong_tin), Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkUtils.haveNetwork(ManageSettingActivity.this)) {
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_REF.SETTINGS);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String message = getResources().getString(R.string.capnhatthanhcong);
                        if (settingModel == null) {
                            settingModel = SettingModel.builder().id(1).build();
                            message = getResources().getString(R.string.themmoithanhcong);
                        }
                        settingModel.setHuongDanTaiXiu(huongDanTaiXiu);
                        settingModel.setHuongDanXocDia(huongDanXocDia);
                        settingModel.setHuongDanBaccarat(huongDanBaccarat);
                        settingModel.setPhoneNumber(phoneNumber);
                        mDatabase.child(settingModel.getId() + "").setValue(settingModel);
                        Toast.makeText(ManageSettingActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(ManageSettingActivity.this, getResources().getString(R.string.check_connection_network), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
