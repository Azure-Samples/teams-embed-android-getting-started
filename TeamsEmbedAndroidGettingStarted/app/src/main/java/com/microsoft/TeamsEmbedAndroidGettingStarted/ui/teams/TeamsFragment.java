/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted.ui.teams;

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

import com.azure.android.communication.common.CommunicationTokenCredential;
import com.azure.android.communication.common.CommunicationTokenRefreshOptions;
import com.azure.android.communication.ui.meetings.MeetingUIClient;
import com.azure.android.communication.ui.meetings.MeetingUIClientCall;
import com.azure.android.communication.ui.meetings.MeetingUIClientGroupCallLocator;
import com.azure.android.communication.ui.meetings.MeetingUIClientIconType;
import com.azure.android.communication.ui.meetings.MeetingUIClientJoinOptions;
import com.azure.android.communication.ui.meetings.MeetingUIClientTeamsMeetingLinkLocator;
import com.microsoft.TeamsEmbedAndroidGettingStarted.R;
import com.microsoft.TeamsEmbedAndroidGettingStarted.delegates.CustomEventListener;
import com.microsoft.TeamsEmbedAndroidGettingStarted.delegates.CustomIdentityProvider;
import com.microsoft.skype.teams.util.SystemUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import static android.content.Context.MODE_PRIVATE;
import static com.microsoft.skype.teams.util.PermissionUtil.ANDROID_PERMISSION_RECORD_AUDIO;

public class TeamsFragment extends Fragment {

    private MeetingUIClient meetingUIClient;
    private MeetingUIClientCall meetingUIClientCall;
    private TextView statusLabel;
    private SharedPreferences mPrefs;
    private static int REQUEST_MICROPHONE = 202;
    private Button joinMeetingButton;
    private Button joinGroupCallButton;
    private Button endMeetingButton;
    public static final int TAB_POSITION = 0;

    public static Fragment newInstance() {
        return new TeamsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_teams, container, false);

        mPrefs = this.getActivity().getSharedPreferences(getString(R.string.sdk_shared_pref), MODE_PRIVATE);

