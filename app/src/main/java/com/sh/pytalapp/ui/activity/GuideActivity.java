package com.sh.pytalapp.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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


public class GuideActivity extends AppCompatActivity {
    private TextView tvContact;
    private ImageView imvZalo;
    private SettingModel settingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        imvZalo = this.findViewById(R.id.imvZalo);
        tvContact = this.findViewById(R.id.tvContact);
        tvContact.setOnClickListener(view -> {
            ClipboardManager cm = (ClipboardManager) GuideActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(tvContact.getText());
        });
        setData();

        imvZalo.setOnClickListener(view -> {
            Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://zalo.me/" + settingModel.getPhoneNumber()));
            startActivity(mIntent);
        });
    }

    private void setData() {
        if (NetworkUtils.haveNetwork(GuideActivity.this)) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Const.FIREBASE_REF.SETTINGS);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                        settingModel = dataSnap.getValue(SettingModel.class);
                        break;
                    }
                    if (settingModel != null) {
                        tvContact.setText(settingModel.getPhoneNumber());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvContact.setText("0843320320");
                }
            });
        } else {
            tvContact.setText("0843320320");
        }
    }
}
