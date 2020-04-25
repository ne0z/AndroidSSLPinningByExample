package com.danang.nosslpinning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danang.nosslpinning.connection.ConnectionTask;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    private Button btnSend;
    private TextView txtResponse;
    private OkHttpClient client;
    private static final String URL = "https://purpleteam.id/";
    public static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        btnSend = (Button) findViewById(R.id.btnSend);
        txtResponse = (TextView) findViewById(R.id.txtResponse);

        client = new OkHttpClient();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    txtResponse.setText("Loading...");
                    onSubmit();
                } catch (IOException e) {
                    txtResponse.setText("Failure");
                }
            }
        });
    }

    public void updateUI(String response){
        txtResponse.setText("Total " + Integer.toString(response.length()) + " bytes");
    }

    private void onSubmit() throws IOException {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(MainActivity.this.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        RequestBody body = RequestBody.create("{'code':'"+telephonyManager.getImei()+"'}", mediaType);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        AsyncTask task = new ConnectionTask(this, client).execute(request);
    }
}
