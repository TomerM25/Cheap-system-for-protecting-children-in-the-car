package com.example.ori.httprequest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;
import android.os.CountDownTimer;
import com.loopj.android.http.*;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity //implements View.OnClickListener
{
    private static final long START_TIME_IN_MILLIS = 86400000;      //24 hours
    private TextView txtResponse;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResponse = findViewById(R.id.txtResponse);

        startTimer();
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
        client.get("http://192.168.43.110", new AsyncHttpResponseHandler()
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
