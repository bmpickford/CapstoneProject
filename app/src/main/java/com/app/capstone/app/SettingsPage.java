package com.app.capstone.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsPage extends Fragment {
    private OnFragmentInteractionListener mListener;

    public SettingsPage() {
        // Required empty public constructor
    }
    public static SettingsPage newInstance(String param1, String param2) {
        SettingsPage fragment = new SettingsPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings_page, container, false);

        //Logout Button
        TextView logout = (TextView) view.findViewById(R.id.sLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: Add button functionality for logout here - Changes page but left menu needs to update
                System.out.println("Logout clicked");
                Intent i= new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
            }
        });

        //Edit Dashboard Button
        TextView editDashboard = (TextView) view.findViewById(R.id.sDashboard);
        editDashboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: Add button functionality for editing dashboard here
                System.out.println("Edit dashboard clicked");
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        System.out.println("onButtonPressed");
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
