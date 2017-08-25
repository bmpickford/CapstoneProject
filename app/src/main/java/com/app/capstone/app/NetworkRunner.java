package com.app.capstone.app;

import android.content.Context;
import android.os.AsyncTask;

import com.app.capstone.app.Course.CourseGPA;
import com.app.capstone.app.Goals.CurrentGoals;
import com.app.capstone.app.Goals.PastGoals;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by benjamin on 13/08/17.
 */

public class NetworkRunner extends AsyncTask<String, Void, String> {

    private Exception exception;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //private ;// = new CurrentGoals();
    private String url;
    private String method;
    private String context;
    private String body;

    public NetworkRunner(String url, String method, String context, String body) throws IOException {
        this.url = url;
        this.method = method;
        this.context = context;
        this.body = body;
    }


    @Override
    protected String doInBackground(String... url) {
        System.out.println("URL: " + url.toString());
        System.out.println(this.url);

        switch (this.method){
            case "GET":
                try {
                    Request request = new Request.Builder()
                            //.url(String.valueOf(url))
                            .url(this.url)
                            .build();

                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case "POST":
                try {
                    RequestBody body = RequestBody.create(JSON, this.body);
                    Request request = new Request.Builder()
                            .url(this.url)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case "PUT":
                try {
                    RequestBody body = RequestBody.create(JSON, this.body);
                    Request request = new Request.Builder()
                            .url(this.url)
                            .put(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            break;
            case "DELETE":
                try {
                    Request request = new Request.Builder()
                            //.url(String.valueOf(url))
                            .url(this.url)
                            .delete()
                            .build();


                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            default:
                return null;
        }


        return null;

    }

    protected void onPostExecute(String res) {
        // TODO: check this.exception
        switch (this.context){
            case "CurrentGoals":
                CurrentGoals cg = null;
                try {
                    cg = new CurrentGoals();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cg.getGoals(res);
                break;
            case "PastGoals":
                PastGoals pg = null;
                try {
                    pg = new PastGoals();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pg.getGoals(res);
                break;
            case "CourseGPA":
                CourseGPA gpa = new CourseGPA();
                gpa.getGPA(res);
                break;
        }
        return;
    }

}
