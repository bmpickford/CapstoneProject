package com.app.capstone.app;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
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
import java.text.DateFormat;
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
    Boolean completed = false;
    String url = "http://0.0.0.0/";
    int id;
    int i = 1;

    Context context;

    //Constructor for new goals
    public Goal(String name, String description, Date date, Context context){
        this.name = name;
        this.description = description;
        this.end_date = date;
        this.context = context;
        //createGoal();
    }

    //Constructor for already persisted goals
    public Goal(String name, String description, Date date, Context context, int id){
        this.name = name;
        this.description = description;
        this.end_date = date;
        this.context = context;
        this.id = id;
    }

    public TextView getView(){
        TextView textView = new TextView(context);
        textView.setText(name);
        textView.setShadowLayer(1, 1, 1, black);
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

    public String getEnd_dateStr(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(this.end_date);
    }

    public int getId(){ return this.id; }

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

    public void delete() throws IOException {
        deleteGoal();
    }

    public void update() throws IOException, JSONException {
        updateGoal();
    }

    private void setGoalId(int id) { this.id = id; }

    public void updateGoal() throws JSONException, IOException {
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
