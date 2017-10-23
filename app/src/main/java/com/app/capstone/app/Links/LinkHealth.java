package com.app.capstone.app.Links;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.capstone.app.R;
import com.app.capstone.app.Requester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LinkHealth extends Fragment {

    private int[] links = new int[] {R.id.healthLink1, R.id.healthLink2, R.id.healthLink3};
    String url = "http://www.schemefactory.com.au:5000/";

    private OnFragmentInteractionListener mListener;

    public LinkHealth() {
    }

    public static LinkHealth newInstance(String param1, String param2) {
        LinkHealth fragment = new LinkHealth();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_link_contacts, container, false);

        final LinearLayout content = (LinearLayout) view.findViewById(R.id.link_contact_content);

        String uri = url + "links/health";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray JSONLinks = response.getJSONArray("Links");
                    ArrayList<String> links = new ArrayList<>();

                    int len = JSONLinks.length();
                    for (int i=0;i<len;i++){
                        links.add(JSONLinks.get(i).toString());
                    }

                    for (String link:links) {
                        TextView t = new TextView(getContext());
                        t.setText(link);
                        t.setMovementMethod(LinkMovementMethod.getInstance());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    messageBox("Unit data", e.toString());
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
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
