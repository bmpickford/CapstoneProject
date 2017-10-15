package com.app.capstone.app.Course;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class UnitDetailPage extends Fragment {
    private PieChart mChart;

    private OnFragmentInteractionListener mListener;

    public UnitDetailPage() {
        // Required empty public constructor
    }


    public static UnitDetailPage newInstance() {
        UnitDetailPage fragment = new UnitDetailPage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_unit_detail_page, container, false);
        Bundle item = getArguments();

        int id = item.getInt("id");

        String uri = "https://jsonplaceholder.typicode.com/posts/1";

        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.units_detail_spinner);
        final TextView content = (TextView) view.findViewById(R.id.unitDetails);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        spinner.setVisibility(View.INVISIBLE);

                        mChart = (PieChart) view.findViewById(R.id.unitChart);
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

                        double grade = 68.0;
                        double rest = 100 - grade;

                        setData(grade, rest);

                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                        mChart.setEntryLabelColor(Color.WHITE);
                        mChart.setEntryLabelTextSize(12f);

                        content.setText("Unit average: 59%\n\nYou're above average!");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.INVISIBLE);
                        System.out.println("Units volley error: " + error);
                    }
                });
        Requester.getInstance(getContext()).addToRequestQueue(jsObjRequest);
        return view;
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

    private void setData(double grade, double rest) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


        entries.add(new PieEntry((float) grade, "Your Grade"));
        entries.add(new PieEntry((float) rest, ""));

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