package com.danang.sslpinning02;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.danang.sslpinning02.connection.ConnectionTask;

import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://purpleteam.id";
    private Button btnSend;
    private TextView txtResponse;
    private OkHttpClient client;
    private MainActivity mainActivity;
    private Request request;

    public static String getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException exception) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSend = (Button) findViewById(R.id.send);
        txtResponse = (TextView) findViewById(R.id.txtResponse);

        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(getDomainName(URL), getResources().getString(R.string.raw_data_01))
                .add(getDomainName(URL), getResources().getString(R.string.raw_data_02))
                .add(getDomainName(URL), getResources().getString(R.string.raw_data_03))
                .build();

        client = new OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .build();

        request = new Request.Builder()
                .url(URL)
                .build();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResponse.setText("Loading...");
                Request request = new Request.Builder()
                        .url(URL)
                        .build();
                AsyncTask task = new ConnectionTask(MainActivity.this, client).execute(request);
            }
        });
    }


    public void updateResponseText(String response) {
        txtResponse.setText("Received " + response.length() + " bytes");
    }
}
