package com.app.capstone.app;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.capstone.app.Course.CourseGPA;
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
import java.util.Iterator;
import java.util.Map;


public class HomePage extends Fragment {

    private PieChart mChart;
    final String url = "http://www.schemefactory.com:5000/";
    String id;


    private CourseGPA.OnFragmentInteractionListener mListener;

    public HomePage() {
        // Required empty public constructor
    }


    public static HomePage newInstance() {
        HomePage fragment = new HomePage();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        id = ((MainActivity)getActivity()).getStudentNumber();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        final LinearLayout content = (LinearLayout) view.findViewById(R.id.homepage);
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.home_spinner);

        content.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.VISIBLE);


       /* final TextView title = (TextView) view.findViewById(R.id.gpaData1);
        final TextView avg = (TextView) view.findViewById(R.id.gpaData2);
        final TextView median = (TextView) view.findViewById(R.id.gpaData3);*/


        String endpoint = "gpa/" + id;

        String uri;

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getGPA";
        } else {
            uri = url + endpoint;
        }

        System.out.println("using url: " + uri);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        content.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                        double gpa = 0;
                        System.out.println(response.toString());

                        try {
                            JSONObject body = response;

                            /*title.setText(body.getJSONObject("Parent_Study_Package_Full_Title").getString("0"));
                            avg.setText("Average: " + body.getJSONObject("Mean").getString("0"));
                            median.setText("Median: " + body.getJSONObject("Median").getString("0"));*/
                            gpa = Double.parseDouble(body.getJSONObject("Course_GPA").getString("0"));
                        } catch (JSONException e) {
                            messageBox("Get GPA Data", e.getMessage());
                            e.printStackTrace();
                        }

                        mChart = (PieChart) view.findViewById(R.id.homechart);

                        mChart.setUsePercentValues(false);
                        mChart.setCenterText("GPA \n" + Double.toString(gpa));
                        //mChart.setCenterTextTypeface();
                        mChart.setCenterTextSize(16);
                        mChart.setCenterTextColor(Color.GRAY);
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

                        double honours = 5.5;

                        System.out.println(gpa);

                        int maxAngle;

                        if(gpa > honours){
                            maxAngle = (int) Math.floor((gpa / 7) * 360);
                        } else {
                            maxAngle = (int) Math.floor((honours / 7) * 360);
                        }

                        System.out.println(maxAngle);

                        mChart.setMaxAngle(maxAngle);

                        setData(gpa);


                        //mChart.setEntryLabelColor(Color.GRAY);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        content.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
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
        if (context instanceof CourseGPA.OnFragmentInteractionListener) {
            mListener = (CourseGPA.OnFragmentInteractionListener) context;
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


    private void messageBox(String method, String message) {
        Log.d("EXCEPTION: " + method,  message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(getContext());
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
}

