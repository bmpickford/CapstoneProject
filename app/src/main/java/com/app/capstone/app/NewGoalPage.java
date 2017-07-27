package com.app.capstone.app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.capstone.app.Course.CourseGPA;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewGoalPage extends AppCompatActivity implements GoalsPage.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal_page);

        Button button = (Button) findViewById(R.id.goalSubmitButton);
        final TextView edittext = (TextView) findViewById(R.id.dueDate);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int goal = R.layout.activity_new_goal_page;
                Goal g = new Goal("test", "test", new Date(), getApplicationContext());
                final LayoutInflater factory = getLayoutInflater();

                final View goalView = factory.inflate(R.layout.fragment_goals_page, null);

                final LinearLayout l = (LinearLayout) goalView.findViewById(R.id.goals_views);
                final LinearLayout l1 = (LinearLayout) findViewById(R.id.goals_views);
                View view = g.getView();
                System.out.println("Got View");
                l.addView(view);
                Fragment fragment = null;
                Class fragmentClass = null;

                fragmentClass = GoalsPage.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.goalWrapper, fragment).commit();
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }

                //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(i);
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
                new DatePickerDialog(NewGoalPage.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
