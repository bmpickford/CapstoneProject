package com.app.capstone.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfilePage extends Fragment {
    private OnFragmentInteractionListener mListener;
    final String url = "http://www.schemefactory.com:5000/";
    String id;

    public ProfilePage() {
        // Required empty public constructor
    }

    public static ProfilePage newInstance() {
        ProfilePage fragment = new ProfilePage();
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
        final View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        final LinearLayout content = (LinearLayout) view.findViewById(R.id.profile_content);
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.profile_spinner);

        final TextView name = (TextView) view.findViewById(R.id.pName);
        final TextView s_no = (TextView) view.findViewById(R.id.pStudentNo);
        final TextView degree = (TextView) view.findViewById(R.id.pDegree);
        final TextView email = (TextView) view.findViewById(R.id.pEmail);

        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.INVISIBLE);

        String endpoint = "student/" + id;

        String uri = url + endpoint;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        spinner.setVisibility(View.INVISIBLE);
                        content.setVisibility(View.VISIBLE);

                        try {
                            name.setText(response.getString("First_Name") + " " + response.getString("Last_Name"));
                            s_no.setText(response.getString("Student_ID"));
                            degree.setText(response.getString("Course_Code"));
                            email.setText(response.getString("Email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            messageBox("Profile Data Error", e.toString());
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.INVISIBLE);
                        content.setVisibility(View.VISIBLE);
                        messageBox("Get profile", error.toString());
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

    private void messageBox(String method, String message) {
        Log.d("EXCEPTION: " + method,  message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(getContext());
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
}
