package com.app.capstone.app.Course;

import android.content.Context;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.capstone.app.Goals.ProgressGoals;
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

import static com.app.capstone.app.R.id.calculatedContent;


public class CourseCalculator extends Fragment {
    final String url = "http://www.schemefactory:5000/";
    private PieChart mChart;

    String id;

    private OnFragmentInteractionListener mListener;

    public CourseCalculator() {
        // Required empty public constructor
    }


    public static CourseCalculator newInstance(String param1, String param2) {
        CourseCalculator fragment = new CourseCalculator();
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
        final View view = inflater.inflate(R.layout.fragment_course_calculator, container, false);

        //Elements
        final CheckBox check = (CheckBox) view.findViewById(R.id.degreeCalcCheck);
        final EditText unitsLeft = (EditText) view.findViewById(R.id.unitsLeft);
        final Button calculate = (Button) view.findViewById(R.id.calculateGPA);
        final EditText goalGPA = (EditText) view.findViewById(R.id.goalGPA);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    unitsLeft.setVisibility(View.INVISIBLE);
                    unitsLeft.setText(null);
                } else {
                    unitsLeft.setVisibility(View.VISIBLE);
                }
             }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickView) {
                mChart = (PieChart) view.findViewById(R.id.calculatedGPAChart);
                double goal = 0;
                int units = 0;
                try {
                    goal = Double.parseDouble(String.valueOf(goalGPA.getText()));
                } catch(Exception e){
                    goalGPA.setError("Goal GPA must not be empty");
                }


                if(check.isChecked()){
                    boolean checkGpa = validateGPA(goal);

                    if(checkGpa){
                        calculateFromUnitsLeft(goal, view);
                    } else {
                        goalGPA.setError("Goal GPA must be between 1 and 7");
                    }
                } else {
                    try {
                        units = Integer.parseInt(String.valueOf(unitsLeft.getText()));
                    } catch(Exception e){
                        unitsLeft.setError("Units must not be empty");
                    }

                    boolean checkUnits = validateCreditPoints(units);
                    boolean checkGpa = validateGPA(goal);

                    if(checkGpa && checkUnits) {
                        calculateManually(goal, units, view);
                    } else if(checkGpa && !checkUnits) {
                        unitsLeft.setError("Credit points must be a multiple of 12");
                    } else if(checkUnits && !checkGpa){
                        goalGPA.setError("Goal GPA must be between 1 and 7");
                    } else {
                        goalGPA.setError("Goal GPA must be between 1 and 7");
                        unitsLeft.setError("Credit points must be a multiple of 12");
                    }
                }

            }
        });

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

    private void calculateFromUnitsLeft(final double goal, final View view){

        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.calculatorSpinner);
        spinner.setVisibility(View.VISIBLE);

        final LinearLayout formContent = (LinearLayout) view.findViewById(R.id.calculatedForm);
        final LinearLayout calculatedContent = (LinearLayout) view.findViewById(R.id.calculatedContent);
        final TextView goalText = (TextView) view.findViewById(R.id.calculatedGPA);

        String endpoint = "gpa/units/" + id;

        String uri;

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getGPACalc";
        } else {
            uri = url + endpoint;
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        formContent.setVisibility(View.INVISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                        calculatedContent.setVisibility(View.VISIBLE);

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

                        double goalGPA = goal;
                        double gpa = 0;
                        int cpLeft = 0;
                        int cpDone = 0;

                        try {
                            JSONObject body = new JSONObject(response.getString("body"));

                            gpa = Double.parseDouble(body.getJSONArray("Course_GPA").get(0).toString());
                            cpLeft = Integer.parseInt(body.getJSONArray("Outstanding_CP").get(0).toString());
                            cpDone = Integer.parseInt(body.getJSONArray("Done_CP").get(0).toString());

                        } catch (JSONException e) {
                            messageBox("Get GPA Data", e.getMessage());
                            e.printStackTrace();
                        }


                        GPACalculator gpaCalc = new GPACalculator(goalGPA, gpa, cpLeft, cpDone);

                        double neededGPA = gpaCalc.calculate();

                        goalText.setText("Current GPA: " + String.valueOf(gpa) + "\n\nTo achieve a GPA of: " + String.valueOf(goalGPA) + "\n\nYou neeed and average GPA of: " + String.valueOf(neededGPA) + " over " + String.valueOf(cpLeft / 12) + " units");
                        int maxAngle;

                        if(gpa > neededGPA){
                            maxAngle = (int) Math.floor((gpa / 7) * 360);
                        } else {
                            maxAngle = (int) Math.floor((neededGPA / 7) * 360);
                        }

                        mChart.setMaxAngle(maxAngle);

                        setData(gpa, neededGPA);

                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                        mChart.setEntryLabelColor(Color.WHITE);
                        mChart.setEntryLabelTextSize(12f);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        formContent.setVisibility(View.INVISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                        calculatedContent.setVisibility(View.VISIBLE);
                        goalText.setText("There was an error: " + error.toString());
                        messageBox("Get GPA Data", error.toString());
                    }
                });

        Requester.getInstance(getContext()).addToRequestQueue(jsObjRequest);


    }

    private void calculateManually(final double goal, final int units, View view){
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.calculatorSpinner);
        spinner.setVisibility(View.VISIBLE);

        final LinearLayout formContent = (LinearLayout) view.findViewById(R.id.calculatedForm);
        final LinearLayout calculatedContent = (LinearLayout) view.findViewById(R.id.calculatedContent);
        final TextView goalText = (TextView) view.findViewById(R.id.calculatedGPA);

        String endpoint = "gpa/units/" + id;

        String uri;

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getGPACalc";
        } else {
            uri = url + endpoint;
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        formContent.setVisibility(View.INVISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                        calculatedContent.setVisibility(View.VISIBLE);

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

                        double goalGPA = goal;
                        double gpa = 0;
                        int cpLeft = units;
                        int cpDone = 0;

                        try {
                            JSONObject body = new JSONObject(response.getString("body"));

                            gpa = Double.parseDouble(body.getJSONArray("gpa").get(0).toString());
                            cpDone = Integer.parseInt(body.getJSONArray("cpDone").get(0).toString());

                        } catch (JSONException e) {
                            messageBox("Get GPA Data", e.getMessage());
                            e.printStackTrace();
                        }

                        GPACalculator gpaCalc = new GPACalculator(goalGPA, gpa, cpLeft, cpDone);

                        double neededGPA = gpaCalc.calculate();

                        goalText.setText("Current GPA: " + String.valueOf(gpa) + "\n\nTo achieve a GPA of: " + String.valueOf(goalGPA) + "\n\nYou neeed and average GPA of: " + String.valueOf(neededGPA) + " over " + String.valueOf(cpLeft / 12) + " units");
                        int maxAngle;

                        if(gpa > neededGPA){
                            maxAngle = (int) Math.floor((gpa / 7) * 360);
                        } else {
                            maxAngle = (int) Math.floor((neededGPA / 7) * 360);
                        }

                        mChart.setMaxAngle(maxAngle);

                        setData(gpa, neededGPA);

                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                        mChart.setEntryLabelColor(Color.WHITE);
                        mChart.setEntryLabelTextSize(12f);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        formContent.setVisibility(View.INVISIBLE);
                        spinner.setVisibility(View.INVISIBLE);
                        calculatedContent.setVisibility(View.VISIBLE);
                        goalText.setText("There was an error: " + error.toString());
                        messageBox("Get GPA Data", error.toString());
                    }
                });

        Requester.getInstance(getContext()).addToRequestQueue(jsObjRequest);

    }

    private boolean validateGPA(double gpa){
        if(gpa < 1 || gpa > 7){
            return false;
        }
        return true;
    }

    private boolean validateCreditPoints(int cp){
        if(cp % 12 != 0 || cp < 12){
            return false;
        }
        return true;
    }

    private void setData(double gpa, double goalGPA) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


        if(goalGPA > gpa){
            entries.add(new PieEntry((float) gpa, "Your GPA"));
            entries.add(new PieEntry((float) (goalGPA - gpa), "Goal GPA"));
        } else {
            entries.add(new PieEntry((float) goalGPA, "Goal GPA"));
            entries.add(new PieEntry((float) (gpa - goalGPA), "Your GPA"));
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
