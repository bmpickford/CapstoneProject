package com.app.capstone.app.Course;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
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

import org.json.JSONObject;


public class CourseBadges extends Fragment {

    private OnFragmentInteractionListener mListener;
    final String url = "http://www.schemefactory:5000/";
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

        if(id.equals("0001")){
            uri = "https://3ws25qypv8.execute-api.ap-southeast-2.amazonaws.com/prod/getBadges";
        } else {
            uri = url + endpoint;
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO: Add badges dynamically
                        spinner.setVisibility(View.INVISIBLE);
                        content.setVisibility(View.VISIBLE);

                        final CheckedTextView ctv = (CheckedTextView) view.findViewById(R.id.checkedTextView);
                        final CheckedTextView ctv2 = (CheckedTextView) view.findViewById(R.id.checkedTextView2);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.INVISIBLE);
                        content.setVisibility(View.VISIBLE);
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
}
