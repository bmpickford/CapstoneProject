package com.app.capstone.app.Course;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
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
   // private PieChart mChart;
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

        PieChart mChart;
        PieChart mChart2;

        final TextView title = (TextView) view.findViewById(R.id.gpaData1);
        final TextView avg = (TextView) view.findViewById(R.id.gpaData2);
        final TextView median = (TextView) view.findViewById(R.id.gpaData3);
        final TextView code = (TextView) view.findViewById(R.id.title);

        final TextView title2 = (TextView) view.findViewById(R.id.gpaData21);
        final TextView avg2 = (TextView) view.findViewById(R.id.gpaData22);
        final TextView median2 = (TextView) view.findViewById(R.id.gpaData23);
        final TextView code2 = (TextView) view.findViewById(R.id.title2);

        final LinearLayout gpaContent = (LinearLayout) view.findViewById(R.id.gpa_content);
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.gpa_spinner);

        String endpoint = "gpa/" + id;

        String uri;

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getGPA";
        } else {
            uri = "http://ec2-54-202-120-169.us-west-2.compute.amazonaws.com:5000/" + endpoint;
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(JSONObject response) {
                        gpaContent.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);

                        PieChart chart1 = (PieChart) view.findViewById(R.id.chart1);
                        PieChart chart2 = (PieChart) view.findViewById(R.id.chart2);

                        double gpa = 0;
                        double gpa2 = 0;

                        double Honours = 5.5;

                        int maxAngle;
                        int maxAngle2;

                        try {
                            JSONObject body = response.getJSONArray("data").getJSONObject(0);

                            if(!body.getString("degree_2").equals("0") && !(body.getString("degree_2").equals(0))){
                                LinearLayout degree_2 = (LinearLayout) view.findViewById(R.id.second_degree);
                                degree_2.setVisibility(View.VISIBLE);

                                title2.setText(body.getString("degree_2"));
                                avg2.setText("Course Average: " + body.getString("avg2"));
                                code2.setText(body.getString("degree_2"));
                                gpa2 = Double.parseDouble(body.getString("degree_2_gpa"));

                                if(gpa2 > Honours){
                                    maxAngle2 = (int) Math.floor((gpa2 / 7) * 360);
                                } else {
                                    maxAngle2 = (int) Math.floor((Honours / 7) * 360);
                                }

                                chart2.setMaxAngle(maxAngle2);

                                setData(gpa2, chart2);
                                setChartDisplay(chart2, gpa2, maxAngle2);
                            } else {
                                LinearLayout degree_1 = (LinearLayout) view.findViewById(R.id.first_degree);
                                LinearLayout degree_data = (LinearLayout) view.findViewById(R.id.first_degree_data);

                                degree_1.setOrientation(LinearLayout.VERTICAL);
                                degree_1.setPadding(60, 60, 60, 60);
                                degree_1.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                degree_1.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);

                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) chart1.getLayoutParams();
                                lp.gravity = Gravity.CENTER_HORIZONTAL;
                                chart1.setLayoutParams(lp);
                                chart1.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);

                                LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) degree_data.getLayoutParams();
                                llp.gravity = Gravity.CENTER_HORIZONTAL;
                                degree_data.setLayoutParams(llp);
                                degree_data.setPadding(120, 120, 120, 120);
                                degree_data.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                degree_data.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);

                                title.setGravity(Gravity.CENTER_HORIZONTAL);
                                avg.setGravity(Gravity.CENTER_HORIZONTAL);
                                median.setGravity(Gravity.CENTER_HORIZONTAL);
                            }

                            title.setText(body.getString("degree_1"));
                            avg.setText("Course Average: " + body.getString("avg1"));
                            code.setText(body.getString("degree_1"));

                            title.setVisibility(View.VISIBLE);
                            gpa = Double.parseDouble(body.getString("degree_1_gpa"));

                            if(gpa > Honours){
                                maxAngle = (int) Math.floor((gpa / 7) * 360);
                            } else {
                                maxAngle = (int) Math.floor((Honours / 7) * 360);
                            }

                            chart1.setMaxAngle(maxAngle);

                            setChartDisplay(chart1, gpa, maxAngle);

                            setData(gpa, chart1);
                        } catch (JSONException e) {
                            messageBox("Get GPA Data", e.getMessage());
                            e.printStackTrace();
                        }



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


    private void setData(double gpa, PieChart mChart) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        double Honours = 5.5;

        entries.add(new PieEntry((float) gpa, "GPA"));

        if(Honours > (gpa + 0.1)){
            entries.add(new PieEntry((float) (Honours - gpa), "Honours Level"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
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

    private void setChartDisplay(PieChart mChart, double gpa, int maxAngle){

        mChart.setUsePercentValues(false);
        mChart.setCenterText("GPA \n" + Double.toString(gpa));
        //mChart.setCenterTextTypeface();
        mChart.setExtraOffsets(0, 5, 0, 0);
        mChart.setCenterTextSize(16);
        mChart.setCenterTextColor(Color.GRAY);
        mChart.getDescription().setEnabled(false);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(80f);
        mChart.setTransparentCircleRadius(75f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        //mChart.getLegend().setEnabled(false);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.setEntryLabelTextSize(0);
        System.out.println(maxAngle);

    }
}
