package com.app.capstone.app.Course;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.capstone.app.ExpandableListAdapter;
import com.app.capstone.app.ExpandableListAdapterUnits;
import com.app.capstone.app.Goal;
import com.app.capstone.app.MainActivity;
import com.app.capstone.app.R;
import com.app.capstone.app.Requester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CourseUnits extends Fragment {

    private OnFragmentInteractionListener mListener;
    final String url = "http://www.schemefactory.com:5000/";
    ExpandableListAdapterUnits listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int lastExpandedPosition = -1;
    private String id;


    private HashMap<Integer, List> unitsMap = new HashMap<>();

    public CourseUnits() {
        // Required empty public constructor
    }

    public static CourseUnits newInstance(String param1, String param2) {
        CourseUnits fragment = new CourseUnits();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String[] getUnits(JSONObject jo) throws JSONException {
        Iterator<String> it = jo.keys();

        HashMap<String, ArrayList> map = new HashMap<>();
        ArrayList<String> values = null;

/*        int unit_id = 1;
        String grade = "72%";
        String code = "CAB201";
        String name = "Programming Principles";
        String date = "31/10/2016";

        List<String> unitData = new ArrayList<String>();
        unitData.add(grade);
        unitData.add(code);
        unitData.add(name);
        unitData.add(date);

        List<String> unitData2 = new ArrayList<String>();
        unitData2.add("58%");
        unitData2.add("CAB202");
        unitData2.add("Embedded Systems");
        unitData2.add("31/10/2016");
        //Goal g = new Goal(name, priority, type, d, getActivity(), id);
        unitsMap.put(unit_id, unitData);*/

        while (it.hasNext()) {
            String key = it.next();
            Object value = jo.get(key);
            JSONObject innerJo = new JSONObject(value.toString());

            Iterator<String> innerit = innerJo.keys();

            values = new ArrayList<String>();

            while (innerit.hasNext()) {
                String k = innerit.next();
                Object v = innerJo.get(k);
                values.add(v.toString());
            }

            map.put(key, values);

        }

        for (int i = 0; i < values.size(); i++) {
            String grade = null;
            String code = null;
            String name = null;
            Date d = null;
            int unit_id = -1;
            for (Map.Entry<String, ArrayList> entry : map.entrySet()) {
                String key = entry.getKey();
                ArrayList value = entry.getValue();
                String v = value.get(i).toString();


                switch (key) {
                    case "Date_Completed":
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        try {
                            d = formatter.parse(v);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "grade":
                        grade = v;
                        break;
                    case "name":
                        name = v;
                        break;
                    case "code":
                        code = v;
                        break;
                    case "id":
                        unit_id = Integer.parseInt(v);
                        break;

                }
            }
            System.out.println("NAME: " + name);
            List<String> unitData = new ArrayList<String>();
            unitData.add(grade);
            unitData.add(code);
            unitData.add(name);
            unitData.add(d.toString());
            //Goal g = new Goal(name, priority, type, d, getActivity(), id);
            unitsMap.put(unit_id, unitData);
        }


        String[] s = {"CAB201"};
        return s;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        id = ((MainActivity)getActivity()).getStudentNumber();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_course_units, container, false);

        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.units_spinner);
        final LinearLayout unitsBtns = (LinearLayout) view.findViewById(R.id.units_group);

        spinner.setVisibility(View.VISIBLE);

        String endpoint = "units/" + id;

        String uri;

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getUnits";
        } else {
            uri = url + endpoint;
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String[] units = {};
                        try {
                            JSONObject jo = response;
                            //units = getUnits(jo);

                            JSONArray u = jo.getJSONArray("code");

                            String[] arr=new String[u.length()];
                            for(int i=0; i<arr.length; i++) {
                                System.out.println(u.optString(i));
                                System.out.println(u.get(i));
                                arr[i]=u.optString(i);
                            }
                            System.out.println(arr);

                            units = arr;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            messageBox("Unit data", e.toString());
                        }
                        spinner.setVisibility(View.INVISIBLE);

                        //String[] u = {"CAB201: Programming Principles", "CAB202: Embedded Systems",
                          //      "IFB299: Application Design and Development", "CAB301: Software Development"};

                        for(int i = 0; i < units.length; i++){
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );

                            params.setMargins(0, 30, 0, 0);
                            Button b = new Button(getActivity());

                            b.setLayoutParams(params);

                            b.setText(units[i]);
                            b.setId(i);

                            b.setBackgroundColor(Color.WHITE);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int unit_id = view.getId();
                                    System.out.println(unit_id);
                                    Bundle b = new Bundle();
                                    b.putInt("id", unit_id);
                                    Fragment fragment = null;
                                    Class fragmentClass = UnitDetailPage.class;
                                    try {
                                        fragment = (Fragment) fragmentClass.newInstance();
                                        fragment.setArguments(b);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.courseContent, fragment).commit();
                                    } catch (Exception e) {
                                        System.out.println(e);
                                        e.printStackTrace();
                                    }
                                }
                            });
                            unitsBtns.addView(b);
                            //b.getLayoutParams().height = 80;
                        }

                        /*expListView = (ExpandableListView) view.findViewById(R.id.lvExpUnits);
                        listDataHeader = new ArrayList<String>();
                        listDataChild = new HashMap<String, List<String>>();

                        prepareListData();

                        listAdapter = new ExpandableListAdapterUnits(getActivity(), listDataHeader, listDataChild);

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
                        });*/
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.INVISIBLE);
                        System.out.println("Units volley error: " + error);
                        messageBox("Unit data", error.toString());
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

    private void prepareListData() {

        int i = 0;

        int unit_id;
        String grade;
        String code;
        String name;
        String date;

        Iterator it = unitsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<String> s = new ArrayList<String>();
            String header;
            ArrayList<String> unit = (ArrayList<String>) pair.getValue();

            grade = unit.get(0);
            code = unit.get(1);
            name = unit.get(2);
            date = unit.get(3);

            Calendar cal1 = Calendar.getInstance();

            Date d = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            try {
                d = formatter.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal1.setTime(d);

            header = name + " - " + code;

            listDataHeader.add(header);
            s.add("Grade: " + grade + ", completed: " + date);
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
