package com.example.ori.httprequest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.*;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnGet = findViewById(R.id.btnGet);
        btnGet.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(responseBody!=null){
                   TextView txtResponse =(TextView) findViewById(R.id.txtResponse);
                    txtResponse.setText(new String(responseBody));
                    assert txtResponse !=null;
                    txtResponse.setText(new String(responseBody));
                }
                v.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                v.setEnabled(true);
            }
        });



    }
}
