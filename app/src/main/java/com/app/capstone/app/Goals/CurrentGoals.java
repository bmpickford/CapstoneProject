package com.app.capstone.app.Goals;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.app.capstone.app.ExpandableListAdapter;
import com.app.capstone.app.Goal;
import com.app.capstone.app.GoalsPage;
import com.app.capstone.app.MainActivity;
import com.app.capstone.app.NewGoalPage;
import com.app.capstone.app.R;
import com.app.capstone.app.Requester;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.fragment;
import static android.R.attr.type;
import static com.app.capstone.app.R.layout.fragment_current_goals;


public class CurrentGoals extends Fragment {

    private String d = null;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int lastExpandedPosition = -1;
    private String id;


    final String url = "http://www.schemefactory.com:5000/";


    private HashMap<Integer, Goal> goalsMap;// = getGoals();
    private OnFragmentInteractionListener mListener;



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getGoals(JSONObject jo) throws JSONException {
        Iterator<String> it  =  jo.keys();

        HashMap<String, ArrayList> map = new HashMap<>();
        ArrayList<String> values = null;

        while( it.hasNext() ){
            String key = it.next();
            Object value = jo.get(key);
            JSONObject innerJo = new JSONObject(value.toString());

            Iterator<String> innerit  =  innerJo.keys();

            values = new ArrayList<String>();

            while( innerit.hasNext() ){
                String k = innerit.next();
                Object v = innerJo.get(k);
                values.add(v.toString());
            }

            map.put(key, values);
        }

        for(int i = 0; i < values.size(); i++){
            int id = -1;
            int type = -1;
            int priority = 1;
            String name = null;
            Date d = null;
            for (Map.Entry<String, ArrayList> entry : map.entrySet()) {
                String key = entry.getKey();
                ArrayList value = entry.getValue();
                String v = value.get(i).toString();


                switch(key){
                    case "Exp_Date":
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        try {
                            d = formatter.parse(v);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Goal_ID":
                        id = Integer.parseInt(v);
                        break;
                    case "Description":
                        name = v;
                        break;
                    case "Goal_Type":
                        if(v == "Personal"){
                            type = 1;
                        } else if (v == "Career"){
                            type = 2;
                        } else {
                            type = 3;
                        }
                }
            }
            System.out.println("NAME: " + name);
            Goal g = new Goal(name, priority, type, d, getActivity(), id);
            goalsMap.put(g.getId(), g);
        }
    }


    public CurrentGoals() throws IOException {
        // Required empty public constructor
    }

    public void deleteGoal(final View view) throws IOException {
        View parent = (View) view.getParent().getParent().getParent();
        TextView taskTextViewItem = (TextView) parent.findViewById(R.id.lblListItem);
        String b = String.valueOf(taskTextViewItem.getText());
        String[] splitStr = b.split("-");
        String idStr = splitStr[0].replace(" ", "");
        final int id2 = Integer.parseInt(idStr);
        //final Goal g = goalsMap.get(id);


        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(view.getContext());
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Deleting");
                        /*try {
                            //update(id2, "DELETE", view);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();

    }

    public void updateGoal(View view) throws IOException, JSONException {

    }



    public static CurrentGoals newInstance() throws IOException {
        CurrentGoals fragment = new CurrentGoals();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        id = ((MainActivity)getActivity()).getStudentNumber();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(fragment_current_goals, container, false);

        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.currentGoalSpinner);
        final TextView err = (TextView) view.findViewById(R.id.errTextCurrGoal);

        spinner.setVisibility(View.VISIBLE);

        String endpoint = "goals/present/" + id;
        String uri;// = url + endpoint;

        goalsMap = new HashMap<>();


        uri = "http://ec2-54-202-120-169.us-west-2.compute.amazonaws.com:5000/" + endpoint;


        System.out.println("using url: " + uri);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, uri, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        spinner.setVisibility(View.INVISIBLE);
                        try {
                            //getGoals(response);
                            for(int i = 0; i < response.getJSONArray("data").length(); i++){
                                JSONObject ja = response.getJSONArray("data").getJSONObject(i);
                                String name;
                                int priority;
                                int type;
                                int goal_id;
                                Date date = null;

                                name = ja.getString("name");
                                priority = Integer.parseInt(ja.getString("priority"));
                                type = Integer.parseInt(ja.getString("type"));
                                goal_id = ja.getInt("id");

                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    date = formatter.parse(ja.getString("expiry"));
                                } catch (java.text.ParseException e) {
                                    try {
                                        date = formatter2.parse(ja.getString("expiry"));
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                }

                                Goal g = new Goal(name, priority, type, date, getActivity(), goal_id);
                                goalsMap.put(goal_id, g);
                            }
                        } catch (JSONException e) {
                            messageBox("Formatting goal data", e.toString());
                            e.printStackTrace();
                        }

                        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
                        listDataHeader = new ArrayList<String>();
                        listDataChild = new HashMap<String, List<String>>();

                        prepareListData();

                        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

                        // setting list adapter
                        expListView.setAdapter(listAdapter);

                        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                            @Override
                            public void onGroupExpand(int groupPosition) {
                                if (lastExpandedPosition != -1
                                        && groupPosition != lastExpandedPosition) {
                                    expListView.collapseGroup(lastExpandedPosition);
                                }
                                lastExpandedPosition = groupPosition;
                            }
                        });

                        LayoutInflater infalInflater = (LayoutInflater) getActivity()
                                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View convertView = infalInflater.inflate(R.layout.list_item, null);


                        Button btn = (Button) convertView.findViewById(R.id.completeGoalBtn);

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("BUTTTON 2222");
                            }
                        });

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        spinner.setVisibility(View.INVISIBLE);
                        messageBox("Goal Server Error", error.toString());
                    }
                });

        Requester.getInstance(getContext()).addToRequestQueue(jsObjRequest);





        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void prepareListData() {

        int i = 0;

        Iterator it = goalsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<String> s = new ArrayList<String>();
            String header;
            Goal goal = (Goal) pair.getValue();

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(new Date());
            cal2.setTime(goal.getEnd_date());

            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if(sameDay){
                header = goal.getName() + "  - DUE NOW";
            } else if(new Date().after(goal.getEnd_date())){
                header = goal.getName() + "  - OVERDUE";
            } else {
                header = goal.getName();
            }

            listDataHeader.add(header);
            s.add(goal.getId() + " - DUE: " + goal.getEnd_dateStr());
            listDataChild.put(listDataHeader.get(i), s);
            i++;


            it.remove(); // avoids a ConcurrentModificationException
        }


    }

    private void messageBox(String method, String message)
    {
        Log.d("EXCEPTION: " + method,  message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(getContext());
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }



}
