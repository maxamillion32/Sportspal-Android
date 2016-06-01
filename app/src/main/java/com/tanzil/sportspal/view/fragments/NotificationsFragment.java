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

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.GameNotifications;
import com.tanzil.sportspal.model.bean.TeamNotifications;
import com.tanzil.sportspal.view.adapters.GameChallengesAdapter;
import com.tanzil.sportspal.view.adapters.TeamChallengesAdapter;

import java.util.ArrayList;

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
    private ArrayList<GameNotifications> gameNotificationsArrayList;
    private ArrayList<TeamNotifications> teamNotificationsArrayList;

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

        gameNotificationsArrayList = ModelManager.getInstance().getNotificationsManager().getGameChallengesNotifications(false);
        if (gameNotificationsArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getNotificationsManager().getGameChallengesNotifications(true);
        } else {
            setData(0);
        }


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

                Utils.showLoading(activity, activity.getString(R.string.please_wait));
                ModelManager.getInstance().getNotificationsManager().getGameChallengesNotifications(true);
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

                Utils.showLoading(activity, activity.getString(R.string.please_wait));
                ModelManager.getInstance().getNotificationsManager().getTeamChallengesNotifications(true);
                break;
        }
    }

    private void setData (int type) {
        if (type == 0) {
            gameNotificationsArrayList = ModelManager.getInstance().getNotificationsManager().getGameChallengesNotifications(false);
            gameChallengesAdapter = new GameChallengesAdapter(activity, gameNotificationsArrayList);
            notificationListView.setAdapter(gameChallengesAdapter);
            gameChallengesAdapter.notifyDataSetChanged();
        } else {
            teamNotificationsArrayList = ModelManager.getInstance().getNotificationsManager().getTeamChallengesNotifications(false);
            teamChallengesAdapter = new TeamChallengesAdapter(activity, teamNotificationsArrayList);
            notificationListView.setAdapter(teamChallengesAdapter);
            teamChallengesAdapter.notifyDataSetChanged();
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
        if (message.equalsIgnoreCase("GetGameChallenges True")) {
            Utils.dismissLoading();
            setData(0);
        } else if (message.equalsIgnoreCase("GetGameChallenges False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetGameChallenges Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetTeamChallenges True")) {
            Utils.dismissLoading();
            setData(1);
        } else if (message.equalsIgnoreCase("GetTeamChallenges False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetTeamChallenges Network Error")) {
            Utils.dismissLoading();
        }
    }
}
