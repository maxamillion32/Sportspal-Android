package com.tanzil.sportspal.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Preferences;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.view.fragments.AddGameFragment;
import com.tanzil.sportspal.view.fragments.ChatFragment;
import com.tanzil.sportspal.view.fragments.FragmentDrawer;
import com.tanzil.sportspal.view.fragments.NewsFeedFragment;
import com.tanzil.sportspal.view.fragments.ProfileFragment;
import com.tanzil.sportspal.view.fragments.SettingsFragment;
import com.tanzil.sportspal.view.fragments.play.PlayMainFragment;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private FragmentManager fragmentManager;
    private boolean backer = false;
    private LinearLayout newsFeedLayout, playLayout, addSportLayout, chatLayout, profileLayout;
    private ImageView img_newsFeed, img_play, img_addSports, img_chat, img_profile, img_right;
    private MyTextView tvTitle, txt_news, txt_play, txt_add, txt_chat, txt_profile;
    private Activity activity = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SPLog.e("user id : ", ModelManager.getInstance().getAuthManager().getUserId());
        fragmentManager = getSupportFragmentManager();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(
                mHeaderReceiver, new IntentFilter("Header"));


        tvTitle = (MyTextView) findViewById(R.id.header_text);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        newsFeedLayout = (LinearLayout) findViewById(R.id.layout_news);
        playLayout = (LinearLayout) findViewById(R.id.layout_play);
        addSportLayout = (LinearLayout) findViewById(R.id.layout_add);
        chatLayout = (LinearLayout) findViewById(R.id.layout_chat);
        profileLayout = (LinearLayout) findViewById(R.id.layout_profile);

        img_newsFeed = (ImageView) findViewById(R.id.img_news);
        img_play = (ImageView) findViewById(R.id.img_play);
        img_addSports = (ImageView) findViewById(R.id.img_add);
        img_chat = (ImageView) findViewById(R.id.img_chat);
        img_profile = (ImageView) findViewById(R.id.img_profile);

        txt_news = (MyTextView) findViewById(R.id.news_text);
        txt_play = (MyTextView) findViewById(R.id.play_text);
        txt_add = (MyTextView) findViewById(R.id.add_text);
        txt_chat = (MyTextView) findViewById(R.id.chat_text);
        txt_profile = (MyTextView) findViewById(R.id.profile_text);

        // display the first navigation drawer view on app launch
        displayView(0, 0);

        newsFeedLayout.setOnClickListener(this);
        playLayout.setOnClickListener(this);
        addSportLayout.setOnClickListener(this);
        chatLayout.setOnClickListener(this);
        profileLayout.setOnClickListener(this);
    }

    /**
     * Header heading update method
     **/
    private final BroadcastReceiver mHeaderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            String textName[] = new String[2];
            try {
                textName = message.split("-");
            } catch (Exception e) {
                e.printStackTrace();
                textName[0] = "";
                textName[1] = "";
            }
            int pos = Integer.parseInt(textName[0]);
            setBackground(pos);
            tvTitle.setText(textName[1]);
            Log.d("receiver", "Got message: " + message);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_news:
                displayView(0, 0);
                break;
            case R.id.layout_play:
                displayView(1, 0);
                break;
            case R.id.layout_add:
                displayView(2, 0);
                break;
            case R.id.layout_chat:
                displayView(3, 0);
                break;
            case R.id.layout_profile:
                displayView(4, 0);
                break;
        }
    }

    private void setBackground(int position) {
        if (position == 1) {
            img_newsFeed.setImageResource(R.drawable.newsfeed);
            img_play.setImageResource(R.drawable.play);
            img_addSports.setImageResource(R.drawable.add);
            img_chat.setImageResource(R.drawable.chat);
            img_profile.setImageResource(R.drawable.profile);

            if (android.os.Build.VERSION.SDK_INT < 23) {
                txt_news.setTextColor(getResources().getColor(R.color.light_green));
                txt_play.setTextColor(getResources().getColor(R.color.white));
                txt_add.setTextColor(getResources().getColor(R.color.white));
                txt_chat.setTextColor(getResources().getColor(R.color.white));
                txt_profile.setTextColor(getResources().getColor(R.color.white));
            } else {
                txt_news.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.light_green));
                txt_play.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_add.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_chat.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_profile.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            }
        } else if (position == 2) {
            img_newsFeed.setImageResource(R.drawable.newsfeed);
            img_play.setImageResource(R.drawable.play);
            img_addSports.setImageResource(R.drawable.add);
            img_chat.setImageResource(R.drawable.chat);
            img_profile.setImageResource(R.drawable.profile);

            if (android.os.Build.VERSION.SDK_INT < 23) {
                txt_play.setTextColor(getResources().getColor(R.color.light_green));
                txt_news.setTextColor(getResources().getColor(R.color.white));
                txt_add.setTextColor(getResources().getColor(R.color.white));
                txt_chat.setTextColor(getResources().getColor(R.color.white));
                txt_profile.setTextColor(getResources().getColor(R.color.white));
            } else {
                txt_play.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.light_green));
                txt_news.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_add.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_chat.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_profile.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            }
        } else if (position == 3) {
            img_newsFeed.setImageResource(R.drawable.newsfeed);
            img_play.setImageResource(R.drawable.play);
            img_addSports.setImageResource(R.drawable.add);
            img_chat.setImageResource(R.drawable.chat);
            img_profile.setImageResource(R.drawable.profile);

            if (android.os.Build.VERSION.SDK_INT < 23) {
                txt_add.setTextColor(getResources().getColor(R.color.light_green));
                txt_play.setTextColor(getResources().getColor(R.color.white));
                txt_news.setTextColor(getResources().getColor(R.color.white));
                txt_chat.setTextColor(getResources().getColor(R.color.white));
                txt_profile.setTextColor(getResources().getColor(R.color.white));
            } else {
                txt_add.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.light_green));
                txt_play.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_news.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_chat.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_profile.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            }
        } else if (position == 4) {
            img_newsFeed.setImageResource(R.drawable.newsfeed);
            img_play.setImageResource(R.drawable.play);
            img_addSports.setImageResource(R.drawable.add);
            img_chat.setImageResource(R.drawable.chat);
            img_profile.setImageResource(R.drawable.profile);

            if (android.os.Build.VERSION.SDK_INT < 23) {
                txt_chat.setTextColor(getResources().getColor(R.color.light_green));
                txt_play.setTextColor(getResources().getColor(R.color.white));
                txt_add.setTextColor(getResources().getColor(R.color.white));
                txt_news.setTextColor(getResources().getColor(R.color.white));
                txt_profile.setTextColor(getResources().getColor(R.color.white));
            } else {
                txt_chat.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.light_green));
                txt_play.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_add.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_news.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_profile.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            }
        } else if (position == 5) {
            img_newsFeed.setImageResource(R.drawable.newsfeed);
            img_play.setImageResource(R.drawable.play);
            img_addSports.setImageResource(R.drawable.add);
            img_chat.setImageResource(R.drawable.chat);
            img_profile.setImageResource(R.drawable.profile);

            if (android.os.Build.VERSION.SDK_INT < 23) {
                txt_profile.setTextColor(getResources().getColor(R.color.light_green));
                txt_play.setTextColor(getResources().getColor(R.color.white));
                txt_add.setTextColor(getResources().getColor(R.color.white));
                txt_chat.setTextColor(getResources().getColor(R.color.white));
                txt_news.setTextColor(getResources().getColor(R.color.white));
            } else {
                txt_profile.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.light_green));
                txt_play.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_add.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_chat.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                txt_news.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            }
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position, 1);
    }

    private void displayView(int position, int flag) {
        setBackground(position);
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        if (flag == 0) {
            switch (position) {

                case 0:
                    fragment = new NewsFeedFragment();
                    title = getString(R.string.title_news);
                    break;
                case 1:
                    fragment = new PlayMainFragment();
                    title = getString(R.string.title_play);
                    break;
                case 2:
                    fragment = new AddGameFragment();
                    title = getString(R.string.title_add);
                    break;
                case 3:
                    fragment = new ChatFragment();
                    title = getString(R.string.title_chat);
                    break;
                case 4:
                    fragment = new ProfileFragment();
                    title = getString(R.string.title_profile);
                    break;
            }
        } else {
            switch (position) {

                case 0:
                    fragment = new NewsFeedFragment();
                    title = getString(R.string.title_news);
                    break;
                case 1:
                    fragment = new PlayMainFragment();
                    title = getString(R.string.title_play);
                    break;
                case 2:
                    fragment = new SettingsFragment();
                    title = getString(R.string.title_settings);
                    break;
                case 3:
                    showAlert("Are you sure, you want to log out?");
                    break;
            }

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

    public void showAlert(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setTitle("Logout!")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        JSONObject post_data = new JSONObject();
                        try {
                            post_data.put("device_type", "Android");
                            post_data.put("device_token", ModelManager.getInstance().getAuthManager().getDeviceToken());
                            SPLog.e(TAG, "LoginData" + post_data.toString());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        Utils.showLoading(activity, getString(R.string.please_wait));

                        ModelManager.getInstance().getAuthManager().logout(activity, post_data);
                        dialog.cancel();
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
            if (f instanceof NewsFeedFragment) {
                title = getString(R.string.title_news);
            } else if (f instanceof PlayMainFragment) {
                title = getString(R.string.title_play);
            } else if (f instanceof AddGameFragment) {
                title = getString(R.string.title_add);
            } else if (f instanceof ChatFragment) {
                title = getString(R.string.title_chat);
            } else if (f instanceof ProfileFragment) {
                title = getString(R.string.title_profile);
            } else if (f instanceof SettingsFragment) {
                title = getString(R.string.title_settings);
            }
        } catch (Exception e) {
            e.printStackTrace();
            title = getString(R.string.title_news);
        }
        // set the toolbar title
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    public void onEventMainThread(String message) {
        if (message.equalsIgnoreCase("Logout True")) {
            Utils.dismissLoading();
            SPLog.e(TAG, "Logout True");
            Preferences.clearAllPreference(MainActivity.this);
            startActivity(new Intent(MainActivity.this, LoginScreen.class));
            finish();
        } else if (message.contains("Logout False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, "Please try again");
            SPLog.e(TAG, "Logout False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("Logout Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            SPLog.e(TAG, "Logout Network Error");
            Utils.dismissLoading();
        }

    }
}