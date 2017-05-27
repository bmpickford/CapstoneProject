package com.app.capstone.app.Course;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.capstone.app.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;


public class CourseGPA extends Fragment {
    private PieChart mChart;


    private OnFragmentInteractionListener mListener;

    public CourseGPA() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_course_gpa, container, false);

        //mChart = (PieChart) view.findViewById(R.id.pieChart);
        //PieChart pieChart = new PieChart();
        mChart = (PieChart) view.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        //mChart.setCenterTextTypeface(mTfLight);
        //mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this);

        setData(2, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        mChart.setEntryLabelColor(Color.WHITE);
        //mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);
       /* ArrayList<PieEntry> entries;
        ArrayList<String> PieEntryLabels;
        PieDataSet pieDataSet;
        PieData pieData;


        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        entries.add(new PieEntry(2f, 0));
        entries.add(new PieEntry(4f, 1));
        entries.add(new PieEntry(6f, 2));
        entries.add(new PieEntry(8f, 3));
        entries.add(new PieEntry(7f, 4));
        entries.add(new PieEntry(3f, 5));

        PieEntryLabels.add("January");
        PieEntryLabels.add("February");
        PieEntryLabels.add("March");
        PieEntryLabels.add("April");
        PieEntryLabels.add("May");
        PieEntryLabels.add("June");

        pieDataSet = new PieDataSet(entries, "");

        pieData = new PieData(pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);
*/
        //pieChart.animateY(3000);
    
        
        //int[] dataObjects = {};

        /*List<PieEntry> entries = new ArrayList<PieEntry>();
        List<String> labels = new ArrayList<String>();
        entries.add(new PieEntry(1, 0));
        entries.add(new PieEntry(8, 1));
        entries.add(new PieEntry(6, 2));
        entries.add(new PieEntry(12, 3));
        entries.add(new PieEntry(18, 4));
        entries.add(new PieEntry(9, 5));

        labels.add("One");
        labels.add("Two");
        labels.add("Three");
        labels.add("Four");
        labels.add("Five");
        labels.add("Six");

*//*        for (int data : dataObjects) {

            // turn your data into Entry objects
            entries.add(new Entry(data.getValueX(), data.getValueY()));
        }*//*
        PieDataSet dataset = new PieDataSet(entries, "Entries");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);


        PieData piedata = new PieData(dataset);
        //chart.animateY(5000);
        chart.setData(piedata);*/
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
/*        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry(2, getResources().getDrawable(R.drawable.ic_menu_send),
                    (float) ((Math.random() * mult) + mult / 5)));
        }*/

        entries.add(new PieEntry(2, getResources().getDrawable(R.drawable.ic_menu_send),65.5));
        entries.add(new PieEntry(2, getResources().getDrawable(R.drawable.ic_menu_send),34.5));

        PieDataSet dataSet = new PieDataSet(entries, "GPA");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }
}
