package com.example.protectyourchild.myservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.example.protectyourchild.R;
import com.example.protectyourchild.SecondActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String CHANNEL_ID = "2";

    public MyFirebaseMessagingService() {

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification()!=null)
        {
            //Create and Display Notification
            showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void showNotification(String title, String text) {

        //Create Notification Channel
        createNotificationChannel();
        Intent intent=new Intent(MyFirebaseMessagingService.this, SecondActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.hazard);
        Uri defaultNotificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setLights(Color.RED,500,200)
                .setSound(defaultNotificationSound)
                .setVibrate(new long[]{0,250,250,250})
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                .addAction(R.mipmap.ic_launcher,"Go To Dashboard",pendingIntent)
                .setColor(getResources().getColor(R.color.colorRed))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.
                from(this);
        //Notification ID is unique for each notification you create
        notificationManagerCompat.notify(2,builder.build());
    }
    private void createNotificationChannel(){
        //Create Notification channel only on API Level 26+
        //NotificationChannel is a new Class and not in a support library
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = "My Channel Name2";
            String description = "My Channel description2";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            //Register the channel with the system.
            //You cannot change importance or other notification behaviors after this
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

}
