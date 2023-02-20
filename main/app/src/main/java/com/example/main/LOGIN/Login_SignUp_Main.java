package com.example.main.LOGIN;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.main.R;
import com.google.android.material.tabs.TabLayout;
public class Login_SignUp_Main extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter adapter;
    public static final String ip = "192.168.1.107"; // the ip to pass to all java files
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        tabLayout.addTab(tabLayout.newTab().setText("Login"));//add the texts for the tabs (actual fragments are added in adapter)
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        FragmentManager fragmentManager = getSupportFragmentManager();//need fragment manager for adapter
        adapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//change position when selected
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {//leave empty
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {//leave empty
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) { //
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}