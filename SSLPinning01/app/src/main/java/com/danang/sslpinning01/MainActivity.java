package com.danang.sslpinning01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danang.sslpinning01.connection.PinnedSSLContextFactory;
import com.danang.sslpinning01.connection.TestConnectionTask;

import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity implements TestConnectionTask.Listener {

    private Button btn;
    private TextView txtResponse;
    private final static String URL = "https://purpleteam.id";
    private final static String TAG = "sslpinning";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        txtResponse = (TextView) findViewById(R.id.response);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    private void onSubmit() {

        txtResponse.setText("Loading...");

        SSLContext sslContext = null;
        try {
            sslContext = getPinnedSSLContext();
        } catch (IOException e) {
            Log.e(TAG, "Failed create pinned SSL context", e);
        }
        new TestConnectionTask(sslContext, this).execute(URL);
    }

    private SSLContext getPinnedSSLContext() throws IOException {
        InputStream input = null;
        try {
            input = getResources().openRawResource(
                    getResources().getIdentifier("kakdeueoe",
                            "raw", getPackageName()));
            return PinnedSSLContextFactory.getSSLContext(input);
        } finally {
            if (null != input) {
                input.close();
            }
        }
    }

    @Override
    public void onConnectionSuccess(String response) {
        if (response.length() > 0) {
            txtResponse.setText("Received secret data "+Integer.toString(response.length())+" bytes");
        }
    }

    @Override
    public void onConnectionFailure() {
        txtResponse.setText("Failure");
    }
}
