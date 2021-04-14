//----------------------------------------------------------------
// Copyright (c) Microsoft Corporation. All rights reserved.
//
// Module name: MainActivity.java
//----------------------------------------------------------------

package com.microsoft.TeamsEmbedAndroidGettingStarted;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.azure.android.communication.common.CommunicationTokenCredential;
import com.azure.android.communication.common.CommunicationTokenRefreshOptions;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallState;
import com.azure.android.communication.ui.meetings.MeetingUIClientGroupCallLocator;
import com.azure.android.communication.ui.meetings.MeetingUIClientJoinOptions;
import com.azure.android.communication.ui.meetings.MeetingUIClient;
import com.azure.android.communication.ui.meetings.MeetingUIClientEventListener;
import com.azure.android.communication.ui.meetings.MeetingUIClientIdentityProvider;
import com.azure.android.communication.ui.meetings.MeetingUIClientIdentityProviderCallback;
import com.azure.android.communication.ui.meetings.MeetingUIClientTeamsMeetingLinkLocator;
import com.azure.android.communication.ui.meetings.MeetingUIClientUserEventListener;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity implements MeetingUIClientEventListener,
        MeetingUIClientIdentityProvider, MeetingUIClientUserEventListener {

    private final String ACS_TOKEN = "<ACS_TOKEN>";
    private final String meetingUrl = "<MEETING_URL>";
    private final String displayName = "John Smith";
    private final UUID groupId = UUID.fromString("29228d3e-040e-4656-a70e-890ab4e173e5");

    private MeetingUIClient meetingUIClient;
    private MeetingUIClientJoinOptions meetingJoinOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meetingJoinOptions = new MeetingUIClientJoinOptions(displayName, false);
        
        getAllPermissions();
        createMeetingClient();

        Button joinMeeting = findViewById(R.id.join_meeting);
        joinMeeting.setOnClickListener(l -> joinMeeting());

        Button joinGroupCall = findViewById(R.id.join_groupCall);
        joinGroupCall.setOnClickListener(l -> joinGroupCall());
    }

    private void createMeetingClient() {
        try {
            CommunicationTokenRefreshOptions refreshOptions = new CommunicationTokenRefreshOptions(tokenRefresher, true, ACS_TOKEN);
            CommunicationTokenCredential credential = new CommunicationTokenCredential(refreshOptions);
            meetingUIClient = new MeetingUIClient(credential);
            meetingUIClient.setMeetingUIClientEventListener(this);
            meetingUIClient.setMeetingUIClientIdentityProvider(this);
            meetingUIClient.setMeetingUIClientUserEventListener(this);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Failed to create meeting client: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void joinMeeting() {
        try {
            MeetingUIClientTeamsMeetingLinkLocator meetingUIClientTeamsMeetingLinkLocator = new MeetingUIClientTeamsMeetingLinkLocator(meetingUrl);
            meetingUIClient.join(meetingUIClientTeamsMeetingLinkLocator, meetingJoinOptions);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Failed to join meeting: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void joinGroupCall() {
        try {
            MeetingUIClientGroupCallLocator meetingUIClientGroupCallLocator = new MeetingUIClientGroupCallLocator(groupId);
            meetingUIClient.join(meetingUIClientGroupCallLocator, meetingJoinOptions);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Failed to join meeting: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllPermissions() {
        String[] requiredPermissions = new String[]{Manifest.permission.RECORD_AUDIO};
        ArrayList<String> permissionsToAskFor = new ArrayList<>();
        for (String permission : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToAskFor.add(permission);
            }
        }
        if (!permissionsToAskFor.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToAskFor.toArray(new String[0]), 202);
        }
    }

    Callable<String> tokenRefresher = () -> {
        return fetchToken();
    };

    public String fetchToken() {
        // Get token
        return "";
    }

    @Override
    public void onCallStateChanged(MeetingUIClientCallState callState) {
        switch(callState) {
            case CONNECTING:
                System.out.println("Call state changed to 'Connecting'");
                break;
            case CONNECTED:
                System.out.println("Call state changed to 'Connected'");
                break;
            case WAITING_IN_LOBBY:
                System.out.println("Call state changed to 'Waiting in Lobby'");
                break;
            case ENDED:
                System.out.println("Call state changed to 'Ended'");
                break;
        }
    }

    @Override
    public void onRemoteParticipantCountChanged(int newCount) {
        System.out.println("Remote participant count changed: " + newCount);
    }

    @Override
    public void provideAvatarFor(String userIdentifier, MeetingUIClientIdentityProviderCallback meetingIdentityProviderCallback) {
        try {
            System.out.println("MeetingUIClientIdentityProvider.provideAvatarFor called for userIdentifier: " + userIdentifier);
            if (isAnonymousVisitor(userIdentifier)) {
                meetingIdentityProviderCallback.onAvatarAvailable(ContextCompat.getDrawable(this, R.drawable.nodpi_avatar_placeholder_large_pink));
            } else if (isOrgIdUser(userIdentifier)) {
                meetingIdentityProviderCallback.onAvatarAvailable(ContextCompat.getDrawable(this, R.drawable.nodpi_doctor_avatar));
            } else if (isACSUser(userIdentifier)) {
                meetingIdentityProviderCallback.onAvatarAvailable(ContextCompat.getDrawable(this, R.drawable.nodpi_avatar_placeholder_large_green));
            }
        } catch (Exception e) {
            System.out.println("MeetingUIClientIdentityProvider: Exception while provideAvatarFor for userIdentifier: " + userIdentifier + e.getMessage());
        }
    }

    @Override
    public void provideDisplayNameFor(String userIdentifier, MeetingUIClientIdentityProviderCallback meetingUIClientIdentityProviderCallback) {
        if (isACSUser(userIdentifier)) {
            meetingUIClientIdentityProviderCallback.onDisplayNameAvailable("ACS User");
        }
    }

    @Override
    public void provideSubTitleFor(String userIdentifier, MeetingUIClientIdentityProviderCallback meetingUIClientIdentityProviderCallback) {
        if (isACSUser(userIdentifier)) {
            meetingUIClientIdentityProviderCallback.onSubTitleAvailable("ACS Subtitle");
        }
    }

    @Override
    public void onNamePlateOptionsClicked(Activity activity, String userIdentifier) {
        Toast.makeText(getApplicationContext(), "Name plate options clicked:", Toast.LENGTH_SHORT).show();
    }

    private boolean isAnonymousVisitor(String userIdentifier) {
        return userIdentifier.startsWith("8:teamsvisitor:");
    }

    private boolean isOrgIdUser(String userIdentifier) {
        return userIdentifier.startsWith("8:orgid:");
    }

    private boolean isACSUser(String userIdentifier) {
        return userIdentifier.startsWith("8:acs:");
    }
}