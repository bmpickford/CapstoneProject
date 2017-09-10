package com.app.capstone.app;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.capstone.app.Course.CourseBadges;
import com.app.capstone.app.Course.CourseGPA;
import com.app.capstone.app.Course.CourseUnits;
import com.app.capstone.app.Goals.CurrentGoals;
import com.app.capstone.app.Goals.PastGoals;
import com.app.capstone.app.Goals.ProgressGoals;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class GoalsPage extends Fragment implements CurrentGoals.OnFragmentInteractionListener, PastGoals.OnFragmentInteractionListener,
        ProgressGoals.OnFragmentInteractionListener {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    // Initialisation of empty array for all goal object
    private List<Goal> goals = getGoals();

    private OnFragmentInteractionListener mListener;

    private ArrayList<Goal> getGoals() throws IOException, JSONException {
        ArrayList<Goal> goals = new ArrayList<Goal>();
        Goal goal = new Goal("First Goal", 1, 2, new Date(), getActivity(), 1);
        Goal goal2 = new Goal("Complete IFB399", 2, 3, new Date(), getActivity(), 2);
        Goal goal3 = new Goal("Do Assignment", 2, 3, new Date(), getActivity(), 3);
        goals.add(goal);
        goals.add(goal2);
        goals.add(goal3);

        return goals;

        /*String u = this.url + /goals/present;
        URL url = new URL(u);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            //TODO: change this to suit output
            for (int c; (c = in.read()) >= 0;)
                System.out.print((char)c);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }*/
    }

    public GoalsPage() throws IOException, JSONException {
        // Required empty public constructor
    }

    public static GoalsPage newInstance(String one, String two, String three) throws IOException, JSONException {
        GoalsPage fragment = new GoalsPage();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Fragment fragment = null;
        Class fragmentClass = CurrentGoals.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.goalsContent, fragment).commit();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_goals_page, container, false);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);

        Bundle item = getArguments();
        System.out.println(item);
        if(item != null && item.getString("title") != null) {
            Fragment fragment = null;
            Class fragmentClass = CurrentGoals.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(item);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.goalsContent, fragment).commit();
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }


        final FloatingActionButton button = (FloatingActionButton) view.findViewById(com.app.capstone.app.R.id.addGoalButton);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                Class fragmentClass = null;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.goalsContent, fragment).commit();
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
                switch (item.getItemId()) {

                    case R.id.goal_present:
                        fragmentClass = CurrentGoals.class;
                        System.out.println("You clicked Current goals");
                        button.setVisibility(view.VISIBLE);
                        break;
                    case R.id.goal_past:
                        fragmentClass = PastGoals.class;
                        System.out.println("You clicked Past goals");
                        button.setVisibility(view.INVISIBLE);
                        break;
                    case R.id.goal_progress:
                        fragmentClass = ProgressGoals.class;
                        System.out.println("You clicked Progress goals");
                        button.setVisibility(view.VISIBLE);
                        break;
                }
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.goalsContent, fragment).commit();
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
                return true;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                System.out.println("New Goal clicked");

                Fragment fragment;
                Class fragmentClass = NewGoalPage.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }

        });







        /*Bundle item = getArguments();
        if(item != null && item.getString("title") != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String title = item.getString("title");
            String description = item.getString("description");
            Date d = new Date();
            try {
                d = formatter.parse(item.getString("due_date"));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            Goal g = new Goal(title, description, d, getActivity());
            goals.add(g);
        }

        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        prepareListData();


        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        FloatingActionButton button = (FloatingActionButton) view.findViewById(com.app.capstone.app.R.id.addGoalButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // TODO: Add button functionality for new goal here - Changes page but left menu needs to update
                System.out.println("New Goal clicked");
                //TextView textView = new TextView(getActivity());
                //textView.setText("testing");

                //Goal goal = new Goal("Clicked", "test", new Date(), getActivity());
                //View gView = goal.getView();

                //LinearLayout l = (LinearLayout) view.findViewById(R.id.goals_views);

                //l.addView(gView);

                Fragment fragment;
                Class fragmentClass = NewGoalPage.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
                //
                //View g = goal.getView();
                //Intent i= new Intent(getActivity(),NewGoalPageDup.class);
                //startActivity(i);
            }

        });
*/
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