        joinMeetingButton = root.findViewById(R.id.join_meeting);
        joinMeetingButton.setOnClickListener(l -> {
            if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), ANDROID_PERMISSION_RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
            } else {
                joinMeeting();
            }
        });

        joinGroupCallButton = root.findViewById(R.id.join_groupCall);
        joinGroupCallButton.setOnClickListener(l -> {
            if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), ANDROID_PERMISSION_RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
            } else {
                joinGroupCall();
            }
        });

        endMeetingButton = root.findViewById(R.id.end_meeting);
        endMeetingButton.setOnClickListener(l -> endMeeting());

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MICROPHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                joinMeeting();
            } else {
                SystemUtil.showToast(this.getActivity(), getString(com.microsoft.teamssdk.R.string.permission_deny_msg_join_meeting));
            }
        }
    }

    public MeetingUIClientCall getMeetingUIClientCall() {
        return meetingUIClientCall;
    }

    private void setupViews() {
        boolean isACSInitialized = mPrefs.getBoolean("isACSInitialized", false);
        joinMeetingButton.setEnabled(!isACSInitialized);
        joinGroupCallButton.setEnabled(!isACSInitialized);
        endMeetingButton.setEnabled(!isACSInitialized);
    }

    private void createMeetingUIClient() {
        try {
            updateStatusLabel(R.string.sdk_initializing);
            final String access_token = mPrefs.getString(getString(R.string.access_token), "");
            final String USER_ACCESS_TOKEN = (access_token != null && access_token.isEmpty()) ? "<USER_ACCESS_TOKEN>" : access_token;
            CommunicationTokenRefreshOptions refreshOptions = new CommunicationTokenRefreshOptions(tokenRefresher, true, USER_ACCESS_TOKEN);
            CommunicationTokenCredential credential = new CommunicationTokenCredential(refreshOptions);
            meetingUIClient = new MeetingUIClient(credential);
            meetingUIClient.setIconConfig(getIconConfig());
            mPrefs.edit().putBoolean("isTeamsSDKInitialized", true).apply();
            updateStatusLabel(R.string.sdk_initialized);
        } catch (Exception ex) {
            updateStatusLabel(R.string.sdk_initilialize_failed);
            mPrefs.edit().putBoolean("isTeamsSDKInitialized", false).apply();
            Toast.makeText(getContext(), "Failed to create meeting client: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void joinMeeting() {
        try {
            createMeetingUIClient();

            final String meetingLink = mPrefs.getString(getString(R.string.meeting_url), "");
            final String meetingUrl = (meetingLink!= null && meetingLink.isEmpty()) ? "<MEETING_URL>" : meetingLink;
            final String displayName = "John Smith";

            CustomEventListener customEventListener = new CustomEventListener(this);

            MeetingUIClientTeamsMeetingLinkLocator meetingUIClientTeamsMeetingLinkLocator = new MeetingUIClientTeamsMeetingLinkLocator(meetingUrl);
            MeetingUIClientJoinOptions meetingJoinOptions = new MeetingUIClientJoinOptions(displayName, false);

            meetingUIClientCall = meetingUIClient.join(meetingUIClientTeamsMeetingLinkLocator, meetingJoinOptions);
            meetingUIClientCall.setMeetingUIClientCallEventListener(customEventListener);
            meetingUIClientCall.setMeetingUIClientCallIdentityProvider(new CustomIdentityProvider(getContext()));
            meetingUIClientCall.setMeetingUIClientCallUserEventListener(customEventListener);

            updateStatusLabel(R.string.sdk_call_connecting);
        } catch (Exception ex) {
            updateStatusLabel(R.string.sdk_call_initialize_fail);
            Toast.makeText(getContext(), "Failed to join meeting: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void endMeeting() {
        try {
            meetingUIClientCall.hangUp();
            updateStatusLabel(R.string.hangup_successful);
        } catch (Exception ex) {
            updateStatusLabel(R.string.hangup_failed);
            Toast.makeText(getContext(), "Failed to end meeting: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            teardownTeamsSdk();
        }
    }

    private void teardownTeamsSdk() {
        try {
            if (meetingUIClient != null) {
                meetingUIClient.dispose();
                meetingUIClientCall = null;
                meetingUIClient = null;
                updateStatusLabel(R.string.sdk_disposed);
                mPrefs.edit().putBoolean("isTeamsSDKInitialized", false).apply();
            }
        } catch (Exception ex) {
            updateStatusLabel(R.string.sdk_dispose_failed);
            Toast.makeText(getContext(), "Failed to teardown Teams Sdk: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void joinGroupCall() {
        try {
            createMeetingUIClient();

            final String groupIdStr = mPrefs.getString(getString(R.string.group_Id), "");
            final String groupId = (groupIdStr != null && groupIdStr.isEmpty()) ? "<GROUP_ID>" : groupIdStr;
            final String displayName = "John Smith";

            UUID groupUUID = UUID.fromString(groupId);
            final boolean showStagingScreen = mPrefs.getBoolean(getString(R.string.stagingScreen_enabled), false);
            MeetingUIClientJoinOptions meetingJoinOptions = new MeetingUIClientJoinOptions(displayName, showStagingScreen);
            MeetingUIClientGroupCallLocator meetingUIClientGroupCallLocator = new MeetingUIClientGroupCallLocator(groupUUID);

            CustomEventListener customEventListener = new CustomEventListener(this);

            meetingUIClientCall = meetingUIClient.join(meetingUIClientGroupCallLocator, meetingJoinOptions);

            meetingUIClientCall.setMeetingUIClientCallEventListener(customEventListener);
            meetingUIClientCall.setMeetingUIClientCallIdentityProvider(new CustomIdentityProvider(getContext()));
            meetingUIClientCall.setMeetingUIClientCallUserEventListener(customEventListener);

            updateStatusLabel(R.string.sdk_call_connecting);
        } catch (Exception ex) {
            updateStatusLabel(R.string.sdk_call_initialize_fail);
            Toast.makeText(getContext(), "Failed to join group call: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    Callable<String> tokenRefresher = () -> {
        return fetchToken();
    };

    String fetchToken() {
        // Get token
        return "";
    }

    private Map<MeetingUIClientIconType, Integer> getIconConfig() {
        Map<MeetingUIClientIconType, Integer> iconConfig = new HashMap<>();
        iconConfig.put(MeetingUIClientIconType.VIDEO_OFF, R.drawable.video_camera_off);
        iconConfig.put(MeetingUIClientIconType.VIDEO_ON, R.drawable.video_camera);
        iconConfig.put(MeetingUIClientIconType.MIC_ON, R.drawable.microphone_fill);
        iconConfig.put(MeetingUIClientIconType.MIC_OFF, R.drawable.microphone_off);
        iconConfig.put(MeetingUIClientIconType.SPEAKER, R.drawable.volume_high);
        return iconConfig;
    }

    public void updateStatusLabel(int status) {
        getActivity().runOnUiThread(() -> statusLabel.setText(status));
    }

}