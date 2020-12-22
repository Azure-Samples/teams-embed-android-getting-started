package com.microsoft.MeetingSDKAndroidGettingStarted;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.azure.android.communication.common.CommunicationUserCredential;
import com.microsoft.teamssdk.calling.MeetingSDK;

public class MainActivity extends AppCompatActivity {

    private final int AUDIO_RECORD_REQUEST_CODE = 1;
    private final String ACS_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEwMiIsInR5cCI6IkpXVCJ9.eyJza3lwZWlkIjoiYWNzOjA4YjcyZjk0LTc2ZDgtNDFhNS04YTg3LTYxNmU1YjNlY2IyYV8wMDAwMDAwNi1mMzVhLWIwNWEtOGQzMy0zYjNhMGQwMDA0YTUiLCJzY3AiOjE3OTIsImNzaSI6IjE2MDc3MTgyMTAiLCJpYXQiOjE2MDc3MTgyMTAsImV4cCI6MTYwNzgwNDYxMCwiYWNzU2NvcGUiOiJ2b2lwIiwicmVzb3VyY2VJZCI6IjA4YjcyZjk0LTc2ZDgtNDFhNS04YTg3LTYxNmU1YjNlY2IyYSJ9.y-H_DpfVILUVTJlUx9q9hom9sszirBWYp85act6bi1SM-6TY_DUMhK8ncSNFrNgCZn3N_RxvfvrF9hOzwfJuPa4XQIF5viugMmFijCgbAYxFJ29-Mwxf59ALZqSU6HLAIAEZ6iEKoifwXMHc1HIyJE7_zCHCLlhwl5lplAPdbamKF7C_IlPsl2H_Oz9X-CxD8_459bNkCmQZbTHpy1Fx0S4L2edIzVN9d-KtkTaLjPV0wFSKmmV_nqkh-OFCajOosSOQBB_bqBBOmJuAtF8qaA4P23xWY8LGkDYg33ikO0yEOXk03KXnv9_rOWAta-oofNlmTTZiugW_FNsRZPcnxA";

    private CommunicationUserCredential communicationUserCredential = new CommunicationUserCredential(ACS_TOKEN);
    private MeetingSDK.JoinOptions joinOptions = new MeetingSDK().new JoinOptions();

    private String meetingUrl = "https://teams.microsoft.com/l/meetup-join/19%3ameeting_ZTA1YTBjOTMtNjFmMi00MzZiLThjMGUtMzYxZmUyZTJmOTZk%40thread.v2/0?context=%7b%22Tid%22%3a%2272f988bf-86f1-41af-91ab-2d7cd011db47%22%2c%22Oid%22%3a%226ab9fe04-8ebd-4fe9-a2ed-70c449c924fa%22%7d";
    private String displayName = "John Smith";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isRecordAudioPermissionGranted())
        {
            Toast.makeText(getApplicationContext(), "Need to request permission", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "No need to request permission", Toast.LENGTH_SHORT).show();
        }

        joinOptions.displayName = displayName;
        MeetingSDK.initialize(communicationUserCredential, joinOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            MeetingSDK.joinMeetingWith(meetingUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isRecordAudioPermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) {
                // put your code for Version>=Marshmallow
                return true;
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this,
                            "App required access to audio", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO
                }, AUDIO_RECORD_REQUEST_CODE);
                return false;
            }

        } else {
            // put your code for Version < Marshmallow
            return true;
        }
    }
}