package com.app.capstone.app.Goals;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.app.capstone.app.ExpandableListAdapter;
import com.app.capstone.app.Goal;
import com.app.capstone.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PastGoals extends Fragment {


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int lastExpandedPosition = -1;

    // Initialisation of empty array for all goal object
    private List<Goal> goals = getGoals();

    private CurrentGoals.OnFragmentInteractionListener mListener;

    private ArrayList<Goal> getGoals(){
        ArrayList<Goal> goals = new ArrayList<Goal>();
        Goal goal = new Goal("Completed goals", "This was my first goal", new Date(), getActivity());
        goals.add(goal);


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

    public PastGoals() {
        // Required empty public constructor
    }


    public static PastGoals newInstance() {
        PastGoals fragment = new PastGoals();
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_goals, container, false);

        Bundle item = getArguments();
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

        expListView = (ExpandableListView) view.findViewById(R.id.lvExpPast);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        prepareListData();


        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
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
        if (context instanceof CurrentGoals.OnFragmentInteractionListener) {
            mListener = (CurrentGoals.OnFragmentInteractionListener) context;
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

    private void prepareListData() {

        int i = 0;
        for (Goal goal: goals) {
            List<String> s = new ArrayList<String>();
            String header;

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(new Date());
            cal2.setTime(goal.getEnd_date());

            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if(sameDay){
                header = goal.getName() + "  - DUE NOW";
            } else if(new Date().after(goal.getEnd_date())){
                header = goal.getName() + "  - OVERDUE";
            } else {
                header = goal.getName();
            }

            listDataHeader.add(header);
            s.add(goal.getDescription() + " - DUE: " + goal.getEnd_dateStr());
            listDataChild.put(listDataHeader.get(i), s);
            i++;
        }


    }
}
