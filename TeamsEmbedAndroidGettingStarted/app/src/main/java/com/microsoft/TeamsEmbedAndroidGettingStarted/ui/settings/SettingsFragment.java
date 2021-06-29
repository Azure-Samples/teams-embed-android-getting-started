/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.microsoft.TeamsEmbedAndroidGettingStarted.R;
import com.microsoft.skype.teams.util.SystemUtil;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private EditText meetingUrl;
    private EditText acsToken;
    private EditText groupId;
    private SharedPreferences.Editor mPrefsEditor;
    public static final int TAB_POSITION = 2;

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        setUpPrefEditor();

        Button saveButton = root.findViewById(R.id.save_button);
        saveButton.setOnClickListener(l -> savePreferences());

        meetingUrl = root.findViewById(R.id.meeting_url);
        acsToken = root.findViewById(R.id.acs_token);
        groupId = root.findViewById(R.id.group_id);

        SwitchCompat switchStagingScreen = (SwitchCompat) root.findViewById(R.id.switch_staging_screen);
        switchStagingScreen.setOnCheckedChangeListener((view, isChecked) -> onSwitchStagingScreen(isChecked));

        return root;
    }

    private void onSwitchStagingScreen(boolean isChecked) {
        mPrefsEditor.putBoolean(getString(R.string.stagingScreen_enabled), isChecked);
        mPrefsEditor.apply();
    }

    private void savePreferences() {
        String meetingUrlStr = meetingUrl.getText().toString();
        String acsTokenStr = acsToken.getText().toString();
        String groupIdStr = groupId.getText().toString();
        if(!meetingUrlStr.isEmpty())
        {
            mPrefsEditor.putString(getString(R.string.meeting_url), meetingUrlStr);
        }
        if(!acsTokenStr.isEmpty()) {
            mPrefsEditor.putString(getString(R.string.access_token), acsTokenStr);
            if(!groupIdStr.isEmpty()) {
                mPrefsEditor.putString(getString(R.string.group_Id), groupIdStr);
            }
        }
        mPrefsEditor.apply();
        SystemUtil.showToast(this.getActivity(), getString(R.string.save_success));
    }

    private void setUpPrefEditor() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences(getString(R.string.sdk_shared_pref), MODE_PRIVATE);
        if (prefs != null) {
            mPrefsEditor = prefs.edit();
        }
    }
}