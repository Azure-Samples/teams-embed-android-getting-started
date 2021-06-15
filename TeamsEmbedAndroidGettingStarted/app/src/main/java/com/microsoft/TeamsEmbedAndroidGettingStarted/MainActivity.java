/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.microsoft.TeamsEmbedAndroidGettingStarted.ui.acs.AcsFragment;
import com.microsoft.TeamsEmbedAndroidGettingStarted.ui.settings.SettingsFragment;
import com.microsoft.TeamsEmbedAndroidGettingStarted.ui.teams.TeamsFragment;

public class MainActivity extends AppCompatActivity {

    ViewPager2 mViewPager;
    BottomNavigationView mBottomNavigationView;

    MainFragmentViewPagerAdapter mPagerAdapter;
    private int mCurrentTabPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView = findViewById(R.id.nav_view);
        mViewPager = findViewById(R.id.viewPager);

        mPagerAdapter = new MainFragmentViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_teams:
                    mCurrentTabPosition = TeamsFragment.TAB_POSITION;
                    mViewPager.setCurrentItem(mCurrentTabPosition);
                    break;
                case R.id.navigation_acs:
                    mCurrentTabPosition = AcsFragment.TAB_POSITION;
                    mViewPager.setCurrentItem(mCurrentTabPosition);
                    break;
                case R.id.navigation_settings:
                    mCurrentTabPosition = SettingsFragment.TAB_POSITION;
                    mViewPager.setCurrentItem(mCurrentTabPosition);
                    break;
                default:
                    return false;
            }
            return true;
        });
    }
}