package com.tanzil.sportspal.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Preferences;
import com.tanzil.sportspal.view.fragments.FragmentDrawer;
import com.tanzil.sportspal.view.fragments.NewsFeedFragment;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private FragmentManager fragmentManager;
    private boolean backer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
    }



    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new NewsFeedFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new NewsFeedFragment();
                title = getString(R.string.title_predict);
                break;
            case 2:
                fragment = new NewsFeedFragment();
                title = getString(R.string.title_weekly);
                break;
            case 3:
                fragment = new NewsFeedFragment();
                title = getString(R.string.title_stocoin);
                break;
//            case 4:
//                fragment = new StocoinFragment();
//                title = getString(R.string.title_stocoin);
//                break;
            case 4:
                fragment = new NewsFeedFragment();
                title = getString(R.string.title_redeem);
                break;
            case 5:
                fragment = new NewsFeedFragment();
                title = getString(R.string.title_change_pass);
                break;
            case 6:
                showAlert(MainActivity.this, "Are you sure, you want to log out?");
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
    public void showAlert(Activity activity, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setTitle("Logout!")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Preferences.clearAllPreference(MainActivity.this);
                        dialog.cancel();
                        startActivity(new Intent(MainActivity.this, LoginScreen.class));
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // show it
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Fragment f = fragmentManager.findFragmentById(R.id.container_body);
        try {
            if (f instanceof NewsFeedFragment) {
                if (backer)
                    finish();
                else {
                    backer = true;
                    Toast.makeText(MainActivity.this, "Press again to exit the app.", Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onBackPressed();
                backer = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle();
    }

    private void setTitle() {
        String title = "";
        Fragment f = fragmentManager.findFragmentById(R.id.container_body);
        try {
//            if (f instanceof NewsFeedFragment) {
//                title = getString(R.string.title_home);
//            } else if (f instanceof NewsFeedFragment) {
//                title = getString(R.string.title_change_pass);
//            } else if (f instanceof NewsFeedFragment) {
//                title = getString(R.string.title_predict);
//            } else if (f instanceof NewsFeedFragment) {
//                title = getString(R.string.title_redeem);
//            } else if (f instanceof NewsFeedFragment) {
//                title = getString(R.string.title_stocoin);
//            } else if (f instanceof NewsFeedFragment) {
                title = getString(R.string.title_weekly);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            title = getString(R.string.title_home);
        }
        // set the toolbar title
        getSupportActionBar().setTitle(title);
    }
}