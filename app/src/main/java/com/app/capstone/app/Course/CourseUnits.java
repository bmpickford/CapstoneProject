package com.app.capstone.app.Course;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.capstone.app.R;
import com.app.capstone.app.Requester;

import org.json.JSONObject;


public class CourseUnits extends Fragment {

    private OnFragmentInteractionListener mListener;
    final String url = "http://www.schemefactory:5000/";

    public CourseUnits() {
        // Required empty public constructor
    }

    public static CourseUnits newInstance(String param1, String param2) {
        CourseUnits fragment = new CourseUnits();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_units, container, false);

        final ScrollView content = (ScrollView) view.findViewById(R.id.units_content);
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.units_spinner);

        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.INVISIBLE);

        String endpoint = "api/units/";

        String uri = url + endpoint;
        uri = "https://jsonplaceholder.typicode.com/posts/1";


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO: Put units into view
                        spinner.setVisibility(View.INVISIBLE);
                        content.setVisibility(View.VISIBLE);
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
}
