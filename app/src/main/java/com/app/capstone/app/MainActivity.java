package com.app.capstone.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.capstone.app.Course.CourseBadges;
import com.app.capstone.app.Course.CourseCalculator;
import com.app.capstone.app.Course.CourseGPA;
import com.app.capstone.app.Course.CourseUnits;
import com.app.capstone.app.Course.UnitDetailPage;
import com.app.capstone.app.Goals.CurrentGoals;
import com.app.capstone.app.Goals.PastGoals;
import com.app.capstone.app.Goals.ProgressGoals;
import com.app.capstone.app.Links.LinkContacts;
import com.app.capstone.app.Links.LinkHealth;
import com.app.capstone.app.Links.LinkStudy;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements CourseGPA.OnFragmentInteractionListener, ProfilePage.OnFragmentInteractionListener, HomePage.OnFragmentInteractionListener,
        CoursePage.OnFragmentInteractionListener, LinksPage.OnFragmentInteractionListener, SettingsPage.OnFragmentInteractionListener,
        GoalsPage.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener, CourseUnits.OnFragmentInteractionListener,
        CourseBadges.OnFragmentInteractionListener, LinkStudy.OnFragmentInteractionListener, LinkContacts.OnFragmentInteractionListener,
        LinkHealth.OnFragmentInteractionListener, NewGoalPage.OnFragmentInteractionListener,
        CurrentGoals.OnFragmentInteractionListener, PastGoals.OnFragmentInteractionListener, ProgressGoals.OnFragmentInteractionListener,
        CourseCalculator.OnFragmentInteractionListener, UnitDetailPage.OnFragmentInteractionListener {

    public String s_no = "n8847762";

    public String getStudentNumber(){
        return s_no;
    }

    public void setStudentNumber(String n){
        this.s_no = n;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("creating");
        Bundle b = getIntent().getExtras();

        System.out.println(b);
        if(b.getString("s_no") != null){
            setStudentNumber(b.getString("s_no"));
        } else {
            setStudentNumber("n8847762");
        }

        super.onCreate(savedInstanceState);
        setContentView(com.app.capstone.app.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(com.app.capstone.app.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(com.app.capstone.app.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.app.capstone.app.R.string.navigation_drawer_open, com.app.capstone.app.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.app.capstone.app.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Class fragmentClass = HomePage.class;
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(com.app.capstone.app.R.id.flContent, fragment).commit();
        navigationView.setCheckedItem(R.id.nav_home);

        //assignProfileData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.app.capstone.app.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.app.capstone.app.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.app.capstone.app.R.id.action_logout) {
            Intent i= new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        System.out.println(item);
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == com.app.capstone.app.R.id.nav_home) {
            fragmentClass = HomePage.class;
            System.out.println("Home clicked");
        } else if (id == com.app.capstone.app.R.id.nav_course) {
            fragmentClass = CoursePage.class;
            System.out.println("Course clicked");
        } else if (id == com.app.capstone.app.R.id.nav_goals) {
            fragmentClass = GoalsPage.class;
            System.out.println("Goals clicked");
        } else if (id == com.app.capstone.app.R.id.nav_links) {
            fragmentClass = LinksPage.class;
            System.out.println("Links clicked");
        } else if (id == com.app.capstone.app.R.id.nav_settings) {
            fragmentClass = SettingsPage.class;
            System.out.println("Settings clicked");
        } else if (id == com.app.capstone.app.R.id.nav_profile) {
            fragmentClass = ProfilePage.class;
            System.out.println("Profile clicked");
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(com.app.capstone.app.R.id.flContent, fragment).commit();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.app.capstone.app.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayView(int id){
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == com.app.capstone.app.R.id.nav_home) {
            fragmentClass = HomePage.class;
            System.out.println("Home clicked");
        } else if (id == com.app.capstone.app.R.id.nav_course) {
            fragmentClass = CoursePage.class;
            System.out.println("Course clicked");
        } else if (id == com.app.capstone.app.R.id.nav_goals) {
            fragmentClass = GoalsPage.class;
            System.out.println("Goals clicked");
        } else if (id == com.app.capstone.app.R.id.nav_links) {
            fragmentClass = LinksPage.class;
            System.out.println("Links clicked");
        } else if (id == com.app.capstone.app.R.id.nav_settings) {
            fragmentClass = SettingsPage.class;
            System.out.println("Settings clicked");
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(com.app.capstone.app.R.id.flContent, fragment).commit();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.app.capstone.app.R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(com.app.capstone.app.R.id.nav_view);
        navigationView.setCheckedItem(id);
        //drawer. //getMenu().getItem(0).setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void assignProfileData(){
        // TODO: API call to retrieve this profile data
        ImageView avatar = (ImageView) findViewById(com.app.capstone.app.R.id.pAvatar);
        TextView name = (TextView) findViewById(com.app.capstone.app.R.id.pName);
        TextView studentNo = (TextView) findViewById(com.app.capstone.app.R.id.pStudentNo);
        TextView degree = (TextView) findViewById(com.app.capstone.app.R.id.pDegree);
        TextView email = (TextView) findViewById(com.app.capstone.app.R.id.pEmail);

        //int id = getResources().getIdentifier("ic_launcher_round", "drawable", getPackageName());

        //avatar.setImageResource(id);
        //name.setText("Random User");
        //studentNo.setText("n01234567");
        //degree.setText("Bachelor of Information Technology");
        //email.setText("randomuser@student.qut.edu.au");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void deleteGoal(View view) throws IllegalAccessException, InstantiationException, IOException {
        CurrentGoals cg = new CurrentGoals();
        cg.deleteGoal(view);
    }

    public void completeGoal(View view) throws IllegalAccessException, InstantiationException, IOException, JSONException {
        CurrentGoals cg = new CurrentGoals();
        cg.updateGoal(view);
    }

    public void uncompleteGoal(View view) throws IOException, JSONException {
        PastGoals pg = new PastGoals();
        pg.uncompleteGoal(view);
    }

    public void navigateMenu(String type){
        NavigationView navigationView = (NavigationView) findViewById(com.app.capstone.app.R.id.nav_view);
        switch(type){
            case "course":
                navigationView.setCheckedItem(R.id.nav_course);
                break;
            case "goals":
                navigationView.setCheckedItem(R.id.nav_goals);
                break;
            default:
                navigationView.setCheckedItem(R.id.nav_home);
                break;
        }


    }

}
