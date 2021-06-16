/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.microsoft.TeamsEmbedAndroidGettingStarted.ui.acs.AcsFragment;
import com.microsoft.TeamsEmbedAndroidGettingStarted.ui.settings.SettingsFragment;
import com.microsoft.TeamsEmbedAndroidGettingStarted.ui.teams.TeamsFragment;

import java.util.Arrays;
import java.util.List;

public class MainFragmentViewPagerAdapter extends FragmentStateAdapter {

    private static final List<Fragment> FRAGMENTS_LIST = Arrays.asList(TeamsFragment.newInstance(), AcsFragment.newInstance(), SettingsFragment.newInstance());

    public MainFragmentViewPagerAdapter(@NonNull FragmentManager fm, @NonNull Lifecycle lf) {
        super(fm, lf);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FRAGMENTS_LIST.get(position);
    }

    @Override
    public int getItemCount() {
        return FRAGMENTS_LIST.size();
    }
}
