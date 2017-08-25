package com.app.capstone.app;

import android.os.AsyncTask;

import com.app.capstone.app.Goals.CurrentGoals;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by benjamin on 13/08/17.
 */

public class NetworkRunner extends AsyncTask<String, Void, String> {

    private Exception exception;
    OkHttpClient client = new OkHttpClient();
    private CurrentGoals cg;// = new CurrentGoals();

    public NetworkRunner() throws IOException {

    }


    @Override
    protected String doInBackground(String... url) {
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

    protected void onPostExecute(String res) {
        // TODO: check this.exception
        cg.getGoals(res);
    }

}
