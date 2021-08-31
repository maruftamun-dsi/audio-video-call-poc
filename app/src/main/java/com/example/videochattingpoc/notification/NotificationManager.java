package com.example.videochattingpoc.notification;

import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationManager {

    Context context;
    NotificationManagerCompat notificationManagerCompat;

    public NotificationManager(Context context) {
        this.context = context;
        this.notificationManagerCompat = NotificationManagerCompat.from(context);
    }

   
}
