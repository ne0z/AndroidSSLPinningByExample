package com.danang.nosslpinning.connection;

import android.os.AsyncTask;

import com.danang.nosslpinning.MainActivity;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectionTask  extends AsyncTask<Request, Void, String> {


    public static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    private MainActivity mActivity;

    public ConnectionTask(MainActivity mActivity , OkHttpClient client) {
        this.client = client;
        this.mActivity = mActivity;
    }

    @Override
    protected String doInBackground(Request... request) {
        try {
            Response response = client.newCall(request[0]).execute();
            return response.body().string();
        } catch (IOException io) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mActivity.updateUI(result);
    }
}