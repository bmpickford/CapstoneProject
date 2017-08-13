package com.app.capstone.app;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by root on 13/08/17.
 */

public class NetworkRunner extends AsyncTask<Object, Object, String> {

    private Exception exception;
    OkHttpClient client = new OkHttpClient();


    protected String run(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return null;
    }


    protected void onPostExecute(String feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }

    @Override
    protected String doInBackground(Object... url) {
        try {
            Request request = new Request.Builder()
                    .url(String.valueOf(url))
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return null;

    }
}
