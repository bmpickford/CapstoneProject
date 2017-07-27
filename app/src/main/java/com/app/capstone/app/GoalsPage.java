package com.app.capstone.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoalsPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoalsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalsPage extends Fragment {

    private OnFragmentInteractionListener mListener;

    public GoalsPage() {
        // Required empty public constructor
    }

    public static GoalsPage newInstance(String param1, String param2) {
        GoalsPage fragment = new GoalsPage();
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
        final View view = inflater.inflate(com.app.capstone.app.R.layout.fragment_goals_page, container, false);
        FloatingActionButton button = (FloatingActionButton) view.findViewById(com.app.capstone.app.R.id.addGoalButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // TODO: Add button functionality for logout here - Changes page but left menu needs to update
                System.out.println("Logout clicked");
                Intent i= new Intent(getActivity(),NewGoalPage.class);
                startActivity(i);
            }

        });

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
