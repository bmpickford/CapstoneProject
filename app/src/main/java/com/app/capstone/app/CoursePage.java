package com.app.capstone.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.capstone.app.Course.CourseBadges;
import com.app.capstone.app.Course.CourseCalculator;
import com.app.capstone.app.Course.CourseGPA;
import com.app.capstone.app.Course.CourseUnits;


public class CoursePage extends Fragment implements CourseGPA.OnFragmentInteractionListener, CourseUnits.OnFragmentInteractionListener, CourseBadges.OnFragmentInteractionListener, CourseCalculator.OnFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;

    public CoursePage() {
        // Required empty public constructor
    }

    public static CoursePage newInstance(String param1, String param2) {
        CoursePage fragment = new CoursePage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = null;
        Class fragmentClass = CourseGPA.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.courseContent, fragment).commit();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_course_page, container, false);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        BottomNavigationHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                Class fragmentClass = null;
                switch (item.getItemId()) {

                    case R.id.action_gpa:
                        fragmentClass = CourseGPA.class;
                        System.out.println("You clicked GPA");
                        break;
                    case R.id.action_calculator:
                        fragmentClass = CourseCalculator.class;
                        System.out.println("You clicked GPA");
                        break;
                    case R.id.action_units:
                        fragmentClass = CourseUnits.class;
                        System.out.println("You clicked Units");
                        break;
                    case R.id.action_badges:
                        fragmentClass = CourseBadges.class;
                        System.out.println("You clicked Badges");
                        break;
                }
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.courseContent, fragment).commit();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}
