package com.app.capstone.app;

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

public class MainActivity extends AppCompatActivity
        implements HomePage.OnFragmentInteractionListener, CoursePage.OnFragmentInteractionListener, LinksPage.OnFragmentInteractionListener, SettingsPage.OnFragmentInteractionListener, GoalsPage.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.app.capstone.app.R.id.action_settings) {
            return true;
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
}
