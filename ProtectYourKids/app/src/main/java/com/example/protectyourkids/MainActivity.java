package com.example.protectyourkids;

import androidx.appcompat.app.AppCompatActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.loopj.android.http.*;
import androidx.core.app.NotificationCompat;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity
{
    private static final long START_TIME_IN_MILLIS = 86400000;      //24 hours
    private TextView txtResponse;
    private Button bt;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.loud);
        bt = findViewById(R.id.bt_notification);
        txtResponse = findViewById(R.id.txtResponse);
        startTimer();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                String message = "Child is stuning";
                NotificationCompat.Builder builder= new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.ic_message)
                        .setContentTitle("New Notification")
                        .setContentText(message)
                        .setAutoCancel(true);
                Intent intent = new Intent(MainActivity.this,NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message",message);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0,builder.build());

            }
        });
    }

    private void startTimer()
    {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 3000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                mTimeLeftInMillis = millisUntilFinished;
                newClient();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
    }

    private void newClient()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ynet.co.il", new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                if(responseBody != null){
                    txtResponse.setText(new String(responseBody) + " cm");
                    assert txtResponse != null;
                    txtResponse.setText(new String(responseBody) + " cm");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
}








