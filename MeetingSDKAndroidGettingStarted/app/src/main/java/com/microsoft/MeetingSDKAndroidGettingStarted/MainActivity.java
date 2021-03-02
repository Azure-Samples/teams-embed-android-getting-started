//----------------------------------------------------------------
// Copyright (c) Microsoft Corporation. All rights reserved.
//
// Module name: MainActivity.java
//----------------------------------------------------------------

package com.microsoft.MeetingSDKAndroidGettingStarted;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.azure.android.communication.common.CommunicationTokenCredential;
import com.azure.android.communication.ui.meetings.CallState;
import com.azure.android.communication.ui.meetings.MeetingAvatarAvailableCallback;
import com.azure.android.communication.ui.meetings.MeetingEventListener;
import com.azure.android.communication.ui.meetings.MeetingIdentityProvider;
import com.azure.android.communication.ui.meetings.MeetingJoinOptions;
import com.azure.android.communication.ui.meetings.MeetingUIClient;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity implements MeetingEventListener, MeetingIdentityProvider {

    private final String ACS_TOKEN = "<ACS_TOKEN>";
    private final String meetingUrl = "<MEETING_URL>";
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

        Button joinMeeting = findViewById(R.id.join_meeting);
        joinMeeting.setOnClickListener(l -> joinMeeting());
    }

    private void createMeetingClient() {
        try {
            CommunicationTokenCredential credential = new CommunicationTokenCredential(tokenRefresher, true, ACS_TOKEN);
            meetingUIClient = new MeetingUIClient(credential);
            meetingUIClient.setMeetingEventListener(this);
            meetingUIClient.setMeetingIdentityProvider(this);
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

    Callable<String> tokenRefresher = () -> {
        return fetchToken();
    };

    public String fetchToken() {
        // Get token
        return "";
    }

    @Override
    public void onCallStateChanged(CallState callState) {
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
    public void requestForAvatar(String userIdentifier, MeetingAvatarAvailableCallback meetingAvatarAvailableCallback) {
        Drawable myAvatar = null;
        try {
            System.out.println("MicrosoftTeamsSDKIdentityProvider.requestForAvatar called for userIdentifier: " + userIdentifier);
            if (userIdentifier.startsWith("8:teamsvisitor:")) {
                // get and provide avatar picture asynchronously with long fetching/decoding delay.
                System.out.println("update avatar for Anonymous user");
                String imageUrl = "https://mlccdn.blob.core.windows.net/dev/LWA/qingy/doughboy_36x36.png";
                updateAvatarFromUrl(this, imageUrl, meetingAvatarAvailableCallback);
            } else if (userIdentifier.startsWith("8:orgid:")) {
                System.out.println("update avatar for OrgID user");
                String imageUrl = "https://mlccdn.blob.core.windows.net/dev/LWA/qingy/qingy_120.jpg";
                updateAvatarFromUrl(this, imageUrl, meetingAvatarAvailableCallback);
            } else if (userIdentifier.startsWith("8:acs:")) {
                System.out.println("update avatar for ACS user");
                String imageUrl = "https://mlccdn.blob.core.windows.net/dev/LWA/qingy/msudan.png";
                updateAvatarFromUrl(this, imageUrl, meetingAvatarAvailableCallback);
            }
        } catch (Exception e) {
            System.out.println("MicrosoftTeamsSDKIdentityProvider: Exception while requestForAvatar for userIdentifier: " + userIdentifier + e.getMessage());
        }
    }

    /**
     * download and callback to set the Avatar image from web URL.
     * We have a few sample images on CDN
     *                 String imageUrl = "https://mlccdn.blob.core.windows.net/dev/LWA/qingy/doughboy_36x36.png";
     *                 String imageUrl = "https://mlccdn.blob.core.windows.net/dev/LWA/qingy/qingy_120.jpg";
     *                 String imageUrl = "https://mlccdn.blob.core.windows.net/dev/LWA/qingy/msudan.png";
     */
    private void updateAvatarFromUrl (Context context, String url, MeetingAvatarAvailableCallback observer) {
        String urlImage = url;
        WeakReference<MeetingAvatarAvailableCallback> weakReferenceObserver = new WeakReference<MeetingAvatarAvailableCallback>(observer);
        new AsyncTask<String, Integer, Drawable>(){
            @Override
            protected Drawable doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(urlImage).openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                return d;
            }
            protected void onPostExecute(Drawable myAvatar) {
                // provide avatar when it is available.
                MeetingAvatarAvailableCallback callback = weakReferenceObserver.get();
                if (callback != null) {
                    System.out.println("invoke the callback method with fetched avatar");
                    callback.onAvatarAvailable(myAvatar);
                } else {
                    System.out.println("callback observer are reclaimed, there is no need to update avatar in UI anymore");
                }
            }
        }.execute();
    }
}