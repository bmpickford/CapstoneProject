package com.app.capstone.app.Links;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.capstone.app.R;

public class LinkHealth extends Fragment {

    private int[] links = new int[] {R.id.healthLink1, R.id.healthLink2, R.id.healthLink3, R.id.healthLink4, R.id.healthLink5, R.id.healthLink6, R.id.healthLink7};

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
        View view = inflater.inflate(R.layout.fragment_link_health, container, false);
        for (int link:links) {
            TextView t = (TextView) view.findViewById(link);
            t.setMovementMethod(LinkMovementMethod.getInstance());
        }

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
