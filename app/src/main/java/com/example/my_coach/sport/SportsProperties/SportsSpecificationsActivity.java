package com.example.my_coach.sport.SportsProperties;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.my_coach.R;
import com.google.android.material.tabs.TabLayout;


public class SportsSpecificationsActivity extends AppCompatActivity {
    public static String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_specifications);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.pager);

        String id=getIntent().getStringExtra("id");
        UID=id;





        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager(), 0));
        tabLayout.setupWithViewPager(viewPager);
    }

}