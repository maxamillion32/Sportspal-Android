package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.view.adapters.GameChallengesAdapter;
import com.tanzil.sportspal.view.adapters.TeamChallengesAdapter;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/10/2016.
 */
public class NotificationsFragment extends Fragment implements View.OnClickListener {
    private String TAG = NotificationsFragment.class.getSimpleName();
    private Activity activity;
    private ImageView img_sports, img_team;
    private ListView notificationListView;
    private GameChallengesAdapter gameChallengesAdapter;
    private TeamChallengesAdapter teamChallengesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Utils.setHeader(activity, "1-" + activity.getString(R.string.title_notifications));

        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        ImageView imageView = (ImageView) activity.findViewById(R.id.img_right);
        imageView.setVisibility(View.INVISIBLE);

        img_sports = (ImageView) rootView.findViewById(R.id.img_sports);
        img_team = (ImageView) rootView.findViewById(R.id.img_team);

        notificationListView = (ListView) rootView.findViewById(R.id.list_notifications);

        img_team.setOnClickListener(this);
        img_sports.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_sports:
                if (Build.VERSION.SDK_INT < 23) {
                    img_sports.setBackgroundColor(activity.getResources().getColor(R.color.white));
                    img_team.setBackgroundColor(activity.getResources().getColor(R.color.transparent_white));
                } else {
                    img_sports.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                    img_team.setBackgroundColor(ContextCompat.getColor(activity, R.color.transparent_white));
                }
                img_team.setImageResource(R.drawable.team_pic);
                img_sports.setImageResource(R.drawable.sports_black);
                break;

            case R.id.img_team:
                if (Build.VERSION.SDK_INT < 23) {
                    img_sports.setBackgroundColor(activity.getResources().getColor(R.color.transparent_white));
                    img_team.setBackgroundColor(activity.getResources().getColor(R.color.white));
                } else {
                    img_sports.setBackgroundColor(ContextCompat.getColor(activity, R.color.transparent_white));
                    img_team.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                }
                img_team.setImageResource(R.drawable.team_black);
                img_sports.setImageResource(R.drawable.sports_pic);

                break;
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(String message) {
        Log.e(TAG, "-- " + message);
        if (message.equalsIgnoreCase("GetTeamDetails True")) {
            Utils.dismissLoading();
//            setData();
        } else if (message.equalsIgnoreCase("GetTeamDetails False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetTeamDetails Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("JoinTeam True")) {
            Utils.dismissLoading();
//            btn_join.setVisibility(View.INVISIBLE);
            Toast.makeText(activity, "Team joined successfully", Toast.LENGTH_SHORT).show();
        } else if (message.equalsIgnoreCase("JoinTeam False")) {
            Utils.dismissLoading();
        }
    }
}
