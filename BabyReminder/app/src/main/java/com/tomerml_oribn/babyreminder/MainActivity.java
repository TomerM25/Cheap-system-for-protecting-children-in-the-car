package com.tomerml_oribn.babyreminder;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button btnGet;
    private TextView txtResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGet = findViewById(R.id.btnGet);
        txtResponse = findViewById(R.id.txtRes);
    }

    @Override
    public void onClick(final View v)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.ynet.co.il", new AsyncHttpResponseHandler()
        {
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(responseBody != null)
                {
                    txtResponse.setText(new String(responseBody));
                    assert txtResponse != null;
                    txtResponse.setText(new String(responseBody));
                }
                v.setEnabled(true);
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                v.setEnabled(true);
            }
        });
    }
}
