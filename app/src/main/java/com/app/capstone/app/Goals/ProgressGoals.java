package com.app.capstone.app.Goals;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
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

import org.json.JSONObject;

import java.util.ArrayList;

public class ProgressGoals extends Fragment {
    private PieChart mChart;
    final String url = "http://www.schemefactory:5000/";
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
        mChart = (PieChart) view.findViewById(R.id.progress_chart);


        String endpoint = "goals/progress/" + id;
        String uri = url + endpoint;

        uri = "http://www.google.com";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        content.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);

                        mChart = (PieChart) view.findViewById(R.id.chart1);
                        mChart.setUsePercentValues(false);
                        mChart.getDescription().setEnabled(false);
                        mChart.setExtraOffsets(5, 10, 5, 5);
                        mChart.setDragDecelerationFrictionCoef(0.95f);
                        mChart.setDrawHoleEnabled(true);
                        mChart.setHoleColor(ResourcesCompat.getColor(getResources(), R.color.colorBackground, null));
                        mChart.setTransparentCircleColor(ResourcesCompat.getColor(getResources(), R.color.colorBackground, null));
                        mChart.setTransparentCircleAlpha(110);
                        mChart.setHoleRadius(58f);
                        mChart.setTransparentCircleRadius(61f);
                        mChart.setDrawCenterText(true);
                        mChart.setRotationAngle(0);
                        mChart.setRotationEnabled(false);
                        mChart.setHighlightPerTapEnabled(true);

                        //TODO: Get data from JSON
                        int totalGoals = 10;
                        int completedGoals = 8;

                        int maxAngle = (completedGoals / totalGoals) * 360;
                        mChart.setMaxAngle(maxAngle);

                        setData(completedGoals);

                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                        mChart.setEntryLabelColor(Color.WHITE);
                        mChart.setEntryLabelTextSize(12f);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        content.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
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

    private void setData(int goals) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry(goals, "Completed Goals"));


        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
    }
}
