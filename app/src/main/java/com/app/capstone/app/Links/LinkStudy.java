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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LinkStudy.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LinkStudy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinkStudy extends Fragment {
    private OnFragmentInteractionListener mListener;

    private int[] links = new int[] {R.id.studyLink1, R.id.studyLink2, R.id.studyLink3, R.id.studyLink4, R.id.studyLink5, R.id.studyLink6, R.id.studyLink7};

    public LinkStudy() {
        // Required empty public constructor
    }

    public static LinkStudy newInstance(String param1, String param2) {
        LinkStudy fragment = new LinkStudy();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_link_study, container, false);
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