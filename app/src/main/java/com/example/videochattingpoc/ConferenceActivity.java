package com.example.videochattingpoc;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dropbox.core.v2.sharing.UserInfo;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.BroadcastIntentHelper;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class ConferenceActivity extends AppCompatActivity {

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // JitsiMeetUserInfo jitsiMeetUserInfo = new JitsiMeetUserInfo();

        // Initialize default options for Jitsi Meet conferences.
        URL serverURL;
        try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
            serverURL = new URL("https://meet.jit.si");
            // serverURL = new URL("https://ssfapp.innovatorslab.net:8443");

            JitsiMeetConferenceOptions defaultOptions
                    = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .setAudioOnly(true)
                    .setAudioMuted(false)
                    .setVideoMuted(true)
                    .setWelcomePageEnabled(false)
                    .setFeatureFlag("add-people.enabled", false)
                    .setFeatureFlag("calendar.enabled", false)
                    .setFeatureFlag("close-captions.enabled", false)
                    .setFeatureFlag("conference-timer.enabled", false)
                    .setFeatureFlag("chat.enabled", false)
                    .setFeatureFlag("filmstrip.enabled", true)
                    .setFeatureFlag("help.enabled", false)
                    .setFeatureFlag("invite.enabled", false)
                    .setFeatureFlag("kick-out.enabled", false)
                    .setFeatureFlag("live-streaming.enabled", false)
                    .setFeatureFlag("lobby-mode.enabled", false)
                    .setFeatureFlag("meeting-name.enabled", false)
                    .setFeatureFlag("meeting-password.enabled", false)
                    .setFeatureFlag("notifications.enabled", false)
                    .setFeatureFlag("overflow-menu.enabled", true)
                    .setFeatureFlag("raise-hand.enabled", false)
                    .setFeatureFlag("recording.enabled", false)
                    .setFeatureFlag("security-options.enabled", false)
                    .setFeatureFlag("tile-view.enabled", false)
                    .setFeatureFlag("toolbox.alwaysVisible", true)
                    .setFeatureFlag("toolbox.enabled", true)
                    .setFeatureFlag("video-share.enabled", false)
                    .build();

            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }

        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setRoom("SampleJitsiAppRoom101")
                .build();

        JitsiMeetActivity.launch(this, options);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }

    private void registerForBroadcastMessages() {
        IntentFilter intentFilter = new IntentFilter();

        /*
            This registers for every possible event sent from JitsiMeetSDK
            If only some of the events are needed, the for loop can be replaced
            with individual statements:
            ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.getAction());
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
                ... other events
        */
        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.getAction());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void onBroadcastReceived(Intent intent) {
        if (intent != null) {
            BroadcastEvent event = new BroadcastEvent(intent);

            switch (event.getType()) {
                case CONFERENCE_JOINED:
                    Log.i(ConferenceActivity.class.getCanonicalName(), "Conference joined");
                    break;
                case PARTICIPANT_JOINED:
                    /*
                        PARTICIPANT_JOINED
                        Broadcasted when a participant has joined the conference.
                        The data HashMap contains information of the participant that has joined.
                        Depending of whether the participant is the local one or not, some of them are present/missing. isLocal email name participantId
                    */
                    Log.i(ConferenceActivity.class.getCanonicalName(), "Participant joined");
                    break;
                case PARTICIPANT_LEFT:
                    /*
                        PARTICIPANT_LEFT
                        Broadcasted when a participant has joined the conference.
                        The data HashMap contains information of the participant that has left.
                        Depending of whether the participant is the local one or not, some of them are present/missing. isLocal email name participantId
                    */
                    Log.i(ConferenceActivity.class.getCanonicalName(), "Participant left");
                    break;
            }
        }
    }

    private void hangUp() {
        Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(hangupBroadcastIntent);
    }
}
