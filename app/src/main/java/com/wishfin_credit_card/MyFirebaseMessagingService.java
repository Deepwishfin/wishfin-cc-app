package com.wishfin_credit_card;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Bitmap bitmap;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            String imageUri = String.valueOf(Objects.requireNonNull(remoteMessage.getNotification()).getImageUrl());
            if (!imageUri.equalsIgnoreCase("null")) {
                bitmap = getBitmapfromUrl(imageUri);
                sendnotification_image(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), remoteMessage.getNotification().getBody(), bitmap);

            } else {
                sendnotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), remoteMessage.getNotification().getBody());

            }
        } catch (Exception ignored) {

        }


    }

    private void sendnotification(String title, String body) {

        Intent intent = new Intent(getApplicationContext(), Splash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String NOTIFICATION_CHANNEL_ID = "1001";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Wishfin", NotificationManager.IMPORTANCE_DEFAULT);

            //Configure Notification Channel
            notificationChannel.setDescription("Wishfin");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);

            }
        }

        if (notificationManager != null) {
            notificationManager.cancelAll();
            notificationManager.notify(0, notificationBuilder.build());

        }

    }

    private void sendnotification_image(String title, String body, Bitmap bitmap1) {

        Intent intent = new Intent(getApplicationContext(), Splash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String NOTIFICATION_CHANNEL_ID = "1001";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(body)
                .setLargeIcon(bitmap1)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Wishfin", NotificationManager.IMPORTANCE_DEFAULT);

            //Configure Notification Channel
            notificationChannel.setDescription("Wishfin");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);

            }
        }

        if (notificationManager != null) {
            notificationManager.cancelAll();
            notificationManager.notify(0, notificationBuilder.build());

        }

    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}
