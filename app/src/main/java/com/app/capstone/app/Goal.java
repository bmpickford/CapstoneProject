package com.app.capstone.app;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static android.R.color.black;
import static android.R.color.white;

/**
 * Created by benjamin on 27/07/17.
 */

public class Goal {
    String name;
    String description;
    Date end_date;
    Boolean completed;
    String url = "http://0.0.0.0/";
    int id;

    Context context;


    public Goal(String name, String description, Date date, Context context){
        this.name = name;
        this.description = description;
        this.end_date = date;
        this.context = context;
        System.out.println("Goal contructor");
    }

    public TextView getView(){
        System.out.println("Getting view");
        TextView textView = new TextView(context);
        textView.setText(name);
        textView.setShadowLayer(1, 1, 1, black);
        System.out.println("returning view");
        return textView;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public Date getEnd_date(){
        return this.end_date;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    public void setDate(Date date){
        this.end_date = date;
    }

    public void setCompleted(Boolean complete){
        this.completed = complete;
    }

    private void updateAPI() throws JSONException, IOException {
        String data = getData();
        sendRequest(((String.format("/goal/%s", this.id))), "PUT", data);
    }

    private void createGoal() throws JSONException, IOException {
        String data = getData();
        sendRequest("/goal", "POST", data);
    }

    private void deleteGoal() throws IOException {
        sendRequest(((String.format("/goal/%s", this.id))), "DELETE", "");
    }

    private String getData() throws JSONException{
        String data = new JSONObject()
                .put("id", this.id)
                .put("name", this.name)
                .put("description", this.description)
                .put("end_date", this.end_date)
                .put("completed", this.completed).toString();
        return data;
    }

    private void sendRequest(String endpoint, String method, String data) throws IOException {
        String u = this.url + endpoint;
        URL url = new URL(u);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod(method.toUpperCase());
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(data.getBytes());

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            //TODO: change this to suit output
            for (int c; (c = in.read()) >= 0;)
                System.out.print((char)c);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

    }
}
