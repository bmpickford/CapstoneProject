package com.app.capstone.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.app.capstone.app.Goals.CurrentGoals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NewGoalPage extends Fragment {
    private Button button;
    private TextView edittext;

    private static String title;
    private static String description;
    private static String due_date;


    private OnFragmentInteractionListener mListener;

    public NewGoalPage() {
        // Required empty public constructor
    }


    public static NewGoalPage newInstance() {
        NewGoalPage fragment = new NewGoalPage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(com.app.capstone.app.R.layout.fragment_new_goal_page, container, false);

        Button button = (Button) view.findViewById(R.id.goalSubmitButton);
        final EditText edittext = (EditText) view.findViewById(R.id.dueDate);
        final AutoCompleteTextView t = (AutoCompleteTextView) view.findViewById(R.id.goalName);
        final AutoCompleteTextView desc = (AutoCompleteTextView) view.findViewById(R.id.goalDescription);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                //String d = edittext.getText().toString();
                due_date = edittext.getText().toString();
                title = t.getText().toString();
                description = desc.getText().toString();
/*                Date d = null;
                try {
                    d = formatter.parse(due_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Goal g = new Goal(title, description, d, getActivity());*/

                bundle.putString("title", title);
                bundle.putString("description", description);
                bundle.putString("due_date", due_date);


                Fragment fragment;
                Class fragmentClass = GoalsPage.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }

            }
        });

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(edittext, myCalendar);
            }

            private void updateLabel(TextView edittext, Calendar myCalendar) {

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                edittext.setText(sdf.format(myCalendar.getTime()));

            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;
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
