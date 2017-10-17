package com.app.capstone.app.Course;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.capstone.app.Goal;
import com.app.capstone.app.MainActivity;
import com.app.capstone.app.R;
import com.app.capstone.app.Requester;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CourseGPA extends Fragment {
    private PieChart mChart;
    final String url = "http://www.schemefactory.com:5000/";
    String id;


    private OnFragmentInteractionListener mListener;

    public CourseGPA() {
        // Required empty public constructor
    }

    public void getGPA(JSONObject data) throws JSONException {
        System.out.println(data.toString());
        //JSONObject jo = new JSONObject(data);



        Iterator<String> it  =  data.keys();

        while( it.hasNext() ) {
            String key = it.next();
            Object value = data.get(key);

            System.out.println(value.toString());
        }


 /*           Iterator<String> it  =  jo.keys();

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
            }*/

    }


    public static CourseGPA newInstance(String param1, String param2) {
        CourseGPA fragment = new CourseGPA();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        id = ((MainActivity)getActivity()).getStudentNumber();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_course_gpa, container, false);

        final TextView title = (TextView) view.findViewById(R.id.gpaData1);
        final TextView avg = (TextView) view.findViewById(R.id.gpaData2);
        final TextView median = (TextView) view.findViewById(R.id.gpaData3);

        final LinearLayout gpaContent = (LinearLayout) view.findViewById(R.id.gpa_content);
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.gpa_spinner);

        String endpoint = "gpa/" + id;

        String uri;

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getGPA";
        } else {
            uri = url + endpoint;
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        gpaContent.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                        double gpa = 0;

                        try {
                            JSONObject body = response;

                            title.setText(body.getJSONObject("Parent_Study_Package_Full_Title").getString("0"));
                            avg.setText("Course Average: " + body.getJSONObject("Mean").getString("0"));
                            median.setText("Course Median: " + body.getJSONObject("Median").getString("0"));
                            gpa = Double.parseDouble(body.getJSONObject("Course_GPA").getString("0"));
                        } catch (JSONException e) {
                            messageBox("Get GPA Data", e.getMessage());
                            e.printStackTrace();
                        }

                        /*title.setText("Bachelor of Information Technology");
                        avg.setText("Average: 5.2");
                        median.setText("Median: 5.01");*/

                        mChart = (PieChart) view.findViewById(R.id.chart1);
                        mChart.setUsePercentValues(false);
                        mChart.setCenterText("GPA \n" + Double.toString(gpa));
                        //mChart.setCenterTextTypeface();
                        mChart.setCenterTextSize(16);
                        mChart.setCenterTextColor(Color.DKGRAY);
                        mChart.getDescription().setEnabled(false);
                        mChart.setExtraOffsets(5, 10, 5, 5);
                        mChart.setDragDecelerationFrictionCoef(0.95f);
                        mChart.setDrawHoleEnabled(true);
                        mChart.setHoleColor(Color.WHITE);
                        mChart.setTransparentCircleColor(Color.GRAY);
                        mChart.setTransparentCircleAlpha(110);
                        mChart.setHoleRadius(75f);
                        mChart.setTransparentCircleRadius(61f);
                        mChart.setDrawCenterText(true);
                        mChart.setRotationAngle(0);
                        mChart.setRotationEnabled(false);
                        mChart.setHighlightPerTapEnabled(false);
                        //mChart.getLegend().setEnabled(false);
                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                        mChart.setEntryLabelTextSize(0);
                        mChart.setHoleColor(ResourcesCompat.getColor(getResources(), R.color.colorBackground, null));

                        double honours = 5.5;

                        int maxAngle;

                        if(gpa > honours){
                            maxAngle = (int) Math.floor((gpa / 7) * 360);
                        } else {
                            maxAngle = (int) Math.floor((honours / 7) * 360);
                        }

                        mChart.setMaxAngle(maxAngle);

                        setData(gpa);

                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                        mChart.setEntryLabelColor(Color.WHITE);
                        mChart.setEntryLabelTextSize(12f);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                gpaContent.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                title.setText("There was an error: " + error.toString());
                messageBox("Get GPA Data", error.toString());
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


    private void setData(double gpa) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        double honors = 5.5;

        entries.add(new PieEntry((float) gpa, "GPA"));

        if(honors > (gpa + 0.2)){
            entries.add(new PieEntry((float) (honors - gpa), "Honors Level"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);

        data.setHighlightEnabled(false);
        //data.setValueTextSize(11f);
        //data.setValueTextColor(Color.GRAY);
        mChart.setData(data);

        mChart.highlightValues(null);
        mChart.invalidate();
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
