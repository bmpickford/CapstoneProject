package com.app.capstone.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.color.black;
import static android.R.color.white;

/**
 * Created by benjamin on 27/07/17.
 */

public class Goal {
    String name;
    int priority;
    int type;
    Date end_date;
    Boolean completed = false;
    String url = "http://www.schemefactory.com:5000/";
    int id;
    int i = 1;

    Context context;

    //Constructor for new goals
    public Goal(String name, int priority, int type, Date date, Context context) throws IOException, JSONException {
        this.name = name;
        this.priority = priority;
        this.type = type;
        this.end_date = date;
        this.context = context;
        createGoal();
    }

    //Constructor for already persisted goals
    public Goal(String name,  int priority, int type, Date date, Context context, int id){
        this.name = name;
        this.priority = priority;
        this.type = type;
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


    public Date getEnd_date(){
        return this.end_date;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getEnd_dateStr(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(this.end_date);
    }

    public int getId(){ return this.id; }

    public void setName(String name){
        this.name = name;
    }

    public void setDate(Date date){
        this.end_date = date;
    }

    public void setCompleted(Boolean complete){
        this.completed = complete;
    }

    public void delete() throws IOException, JSONException {
        deleteGoal();
    }

    public void update() throws IOException, JSONException {
        updateGoal();
    }

    private void setGoalId(int id) { this.id = id; }

    public void updateGoal() throws JSONException, IOException {
        JSONObject data = getData();
        sendRequest(((String.format("goal/%s", this.id))), "PUT", data);
    }

    private void createGoal() throws JSONException, IOException {
        JSONObject data = getData();
        sendRequest("goal", "POST", data);
    }

    private void deleteGoal() throws IOException, JSONException {
        sendRequest(((String.format("goal/%s", this.id))), "DELETE", null);
    }

    private JSONObject getData() throws JSONException{
        JSONObject data = new JSONObject();

        data.put("Name", this.name);
        data.put("Priority", Integer.valueOf(this.priority));
        data.put("Goal_Type", Integer.valueOf(this.type));
        data.put("Exp_Date", this.end_date);

        Activity activity = (Activity) context;
        String id = ((MainActivity) activity).getStudentNumber();

        data.put("Student_ID", id);


        if(this.completed){
            data.put("Goal_Status", "1");
        } else{
            data.put("Goal_Status", "0");
        }

        return data;
    }



    private void sendRequest(String endpoint, String method, JSONObject data) throws IOException, JSONException {
        final int req;
        JSONObject jsonData = null;
        String uri = this.url + endpoint;

        switch(method){
            case "POST":
                req = com.android.volley.Request.Method.POST;
                jsonData = data;
                break;
            case "PUT":
                req = com.android.volley.Request.Method.PUT;
                jsonData = data;
                break;
            case "DELETE":
                req = com.android.volley.Request.Method.DELETE;
                break;
            default:
                req = com.android.volley.Request.Method.GET;
                break;
        }

        final String requestBody = jsonData.toString();

        System.out.println("New goal: " + uri);

        StringRequest jsObjRequest = new StringRequest
                (req, uri, new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }

                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return String.format("application/json; charset=utf-8");
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    requestBody, "utf-8");
                            return null;
                        }
                    }
        };

        Requester.getInstance(context).addToRequestQueue(jsObjRequest);
    }

}
