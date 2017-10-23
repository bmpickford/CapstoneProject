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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UnitDetailPage extends Fragment {
    private PieChart mChart;
    String id;
    String url = "http://www.schemefactory.com:5000/";

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
        id = ((MainActivity)getActivity()).getStudentNumber();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_unit_detail_page, container, false);

        final TextView code = (TextView) view.findViewById(R.id.unit_code);
        final TextView name = (TextView) view.findViewById(R.id.unit_name);
        Bundle item = getArguments();

        int uid = item.getInt("id");

        System.out.println("ID: " + uid);

        String endpoint = "units/" + id + "/" + uid;

        String uri;

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getUnitDetail/" + uid;
        } else {
            uri = "http://ec2-54-202-120-169.us-west-2.compute.amazonaws.com:5000/" + endpoint;
        }

        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.units_detail_spinner);
        final TextView content = (TextView) view.findViewById(R.id.unitDetails);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());

                        spinner.setVisibility(View.INVISIBLE);
                        double grade = 0;// = 68.0;
                        double average = 0;
                        try{
                            JSONObject ja = response.getJSONArray("data").getJSONObject(0);

                            grade = Double.parseDouble(ja.getString("mark"));
                            average = Double.parseDouble(ja.getString("average"));
                            String code_j = ja.getString("code");
                            String name_j = ja.getString("name");

                            code.setText(code_j);
                            name.setText(name_j);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mChart = (PieChart) view.findViewById(R.id.unitChart);
                        mChart.setUsePercentValues(false);
                        mChart.setCenterText(grade + "%");
                        mChart.setCenterTextSize(18);
                        mChart.setCenterTextColor(Color.GRAY);
                        mChart.getDescription().setEnabled(false);
                        mChart.setExtraOffsets(5, 10, 5, 5);
                        mChart.setDragDecelerationFrictionCoef(0.95f);
                        mChart.setDrawHoleEnabled(true);
                        mChart.setHoleColor(ResourcesCompat.getColor(getResources(), R.color.colorBackground, null));
                        mChart.setTransparentCircleColor(ResourcesCompat.getColor(getResources(), R.color.colorBackground, null));
                        mChart.setTransparentCircleAlpha(110);
                        mChart.setHoleRadius(75f);
                        mChart.setTransparentCircleRadius(70f);
                        mChart.setDrawCenterText(true);
                        mChart.setRotationAngle(0);
                        mChart.setRotationEnabled(false);
                        mChart.setHighlightPerTapEnabled(true);
                        mChart.getLegend().setEnabled(false);
                        mChart.setEntryLabelTextSize(0);


                        mChart.setMaxAngle((float) (grade / 100) * 360);

                        setData(grade);

                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

                        if(grade > average){
                            content.setText("Unit average: " + average + "\n\nYou're above average!");
                        } else if (grade >= 50.0){
                            content.setText("Unit average: " + average + "\n\nCongratulations on passing!!");
                        } else {
                            content.setText("Unit average: " + average);
                        }

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

    private void setData(double grade) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


        entries.add(new PieEntry((float) grade, "Your Grade"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(0);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
    }
}
