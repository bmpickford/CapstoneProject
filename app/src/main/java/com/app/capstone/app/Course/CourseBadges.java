package com.app.capstone.app.Course;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;

import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.capstone.app.MainActivity;
import com.app.capstone.app.R;
import com.app.capstone.app.Requester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CourseBadges extends Fragment {

    private OnFragmentInteractionListener mListener;
    final String url = "http://www.schemefactory.com:5000/";
    String id;

    public CourseBadges() {
        // Required empty public constructor
    }


    public static CourseBadges newInstance() {
        CourseBadges fragment = new CourseBadges();
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
        final View view = inflater.inflate(R.layout.fragment_course_badges, container, false);

        final LinearLayout content = (LinearLayout) view.findViewById(R.id.badges_content);
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.badges_spinner);

        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.INVISIBLE);

        String endpoint = "badges/" + id;

        String uri;

        uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getBadges";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        spinner.setVisibility(View.INVISIBLE);
                        content.setVisibility(View.VISIBLE);

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

                        } catch (JSONException e) {
                            messageBox("Apply Badges Data", e.toString());
                        }

                        String gpa_sem = "gpaSTextView";
                        String gpa_yr = "gpaYTextView";
                        String goal = "goalTextView";
                        String blackboard = "blackboardTextView";

                        int[] imgs = {Integer.parseInt(gS), Integer.parseInt(gY), Integer.parseInt(g), Integer.parseInt(bl)};
                        String[] img_paths = {"badge_gpa_sem", "badge_gpa_year","badge_goals","badge_blackboard"};
                        String[] img_paths_2 = {"badge_gs", "badge_gy","badge_g","badge_bl"};


                        for(int x = 0; x < imgs.length; x++){
                            switch(imgs[x]){
                                case 0:
                                    ImageView iv0 = (ImageView) view.findViewById((getResources().getIdentifier(img_paths[x], "id", getContext().getPackageName())));
                                    iv0.setImageResource((getResources().getIdentifier(img_paths_2[x] + "_0", "drawable", getContext().getPackageName())));
                                    break;
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

                        for(int j = 1; j <= Integer.parseInt(gS); j++){
                            String w = gpa_sem + j;
                            CheckedTextView ctv = (CheckedTextView) view.findViewById(getResources().getIdentifier(w, "id", getContext().getPackageName()));
                            ctv.setChecked(true);
                        }

                        for(int j = 1; j <= Integer.parseInt(gY); j++){
                            String w = gpa_yr + j;
                            CheckedTextView ctv = (CheckedTextView) view.findViewById(getResources().getIdentifier(w, "id", getContext().getPackageName()));
                            ctv.setChecked(true);
                        }

                        for(int j = 1; j <= Integer.parseInt(g); j++){
                            String w = goal + j;
                            CheckedTextView ctv = (CheckedTextView) view.findViewById(getResources().getIdentifier(w, "id", getContext().getPackageName()));
                            ctv.setChecked(true);
                        }

                        for(int j = 1; j <= Integer.parseInt(bl); j++){
                            String w = blackboard + j;
                            CheckedTextView ctv = (CheckedTextView) view.findViewById(getResources().getIdentifier(w, "id", getContext().getPackageName()));
                            ctv.setChecked(true);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        messageBox("Get Badges Data", error.toString());
                        spinner.setVisibility(View.INVISIBLE);
                        content.setVisibility(View.VISIBLE);
                        messageBox("Get Badges", error.toString());
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
