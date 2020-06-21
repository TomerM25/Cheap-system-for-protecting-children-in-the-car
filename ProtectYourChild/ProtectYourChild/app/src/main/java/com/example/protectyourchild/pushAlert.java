package com.example.protectyourchild;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class pushAlert extends AppCompatActivity {

    EditText cookies;
    Button buy_btn;
    String CHANNEL_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_alert);

        //FirebaseMessaging.getInstance().subscribeToTopic("NEWYORK_WEATHER");



        createNotificationChannel();
        myRegistrationToken();


        cookies = findViewById(R.id.cookies);
        buy_btn = findViewById(R.id.buy_btn);
        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String numberOfCookies = cookies.getText().toString();


                Intent intent=new Intent(pushAlert.this,SecondActivity.class); //need to put alerts activity list
                //intent.putExtra("cookie",numberOfCookies);
                PendingIntent pendingIntent = PendingIntent.getActivity(pushAlert.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.hazard);

                Uri defaultNotificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        pushAlert.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_chat)
                        .setContentTitle("Warning")
                        .setContentText("Drowning danger was identified!")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setLargeIcon(bitmap)
                        .setSound(defaultNotificationSound)
                        .setLights(Color.RED,500,200)
                        .setVibrate(new long[]{0,250,250,250})
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                        .addAction(R.mipmap.ic_launcher,"Go To Dashboard",pendingIntent)
                        .setColor(getResources().getColor(R.color.colorRed))
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.
                        from(pushAlert.this);
                //Notification ID is unique for each notification you create
                notificationManagerCompat.notify(1,builder.build());
            }
        });
    }

    public void createNotificationChannel(){
        //Create Notification channel only on API Level 26+
        //NotificationChannel is a new Class and not in a support library
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = "My Channel Name";
            String description = "My Channel description";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setVibrationPattern(new long[]{0,250,250,250});
            //channel.setDescription(description);
            //Register the channel with the system.
            //You cannot change importance or other notification behaviors after this
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    void myRegistrationToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        //Get new Instance ID token
                        String token = task.getResult().getToken();

                        //Log and toast
                        Toast.makeText(pushAlert.this ,token,Toast.LENGTH_SHORT).show();
                        Log.d("Token", token);
                    }
                });
    }
}