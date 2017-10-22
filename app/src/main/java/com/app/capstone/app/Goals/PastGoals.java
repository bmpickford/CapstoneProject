package com.app.capstone.app.Goals;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.capstone.app.ExpandableListAdapter;
import com.app.capstone.app.ExpandableListAdapterPast;
import com.app.capstone.app.Goal;
import com.app.capstone.app.MainActivity;
import com.app.capstone.app.R;
import com.app.capstone.app.Requester;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PastGoals extends Fragment {
    final String url = "http://www.schemefactory.com:5000/";

    ExpandableListAdapterPast listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int lastExpandedPosition = -1;

    private String id;

    // Initialisation of empty array for all goal object
    private HashMap<Integer, Goal> goalsMap = new HashMap<>();

    private PastGoals.OnFragmentInteractionListener mListener;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getGoals(JSONObject jo) throws JSONException {
        //JSONObject jo = new JSONObject(data);

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

    public void uncompleteGoal(View view) throws IOException, JSONException {
        View parent = (View) view.getParent().getParent().getParent();
        TextView taskTextViewItem = (TextView) parent.findViewById(R.id.lblListItemPast);
        String b = String.valueOf(taskTextViewItem.getText());
        String[] splitStr = b.split("-");
        String idStr = splitStr[0].replace(" ", "");
        int id = Integer.parseInt(idStr);
        final Goal g = goalsMap.get(id);
        g.setCompleted(false);
        //g.update();
        //refreshUI();
    }



    public PastGoals() throws IOException {
        // Required empty public constructor
    }


    public static PastGoals newInstance() throws IOException {
        PastGoals fragment = new PastGoals();
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
        final View view = inflater.inflate(R.layout.fragment_past_goals, container, false);

        /*Bundle item = getArguments();
        if(item != null && item.getString("title") != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String title = item.getString("title");
            int priority = item.getInt("priority");
            int type = item.getInt("type");
            Date d = new Date();
            try {
                d = formatter.parse(item.getString("due_date"));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            Goal g = new Goal(title, priority, type, d, getActivity());
            goalsMap.put(g.getId(), g);
        }*/

        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.pastGoalSpinner);


        String endpoint = "goals/past/" + id;
        String uri;// = url + endpoint;

        uri = "http://ec2-54-202-120-169.us-west-2.compute.amazonaws.com:5000/" + endpoint;

        spinner.setVisibility(View.VISIBLE);

        System.out.println("using url: " + uri);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, uri, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        spinner.setVisibility(View.INVISIBLE);
                        System.out.println(response.toString());
                        try {
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

                        expListView = (ExpandableListView) view.findViewById(R.id.lvExpPast);
                        listDataHeader = new ArrayList<String>();
                        listDataChild = new HashMap<String, List<String>>();

                        prepareListData();

                        listAdapter = new ExpandableListAdapterPast(getActivity(), listDataHeader, listDataChild);

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

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.INVISIBLE);
                        messageBox("Goal server error", error.toString());
                        System.out.println(error);
                    }
                });

        Requester.getInstance(getContext()).addToRequestQueue(jsObjRequest);

   /*     expListView = (ExpandableListView) view.findViewById(R.id.lvExpPast);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        prepareListData();


        listAdapter = new ExpandableListAdapterPast(getActivity(), listDataHeader, listDataChild);

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
*/

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
        if (context instanceof PastGoals.OnFragmentInteractionListener) {
            mListener = (PastGoals.OnFragmentInteractionListener) context;
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
                header = goal.getName();
            } else if(new Date().after(goal.getEnd_date())){
                header = goal.getName();
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
