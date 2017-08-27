package com.app.capstone.app.Goals;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.capstone.app.ExpandableListAdapter;
import com.app.capstone.app.Goal;
import com.app.capstone.app.NetworkRunner;
import com.app.capstone.app.NewGoalPage;
import com.app.capstone.app.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CurrentGoals extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private int lastExpandedPosition = -1;


    final String url = "http://www.schemefactory:5000/";


    private HashMap<Integer, Goal> goalsMap = new HashMap<>();// = getGoals();
    private OnFragmentInteractionListener mListener;



    public void getGoals(String data){
        System.out.println(data);
    }

/*    public HashMap<Integer, Goal> getGoals() throws IOException {
        HashMap<Integer, Goal> g = new HashMap<>();
        g.put(1, new Goal("First Goal", "This is my first goal", new Date(), getActivity(), 1));
        g.put(2, new Goal("2ns Goal", "This is my 2nd goal", new Date(), getActivity(), 2));
        g.put(3, new Goal("3rd Goal", "This is my 3rd goal", new Date(), getActivity(), 3));

       // System.out.println(run(this.url));
            //return response.body().string();

        //new NetworkRunner().execute(url);

        return g;
        *//*String u = this.url + /goals/present;
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
        }*//*
    }*/







    public CurrentGoals() throws IOException {
        // Required empty public constructor
    }

    public void deleteGoal(View view) throws IOException {
        View parent = (View) view.getParent().getParent().getParent();
        TextView taskTextViewItem = (TextView) parent.findViewById(R.id.lblListItem);
        String b = String.valueOf(taskTextViewItem.getText());
        String[] splitStr = b.split("-");
        String idStr = splitStr[0].replace(" ", "");
        int id = Integer.parseInt(idStr);
        final Goal g = goalsMap.get(id);


        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(view.getContext());
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Deleting");
                       /* try {
                            g.delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();

    }

    public void updateGoal(View view) throws IOException, JSONException {
        View parent = (View) view.getParent().getParent().getParent();
        TextView taskTextViewItem = (TextView) parent.findViewById(R.id.lblListItem);
        String b = String.valueOf(taskTextViewItem.getText());
        String[] splitStr = b.split("-");
        String idStr = splitStr[0].replace(" ", "");
        int id = Integer.parseInt(idStr);
        final Goal g = goalsMap.get(id);
        g.setCompleted(true);
        //g.update();
        //refreshUI();
    }

    private void refreshUI() throws IOException {
        System.out.println("Networking running");
        new NetworkRunner(url + "/api/goals/present", "GET", "CurrentGoals", "{id: 1}").execute(url, "test");
        if(goalsMap != null){
            goalsMap.clear();
        }


        try {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        } catch(Exception e){

        }

/*        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("CurrentGoals");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();*/
    }


    public static CurrentGoals newInstance() throws IOException {
        CurrentGoals fragment = new CurrentGoals();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            refreshUI();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_goals, container, false);

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

        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
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
