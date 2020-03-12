package com.example.whereismycheese;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class GeofenceTransitionsIntentService extends IntentService {
    private static final String TAG = "GeofenceTransitionsIntent";


    public GeofenceTransitionsIntentService() {
        super("Test");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()){
            Log.e(TAG, "onHandleIntent: "+event.getErrorCode());
            return;
        }else{
            Log.i(TAG, "onHandleIntent: "+getGeofenceTransitionDetails(event));
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    createNotification();
                }
            });
        }
    }

    private static String getGeofenceTransitionDetails(GeofencingEvent event) {
        String transitionString =
                GeofenceStatusCodes.getStatusCodeString(event.getGeofenceTransition());
        List triggeringIDs = new ArrayList();
        for (Geofence geofence : event.getTriggeringGeofences()) {
            triggeringIDs.add(geofence.getRequestId());
        }
        return String.format("%s: %s", transitionString, TextUtils.join(", ", triggeringIDs));
    }

    private void createNotification() {
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";
        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel newChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            newChannel.setDescription(Description);
            newChannel.enableVibration(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(newChannel);
            }

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("WhereIsMyCheese")
                    .setContentIntent(contentIntent)
                    .setContentText("You are near some cheez")
                    .setSmallIcon(R.drawable.note_discovered_icon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }

        }
    }
}

