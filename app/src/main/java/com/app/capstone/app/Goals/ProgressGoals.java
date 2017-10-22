package com.app.capstone.app.Goals;

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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class ProgressGoals extends Fragment {
    private PieChart mChart;
    final String url = "http://www.schemefactory.com:5000/";
    private String id;

    private OnFragmentInteractionListener mListener;

    public ProgressGoals() {
        // Required empty public constructor
    }


    public static ProgressGoals newInstance() {
        ProgressGoals fragment = new ProgressGoals();

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
        final View view = inflater.inflate(R.layout.fragment_progress_goals, container, false);


        final FrameLayout content = (FrameLayout) view.findViewById(R.id.goalProgressContent);
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.progressGoalSpinner);

        content.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.VISIBLE);


        String endpoint = "goals/progress/" + id;
        String uri;

        uri = "http://ec2-54-202-120-169.us-west-2.compute.amazonaws.com:5000/" + endpoint;


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        content.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);

                        //TODO: Get data from JSON
                        int totalGoals = 10;
                        int completedGoals = 8;

                        try {
                            totalGoals = response.getInt("total");
                            completedGoals = response.getInt("completed");
                        } catch (JSONException e) {
                            messageBox("Formatting goal data", e.toString());
                            e.printStackTrace();
                        }

                        mChart = (PieChart) view.findViewById(R.id.progress_chart);
                        mChart.setUsePercentValues(false);
                        mChart.getDescription().setEnabled(false);
                        mChart.setDragDecelerationFrictionCoef(0.95f);
                        mChart.setCenterText("Completed \n" + completedGoals + " out of " + totalGoals);
                        mChart.setCenterTextColor(Color.GRAY);
                        mChart.setCenterTextSize(18);
                        mChart.setDrawHoleEnabled(true);
                        mChart.setTransparentCircleColor(Color.WHITE);
                        mChart.setTransparentCircleAlpha(110);
                        mChart.setHoleRadius(80f);
                        mChart.setHoleColor(Color.WHITE);
                        mChart.setTransparentCircleRadius(75f);
                        mChart.setDrawCenterText(true);
                        mChart.setRotationAngle(0);
                        mChart.setRotationEnabled(true);
                        mChart.setHighlightPerTapEnabled(true);
                        mChart.setEntryLabelTextSize(0);

                        setData(completedGoals, totalGoals);

                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        content.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                        System.out.println(error.toString());
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

    private void setData(int goals, int total) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry(goals, "Completed Goals"));
        entries.add(new PieEntry((total - goals), "Total Goals"));


        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.createColors(new int[]{Color.rgb(22, 147, 165), Color.rgb(159, 92, 232)}));

        PieData data = new PieData(dataSet);
        data.setValueTextSize(0);
        data.setValueTextColor(Color.GRAY);
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
