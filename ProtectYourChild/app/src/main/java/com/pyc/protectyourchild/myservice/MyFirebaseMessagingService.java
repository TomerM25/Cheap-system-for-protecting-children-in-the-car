package com.pyc.protectyourchild.myservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;

import com.pyc.protectyourchild.EventHistory;
import com.pyc.protectyourchild.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String CHANNEL_ID = "2";
    private DatabaseReference reference;
    String currentuser,sensor;
    AudioManager am;

    public MyFirebaseMessagingService() {
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
        Map <String, String> data = remoteMessage.getData();

        am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        am.setStreamVolume(AudioManager.STREAM_RING,am.getStreamMaxVolume(AudioManager.STREAM_RING),0);

        if(remoteMessage.getData()!=null)
        {
            currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            long time = remoteMessage.getSentTime();
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            final String dateToStr = format.format(time);
            Map<String,String> alerts = new HashMap<>();
            sensor = data.get("Sensor");
            alerts.put("Alert",sensor+" At: "+dateToStr );
            reference.child(currentuser).push().setValue(alerts);
            //Create and Display Notification

        }
        showNotification(data.get("title"),data.get("text"));

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void showNotification(String title, String text) {

        Intent intent=new Intent(MyFirebaseMessagingService.this, EventHistory.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.hazard);
        Uri defaultNotificationSound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.siren);



        //Create Notification Channel
        createNotificationChannel(defaultNotificationSound);

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
                .setVibrate(new long[]{0, 1000, 100, 100, 100, 100, 100, 1000})
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap)
                        .bigLargeIcon(null))
                .addAction(R.mipmap.ic_launcher,"Go To Events History",pendingIntent)
                .setColor(getResources().getColor(R.color.colorRed))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.
                from(this);
        //Notification ID is unique for each notification you create
        notificationManagerCompat.notify(2,builder.build());


    }
    private void createNotificationChannel(Uri defaultNotificationSound ){

        //Create Notification channel only on API Level 26+
        //NotificationChannel is a new Class and not in a support library
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = "My Channel Name2";
            String description = "My Channel description2";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            am.setStreamVolume(AudioManager.STREAM_RING,am.getStreamMaxVolume(AudioManager.STREAM_RING),0);

            //Register the channel with the system.
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setLightColor(Color.RED);
            channel.enableLights(true);
            channel.enableVibration(true);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .build();
            channel.setSound(defaultNotificationSound,audioAttributes);

            //You cannot change importance or other notification behaviors after this
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
