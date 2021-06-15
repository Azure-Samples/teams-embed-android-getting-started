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
        
        SwitchCompat switchCustomizeScreen = (SwitchCompat) root.findViewById(R.id.switch_customize_screen);
        switchCustomizeScreen.setOnCheckedChangeListener((view, isChecked) -> onSwitchCustomizeScreen(isChecked));

        return root;
    }

    private void onSwitchCustomizeScreen(boolean isChecked) {
        mPrefsEditor.putBoolean("CustomizeScreen_Enabled", isChecked);
        mPrefsEditor.apply();
    }

    private void onSwitchStagingScreen(boolean isChecked) {
        mPrefsEditor.putBoolean("StagingScreen_Enabled", isChecked);
        mPrefsEditor.apply();
    }

    private void clearPreferenceEditor() {
        mPrefsEditor.clear();
        mPrefsEditor.apply();
    }

    private void savePreferences() {
        String meetingUrlStr = meetingUrl.getText().toString();
        String acsTokenStr = acsToken.getText().toString();
        String groupIdStr = groupId.getText().toString();
        if(!meetingUrlStr.isEmpty())
        {
            mPrefsEditor.putString("Meeting_Url", meetingUrlStr);
        }
        if(!acsTokenStr.isEmpty()) {
            mPrefsEditor.putString("Access_Token", acsTokenStr);
            if(!groupIdStr.isEmpty()) {
                mPrefsEditor.putString("Group_Id", groupIdStr);
            }
        }
        mPrefsEditor.apply();
    }

    private void setUpPrefEditor() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Token_Pref", MODE_PRIVATE);
        if (prefs != null) {
            mPrefsEditor = prefs.edit();
        }
    }
}