package com.app.capstone.app.Course;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
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


public class CourseGPA extends Fragment {
    private PieChart mChart;
    final String url = "http://www.schemefactory:5000/";


    private OnFragmentInteractionListener mListener;

    public CourseGPA() {
        // Required empty public constructor
    }

    public void getGPA(String data){

    }


    public static CourseGPA newInstance(String param1, String param2) {
        CourseGPA fragment = new CourseGPA();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String endpoint = "api/gpa/";

        String uri = url + endpoint;
        uri = "http://www.google.com";


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        gpaContent.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.INVISIBLE);

                        title.setText("Bachelor of Information Technology");
                        avg.setText("Average: 5.2");
                        median.setText("Median: 5.01");

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
                        double honours = 5.5;
                        double gpa = 6.1;
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

        entries.add(new PieEntry((float) gpa, "Your GPA"));

        if(honors > gpa){
            entries.add(new PieEntry((float) (honors - gpa), "Honors Level"));
        }

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
