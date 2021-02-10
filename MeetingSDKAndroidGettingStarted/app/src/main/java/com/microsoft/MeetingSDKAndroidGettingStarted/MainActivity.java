package com.microsoft.MeetingSDKAndroidGettingStarted;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.azure.android.communication.common.CommunicationTokenCredential;
import com.azure.android.communication.ui.meetings.CallState;
import com.azure.android.communication.ui.meetings.MeetingEventListener;
import com.azure.android.communication.ui.meetings.MeetingIdentityCallback;
import com.azure.android.communication.ui.meetings.MeetingIdentityProvider;
import com.azure.android.communication.ui.meetings.MeetingTokenProvider;
import com.azure.android.communication.ui.meetings.MeetingUIClient;
import com.azure.android.communication.ui.meetings.MeetingJoinOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String ACS_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwMiIsIng1dCI6IjNNSnZRYzhrWVNLd1hqbEIySmx6NTRQVzNBYyIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOjA4YjcyZjk0LTc2ZDgtNDFhNS04YTg3LTYxNmU1YjNlY2IyYV8wMDAwMDAwOC0yOWEzLThhNmItNmEwYi0zNDNhMGQwMDAzMmYiLCJzY3AiOjE3OTIsImNzaSI6IjE2MTI5MjM5MjIiLCJpYXQiOjE2MTI5MjM5MjIsImV4cCI6MTYxMzAxMDMyMiwiYWNzU2NvcGUiOiJ2b2lwIiwicmVzb3VyY2VJZCI6IjA4YjcyZjk0LTc2ZDgtNDFhNS04YTg3LTYxNmU1YjNlY2IyYSJ9.FU6epcsO30aaJw3oEgR8e99A2bNiVtfQGoVqx8t1563Epaz3WeDsZx4DAnICOtA4N0Kn-l5cU_KHB4htVBFM61algp7F_yEjzcWmZjpmteQwAXUJfDzcCq06za9SwxzU27ZhH-g97DgtnWEwAFayYExpFBBWiu9QsenWwhQM1rfbFkOcpJbxUOLDbrNzQQEE1Cq_nuN6xuzO1tRdpIeelKjMeSIaKK5hvnRkU3fmNdeBF2Uw1sQyxR8Bn0tr1aR3fYth0xYYHqqLQSBtEbwhQeXSetnPCP7Tu_g1HkE6r11OW7XFb_Acoh4LRb12m7Vz7aA1HOOUOkl0eSE2OPiJEA";
    private final String meetingUrl = "https://teams.microsoft.com/l/meetup-join/19%3ameeting_ZTA1YTBjOTMtNjFmMi00MzZiLThjMGUtMzYxZmUyZTJmOTZk%40thread.v2/0?context=%7b%22Tid%22%3a%2272f988bf-86f1-41af-91ab-2d7cd011db47%22%2c%22Oid%22%3a%226ab9fe04-8ebd-4fe9-a2ed-70c449c924fa%22%7d";
    private final String displayName = "John Smith";

    private MeetingUIClient meetingUIClient;
    private MeetingJoinOptions meetingJoinOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meetingJoinOptions = new MeetingJoinOptions(displayName);
        
        getAllPermissions();
        createMeetingClient();

        meetingUIClient.setMeetingEventListener(new MeetingEventListener() {
            @Override
            public void onCallStateChanged(CallState callState) {
                System.out.println("Call state changed: " + callState);
            }

            @Override
            public void onRemoteParticipantCountChanged(int newCount) {
                System.out.println("Remote participant count changed: " + newCount);
            }
        });

        meetingUIClient.setMeetingIdentityProvider(new MeetingIdentityProvider() {
            @Override
            public Drawable onProvideAvatar(String s, MeetingIdentityCallback meetingIdentityCallback) {
                return null;
            }
        });

        meetingUIClient.setMeetingTokenProvider(new MeetingTokenProvider() {
            @Override
            public void onTokenExpired(String s) {
            }
        });

        Button joinMeeting = findViewById(R.id.join_meeting);
        joinMeeting.setOnClickListener(l -> joinMeeting());
    }

    private void createMeetingClient() {
        try {
            CommunicationTokenCredential credential = new CommunicationTokenCredential(ACS_TOKEN);
            meetingUIClient = new MeetingUIClient(credential);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Failed to create meeting client: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void joinMeeting() {
        try {
            meetingUIClient.joinMeeting(meetingUrl, meetingJoinOptions);
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
}