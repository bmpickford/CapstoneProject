package com.app.capstone.app;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.capstone.app.Course.CourseBadges;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

<<<<<<< HEAD
=======

        final LinearLayout goal_link = (LinearLayout) view.findViewById(R.id.goal_link);
        final LinearLayout badge_link = (LinearLayout) view.findViewById(R.id.badge_link);
        final LinearLayout gpa_link = (LinearLayout) view.findViewById(R.id.gpa_link);

        goal_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass = null;

                fragmentClass = GoalsPage.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(com.app.capstone.app.R.id.flContent, fragment).commit();

                    ((MainActivity)getActivity()).navigateMenu("goals");
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        });

        badge_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass = null;

                fragmentClass = CourseBadges.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(com.app.capstone.app.R.id.flContent, fragment).commit();
                    ((MainActivity)getActivity()).navigateMenu("course");
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        });

        gpa_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass = null;

                fragmentClass = CourseGPA.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(com.app.capstone.app.R.id.flContent, fragment).commit();
                    ((MainActivity)getActivity()).navigateMenu("course");
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        });

        setGPA(view, spinner, content);
        setBadges(view);
        setGoals(view);




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

        double Honours = 5.5;

        entries.add(new PieEntry((float) gpa, "GPA"));

        if(Honours > (gpa + 0.2)){
            entries.add(new PieEntry((float) (Honours - gpa), "Honours Level"));
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

    private  void setGPA(final View view, final ProgressBar spinner, final LinearLayout content){
>>>>>>> 6018c22e8cf71621763f7244ca90d13fea39ec55
        String endpoint = "gpa/" + id;

        String uri;

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getGPA";
        } else {
            uri = url + endpoint;
        }

        final TextView gpa_title = (TextView) view.findViewById(R.id.gpa_title);

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
<<<<<<< HEAD

=======
>>>>>>> 6018c22e8cf71621763f7244ca90d13fea39ec55
                            gpa = Double.parseDouble(body.getJSONObject("Course_GPA").getString("0"));
                            gpa_title.setText(body.getJSONObject("Course_Code").getString("0"));
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
                        mChart.setExtraOffsets(10, 10, 10, 10);
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
                        mChart.getLegend().setEnabled(false);
                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                        mChart.setEntryLabelTextSize(0);

                        double Honours = 5.5;

                        System.out.println(gpa);

                        int maxAngle;

                        if(gpa > Honours){
                            maxAngle = (int) Math.floor((gpa / 7) * 360);
                        } else {
                            maxAngle = (int) Math.floor((Honours / 7) * 360);
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

    }

    private void setBadges(final View view){
        String endpoint = "badges/" + id;

        String uri;

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getBadges";
        } else {
            uri = url + endpoint;
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String gS = null; //GPA sem
                        String gY = null; //GPA yr
                        String g = null; //Goals
                        String bl = null; //Blackboard

                        try {
                            JSONObject body = response;

                            JSONObject a = body.getJSONObject("gpa_sem");
                            JSONObject b = body.getJSONObject("gpa_yr");
                            JSONObject c = body.getJSONObject("goals");
                            JSONObject d = body.getJSONObject("blackboard");


                            gS = a.getString("0");
                            gY = b.getString("0");
                            g = c.getString("0");
                            bl = d.getString("0");

                            int[] imgs = {Integer.parseInt(gS), Integer.parseInt(gY), Integer.parseInt(g), Integer.parseInt(bl)};
                            String[] img_paths = {"home_gpa_sem", "home_gpa_year","home_goals","home_blackboard"};
                            String[] img_paths_2 = {"badge_gs", "badge_gy","badge_g","badge_bl"};


                            for(int x = 0; x < imgs.length; x++){
                                switch(imgs[x]){
                                    case 1:
                                        ImageView iv = (ImageView) view.findViewById((getResources().getIdentifier(img_paths[x], "id", getContext().getPackageName())));
                                        iv.setImageResource((getResources().getIdentifier(img_paths_2[x] + "_b", "drawable", getContext().getPackageName())));
                                        break;
                                    case 2:
                                        ImageView iv2 = (ImageView) view.findViewById((getResources().getIdentifier(img_paths[x], "id", getContext().getPackageName())));
                                        iv2.setImageResource((getResources().getIdentifier(img_paths_2[x] + "_s", "drawable", getContext().getPackageName())));
                                        break;
                                    case 3:
                                        ImageView iv3 = (ImageView) view.findViewById((getResources().getIdentifier(img_paths[x], "id", getContext().getPackageName())));
                                        iv3.setImageResource((getResources().getIdentifier(img_paths_2[x] + "_g", "drawable", getContext().getPackageName())));
                                        break;
                                    default:
                                        messageBox("Data Error", "Data recieved for badges is either null or out of range. Check API call");
                                        break;
                                }
                            }

                        } catch (JSONException e) {
                            messageBox("Apply Badges Data", e.toString());
                        }

<<<<<<< HEAD

=======
>>>>>>> 6018c22e8cf71621763f7244ca90d13fea39ec55



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        messageBox("Get Badges", error.toString());
                    }
                });
        Requester.getInstance(getContext()).addToRequestQueue(jsObjRequest);

    }

    private void setGoals(View view){

<<<<<<< HEAD
        data.setHighlightEnabled(false);
        mChart.setData(data);
=======
>>>>>>> 6018c22e8cf71621763f7244ca90d13fea39ec55

        final LinearLayout goal_content = (LinearLayout) view.findViewById(R.id.home_goal_content);

        String endpoint = "goals/present/" + id;
        String uri;// = url + endpoint;


        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getPresentGoals";
        } else {
            uri = url + endpoint;
        }

        System.out.println("using url: " + uri);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, uri, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        llp.setMargins(80, 0, 80, 0); // llp.setMargins(left, top, right, bottom);

                        try {
                            for(int i = 0; i < 3; i++){
                                TextView tv=new TextView(getContext());
                                tv.setLayoutParams(llp);
                                String desc = response.getJSONObject("Description").getString(String.valueOf(i));
                                String exp = response.getJSONObject("Exp_Date").getString(String.valueOf(i));
                                tv.setText(desc + " - DUE: " + exp);
                                tv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.border));


                                tv.setLayoutParams(llp);
                                tv.setPadding(0, 60, 0, 20);
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(16);
                                goal_content.addView(tv);
                            }
                        } catch (JSONException e) {
                            TextView tv=new TextView(getContext());
                            tv.setLayoutParams(llp);
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(16);
                            tv.setText("No Goals Yet");
                            goal_content.addView(tv);
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        messageBox("Goal Server Error", error.toString());
                    }
                });

        Requester.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }
}

