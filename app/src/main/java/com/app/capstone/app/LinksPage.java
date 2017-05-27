package com.app.capstone.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.capstone.app.Course.CourseBadges;
import com.app.capstone.app.Course.CourseGPA;
import com.app.capstone.app.Course.CourseUnits;
import com.app.capstone.app.Links.LinkContacts;
import com.app.capstone.app.Links.LinkHealth;
import com.app.capstone.app.Links.LinkStudy;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LinksPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LinksPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinksPage extends Fragment {

    private OnFragmentInteractionListener mListener;

    public LinksPage() {
        // Required empty public constructor
    }


    public static LinksPage newInstance() {
        LinksPage fragment = new LinksPage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = null;
        Class fragmentClass = LinkContacts.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.linkContent, fragment).commit();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_links_page, container, false);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.link_bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                Class fragmentClass = null;
                switch (item.getItemId()) {

                    case R.id.action_contacts:
                        fragmentClass = LinkContacts.class;
                        System.out.println("You clicked contacts");
                        break;
                    case R.id.action_study:
                        fragmentClass = LinkStudy.class;
                        System.out.println("You clicked study");
                        break;
                    case R.id.action_health:
                        fragmentClass = LinkHealth.class;
                        System.out.println("You clicked health");
                        break;
                }
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.linkContent, fragment).commit();
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
                return true;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
