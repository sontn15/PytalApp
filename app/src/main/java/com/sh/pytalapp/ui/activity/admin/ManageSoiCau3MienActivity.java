package com.sh.pytalapp.ui.activity.admin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.sh.pytalapp.R;
import com.sh.pytalapp.ui.fragment.SoiCauMienBacFragment;
import com.sh.pytalapp.ui.fragment.SoiCauMienNamFragment;
import com.sh.pytalapp.ui.fragment.SoiCauMienTrungFragment;

public class ManageSoiCau3MienActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_soicau3mien);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.mien_bac, SoiCauMienBacFragment.class)
                .add(R.string.mien_trung, SoiCauMienTrungFragment.class)
                .add(R.string.mien_nam, SoiCauMienNamFragment.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }
}
