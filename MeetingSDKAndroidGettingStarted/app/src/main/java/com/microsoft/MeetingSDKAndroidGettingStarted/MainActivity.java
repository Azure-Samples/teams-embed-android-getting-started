package com.microsoft.MeetingSDKAndroidGettingStarted;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.azure.android.communication.common.CommunicationUserCredential;
import com.azure.android.communication.MeetingSDK.MeetingSDK;
import com.azure.android.communication.MeetingSDK.JoinOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String ACS_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwMiIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOjA4YjcyZjk0LTc2ZDgtNDFhNS04YTg3LTYxNmU1YjNlY2IyYV8wMDAwMDAwNi1mMzVhLWIwNWEtOGQzMy0zYjNhMGQwMDA0YTUiLCJzY3AiOjE3OTIsImNzaSI6IjE2MDc3MTgyMTAiLCJpYXQiOjE2MDc3MTgyMTAsImV4cCI6MTYwNzgwNDYxMCwiYWNzU2NvcGUiOiJ2b2lwIiwicmVzb3VyY2VJZCI6IjA4YjcyZjk0LTc2ZDgtNDFhNS04YTg3LTYxNmU1YjNlY2IyYSJ9.y-H_DpfVILUVTJlUx9q9hom9sszirBWYp85act6bi1SM-6TY_DUMhK8ncSNFrNgCZn3N_RxvfvrF9hOzwfJuPa4XQIF5viugMmFijCgbAYxFJ29-Mwxf59ALZqSU6HLAIAEZ6iEKoifwXMHc1HIyJE7_zCHCLlhwl5lplAPdbamKF7C_IlPsl2H_Oz9X-CxD8_459bNkCmQZbTHpy1Fx0S4L2edIzVN9d-KtkTaLjPV0wFSKmmV_nqkh-OFCajOosSOQBB_bqBBOmJuAtF8qaA4P23xWY8LGkDYg33ikO0yEOXk03KXnv9_rOWAta-oofNlmTTZiugW_FNsRZPcnxA";
    private final String meetingUrl = "https://teams.microsoft.com/l/meetup-join/19%3ameeting_OTAxOWZkMjgtYjk5YS00Y2U5LWE2ZTEtNjBkYTNkY2JmZGI0%40thread.v2/0?context=%7b%22Tid%22%3a%2272f988bf-86f1-41af-91ab-2d7cd011db47%22%2c%22Oid%22%3a%226a2237f3-1226-44f7-b215-1092b63bafed%22%7d";
    private final String displayName = "John Smith";

    private JoinOptions joinOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinOptions = new JoinOptions();
        joinOptions.displayName = displayName;

        getAllPermissions();
        createMeetingClient();

        Button joinMeeting = findViewById(R.id.join_meeting);
        joinMeeting.setOnClickListener(l -> joinMeeting());
    }

    private void createMeetingClient() {
        try {
            CommunicationUserCredential credential = new CommunicationUserCredential(ACS_TOKEN);
            MeetingSDK.initialize(credential);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Failed to create meeting client.", Toast.LENGTH_SHORT).show();
        }
    }

    private void joinMeeting() {
        try {
            MeetingSDK.joinMeetingWith(meetingUrl, joinOptions);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to joing meeting.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllPermissions() {
        String[] requiredPermissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        ArrayList<String> permissionsToAskFor = new ArrayList<>();
        for (String permission : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToAskFor.add(permission);
            }
        }
        if (!permissionsToAskFor.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToAskFor.toArray(new String[0]), 1);
        }
    }
}