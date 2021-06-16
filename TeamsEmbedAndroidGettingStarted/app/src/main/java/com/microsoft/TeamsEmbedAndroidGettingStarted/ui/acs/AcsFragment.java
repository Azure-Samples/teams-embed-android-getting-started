/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted.ui.acs;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.azure.android.communication.calling.Call;
import com.azure.android.communication.calling.CallAgent;
import com.azure.android.communication.calling.CallClient;
import com.azure.android.communication.calling.CallState;
import com.azure.android.communication.calling.GroupCallLocator;
import com.azure.android.communication.calling.JoinCallOptions;
import com.azure.android.communication.common.CommunicationTokenCredential;
import com.microsoft.TeamsEmbedAndroidGettingStarted.R;

import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static com.microsoft.skype.teams.util.PermissionUtil.ANDROID_PERMISSION_RECORD_AUDIO;

public class AcsFragment extends Fragment {
    private static int REQUEST_MICROPHONE = 202;
    private CallAgent callAgent;
    private CallClient mCallClient;
    private Call mCall;
    private SharedPreferences mPrefs;
    private TextView statusLabel;
    private Button joinAcsCallButton;
    private Button endAcsCallButton;
    private Button stopAcsButton;

    public static final int TAB_POSITION = 1;


    public static Fragment newInstance() {
        return new AcsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_acs, container, false);

        mPrefs = this.getActivity().getSharedPreferences(getString(R.string.sdk_shared_pref), MODE_PRIVATE);

        joinAcsCallButton = root.findViewById(R.id.join_acs_call);
        joinAcsCallButton.setOnClickListener(l -> {
            if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), ANDROID_PERMISSION_RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
            } else {
                joinAcsCall();
            }
        });

        endAcsCallButton = root.findViewById(R.id.end_acs_call);
        endAcsCallButton.setOnClickListener(l -> endAcsCall());

        stopAcsButton = root.findViewById(R.id.stop_acs);
        stopAcsButton.setOnClickListener(l -> stopAcs());

        statusLabel = root.findViewById(R.id.status_label);

        setupViews();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViews();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }

    private void setupViews() {
        boolean isTeamsSDKInitialized = mPrefs.getBoolean("isTeamsSDKInitialized", false);
        joinAcsCallButton.setEnabled(!isTeamsSDKInitialized);
        endAcsCallButton.setEnabled(!isTeamsSDKInitialized);
        stopAcsButton.setEnabled(!isTeamsSDKInitialized);
    }

    private void stopAcs() {
        mCall = null;
        updateStatusLabel(R.string.acs_disposing);
        if (callAgent != null && mCallClient != null) {
            synchronized (this) {
                callAgent.dispose();
                callAgent = null;
                mCallClient.dispose();
                mCallClient = null;
            }
            mPrefs.edit().putBoolean("isACSInitialized", false).apply();
            updateStatusLabel(R.string.acs_disposed);
        }
    }

    private void endAcsCall() {
        if (mCall != null) {
            mCall.hangUp(null).thenRun(() -> {
                updateStatusLabel(R.string.hangup_successful);
            });
        } else {
            updateStatusLabel(R.string.hangup_failed);
        }
    }

    private void joinAcsCall() {
        try {
            createAgent();
            mPrefs.edit().putBoolean("isACSInitialized", true).apply();
            startAcsCall();

        } catch (Exception ex){
            updateStatusLabel(R.string.acs_call_agent_failed);
            Toast.makeText(getContext(), "Failed to create call agent: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private CommunicationTokenCredential createCommunicationTokenCredential() {
        CommunicationTokenCredential credential = null;
        final String access_token = mPrefs.getString(getString(R.string.access_token), "");
        final String USER_ACCESS_TOKEN = (access_token != null && access_token.isEmpty()) ? "<USER_ACCESS_TOKEN>" : access_token;
        try {
            credential = new CommunicationTokenCredential(USER_ACCESS_TOKEN);
        } catch (Exception e) {
            updateStatusLabel(R.string.acs_credential_failed);
            Toast.makeText(getContext(), "Failed to create CommunicationTokenCredential: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return credential;
    }

    /**
     * Create the call agent for placing calls
     */
    private void createAgent() {
        try {
            CommunicationTokenCredential credential = createCommunicationTokenCredential();
            synchronized (this) {
                if (mCallClient == null) {
                    mCallClient = new CallClient();
                    callAgent = mCallClient.createCallAgent(this.getActivity().getApplicationContext(), credential).get();
                }
            }
            updateStatusLabel(R.string.acs_call_agent_success);
        } catch (Exception ex) {
            Toast.makeText(this.getActivity().getApplicationContext(), "Failed to create call agent.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startAcsCall() {
        final String groupIdStr = mPrefs.getString(getString(R.string.group_Id), "");
        final String groupId = (groupIdStr != null && groupIdStr.isEmpty()) ? "<GROUP_ID>" : groupIdStr;
        GroupCallLocator groupCallLocator = new GroupCallLocator(UUID.fromString(groupId));
        JoinCallOptions joinCallOptions = new JoinCallOptions();
        try {
            mCall = callAgent.join(getContext(), groupCallLocator, joinCallOptions);
            updateStatusLabel(R.string.acs_call_success);
            mCall.addOnStateChangedListener(p -> handleCallOnStateChanged(mCall.getState()));
        } catch (Exception e) {
            updateStatusLabel(R.string.acs_call_failed);
            Toast.makeText(getContext(), "Failed to get call object: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCallOnStateChanged(CallState state) {
        if (state == CallState.CONNECTING) {
            updateStatusLabel(R.string.call_connecting);
        } else if (state == CallState.IN_LOBBY) {
            updateStatusLabel(R.string.call_in_lobby);
        } else if (state == CallState.CONNECTED) {
            updateStatusLabel(R.string.call_connected);
        } else if (state == CallState.DISCONNECTED) {
            updateStatusLabel(R.string.no_active_call);
            mCall = null;
        }
    }

    /**
     * Shows call status in status label
     */
    private void updateStatusLabel(int status) {
        getActivity().runOnUiThread(() -> statusLabel.setText(status));
    }
}