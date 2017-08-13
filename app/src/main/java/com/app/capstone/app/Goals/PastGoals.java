package com.app.capstone.app.Goals;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.app.capstone.app.ExpandableListAdapter;
import com.app.capstone.app.ExpandableListAdapterPast;
import com.app.capstone.app.Goal;
import com.app.capstone.app.R;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PastGoals extends Fragment {
    final String url = "http://www.schemefactory:5000/";

    ExpandableListAdapterPast listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int lastExpandedPosition = -1;

    // Initialisation of empty array for all goal object
    private HashMap<Integer, Goal> goalsMap = getGoals();

    private PastGoals.OnFragmentInteractionListener mListener;

    private HashMap<Integer, Goal> getGoals() throws IOException {
        HashMap<Integer, Goal> goals = new HashMap<>();
        Goal goal = new Goal("Completed goals", "This was my first goal", new Date(), getActivity(), 1);
        goals.put(goal.getId(), goal);

        String u = this.url; // + "/goals/past";
        URL url = new URL(this.url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("charset", "utf-8");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

            //OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

            //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //InputStream error = ((HttpURLConnection) urlConnection).getErrorStream();
            //InputStream response = urlConnection.getInputStream();


            //TODO: change this to suit output
/*            for (int c; (c = in.read()) >= 0;)
                System.out.print((char)c);*/
        } finally {
            urlConnection.disconnect();
            return goals;
        }

    }

    public void uncompleteGoal(View view) throws IOException, JSONException {
        View parent = (View) view.getParent().getParent().getParent();
        TextView taskTextViewItem = (TextView) parent.findViewById(R.id.lblListItemPast);
        String b = String.valueOf(taskTextViewItem.getText());
        String[] splitStr = b.split("-");
        String idStr = splitStr[0].replace(" ", "");
        int id = Integer.parseInt(idStr);
        final Goal g = goalsMap.get(id);
        g.setCompleted(false);
        //g.update();
        //refreshUI();
    }

    private void refreshUI() throws IOException {
        goalsMap.clear();
        goalsMap = getGoals();

        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("PastGoals");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }



    public PastGoals() throws IOException {
        // Required empty public constructor
    }


    public static PastGoals newInstance() throws IOException {
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
            goalsMap.put(g.getId(), g);
        }

        expListView = (ExpandableListView) view.findViewById(R.id.lvExpPast);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        prepareListData();


        listAdapter = new ExpandableListAdapterPast(getActivity(), listDataHeader, listDataChild);

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
        if (context instanceof PastGoals.OnFragmentInteractionListener) {
            mListener = (PastGoals.OnFragmentInteractionListener) context;
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
        Iterator it = goalsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<String> s = new ArrayList<String>();
            String header;
            Goal goal = (Goal) pair.getValue();

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
            s.add(goal.getId() + " - " +goal.getDescription() + " - DUE: " + goal.getEnd_dateStr());
            listDataChild.put(listDataHeader.get(i), s);
            i++;


            it.remove(); // avoids a ConcurrentModificationException
        }


    }
}
