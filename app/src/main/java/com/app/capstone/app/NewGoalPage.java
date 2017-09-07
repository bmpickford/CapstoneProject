package com.app.capstone.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
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
    private Calendar myCalendar = Calendar.getInstance();


    private static String title;
    private static String due_date;
    private static int priority = 1;
    private static int type = 1;
    private static String links;


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

        final Button low_btn = (Button) view.findViewById(R.id.low_priority);
        final Button med_btn = (Button) view.findViewById(R.id.medium_priority);
        final Button high_btn = (Button) view.findViewById(R.id.high_priority);

        final Button personal = (Button) view.findViewById(R.id.personal_btn);
        final Button career = (Button) view.findViewById(R.id.career_btn);
        final Button academic = (Button) view.findViewById(R.id.academic_btn);

        final EditText linksBtn = (EditText) view.findViewById(R.id.linked_ids);


        low_btn.setBackgroundColor(0xFF00c500);
        personal.setBackgroundColor(0xFF28487B);
        low_btn.setTextColor(Color.WHITE);
        personal.setTextColor(Color.WHITE);

        low_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = 1;
                System.out.println("low btn clicked");
                low_btn.setTextColor(Color.WHITE);
                med_btn.setTextColor(Color.BLACK);
                high_btn.setTextColor(Color.BLACK);
                low_btn.setBackgroundColor(0xFF00c500);
                med_btn.setBackgroundColor(0x00000000);
                high_btn.setBackgroundColor(0x00000000);
            }
        });

        med_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = 2;
                System.out.println("med btn clicked");
                low_btn.setTextColor(Color.BLACK);
                med_btn.setTextColor(Color.WHITE);
                high_btn.setTextColor(Color.BLACK);
                low_btn.setBackgroundColor(0x00000000);
                med_btn.setBackgroundColor(0xFFFFA500);
                high_btn.setBackgroundColor(0x00000000);
            }
        });

        high_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = 3;
                System.out.println("high btn clicked");
                low_btn.setTextColor(Color.BLACK);
                med_btn.setTextColor(Color.BLACK);
                high_btn.setTextColor(Color.WHITE);
                low_btn.setBackgroundColor(0x00000000);
                med_btn.setBackgroundColor(0x00000000);
                high_btn.setBackgroundColor(0xFFCC0000);
            }
        });

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;
                personal.setTextColor(Color.WHITE);
                career.setTextColor(Color.BLACK);
                academic.setTextColor(Color.BLACK);
                personal.setBackgroundColor(0xFF28487B);
                career.setBackgroundColor(0x00000000);
                academic.setBackgroundColor(0x00000000);
            }
        });

        career.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;
                personal.setTextColor(Color.BLACK);
                career.setTextColor(Color.WHITE);
                academic.setTextColor(Color.BLACK);
                personal.setBackgroundColor(0x00000000);
                career.setBackgroundColor(0xFF28487B);
                academic.setBackgroundColor(0x00000000);
            }
        });

        academic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 3;
                personal.setTextColor(Color.BLACK);
                career.setTextColor(Color.BLACK);
                academic.setTextColor(Color.WHITE);
                personal.setBackgroundColor(0x00000000);
                career.setBackgroundColor(0x00000000);
                academic.setBackgroundColor(0xFF28487B);
            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendar.set(Calendar.YEAR, y);
                myCalendar.set(Calendar.MONTH, m);
                myCalendar.set(Calendar.DAY_OF_MONTH, d);
                updateLabel(edittext, myCalendar);
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                due_date = edittext.getText().toString();
                title = t.getText().toString();
                links = linksBtn.getText().toString();

                bundle.putString("title", title);
                bundle.putString("due_date", due_date);
                bundle.putInt("priority", priority);
                bundle.putInt("type", type);
                bundle.putString("links", links);

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


        return view;
    }

    private void updateLabel(EditText edittext, Calendar myCalendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        edittext.setText(sdf.format(myCalendar.getTime()));
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
