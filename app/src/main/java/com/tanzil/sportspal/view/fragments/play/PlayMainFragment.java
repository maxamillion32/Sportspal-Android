package com.tanzil.sportspal.view.fragments.play;

/**
 * Created by Arun on 29/07/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.customUi.MyTextView;


public class PlayMainFragment extends Fragment implements View.OnClickListener {

    private String TAG = PlayMainFragment.class.getSimpleName();
    private Activity activity;
    private LinearLayout sportsLayout, playersLayout, teamsLayout;
    private ImageView img_sports, img_players, img_teams;
    private MyTextView txt_sports, txt_players, txt_teams;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.activity = super.getActivity();
        View rootView = inflater.inflate(R.layout.fragment_play, container, false);

        fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();

        sportsLayout = (LinearLayout) rootView.findViewById(R.id.layout_sports);
        playersLayout = (LinearLayout) rootView.findViewById(R.id.layout_players);
        teamsLayout = (LinearLayout) rootView.findViewById(R.id.layout_teams);

        img_players = (ImageView) rootView.findViewById(R.id.img_players);
        img_sports = (ImageView) rootView.findViewById(R.id.img_sports);
        img_teams = (ImageView) rootView.findViewById(R.id.img_teams);

        txt_players = (MyTextView) rootView.findViewById(R.id.players_text);
        txt_sports = (MyTextView) rootView.findViewById(R.id.sports_text);
        txt_teams = (MyTextView) rootView.findViewById(R.id.teams_text);

        sportsLayout.setOnClickListener(this);
        playersLayout.setOnClickListener(this);
        teamsLayout.setOnClickListener(this);

        setBackground(0);
        setFragment(0);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_sports:
                setBackground(0);
                setFragment(0);
                break;

            case R.id.layout_players:
                setBackground(1);
                setFragment(1);
                break;

            case R.id.layout_teams:
                setBackground(2);
                setFragment(2);
                break;
        }
    }

    private void setBackground(int position) {
        int colorWhite = 0, colorSelected = 0;
        if (android.os.Build.VERSION.SDK_INT < 23) {
            colorWhite = activity.getResources().getColor(R.color.white);
            colorSelected = activity.getResources().getColor(R.color.light_green);
        } else {
            colorWhite = ContextCompat.getColor(activity, R.color.white);
            colorSelected = ContextCompat.getColor(activity, R.color.light_green);
        }

        if (position == 0) {
            txt_sports.setTextColor(colorSelected);
            txt_players.setTextColor(colorWhite);
            txt_teams.setTextColor(colorWhite);

            img_sports.setImageResource(R.drawable.sports);
            img_players.setImageResource(R.drawable.players);
            img_teams.setImageResource(R.drawable.team);
        } else if (position == 1) {
            txt_sports.setTextColor(colorWhite);
            txt_players.setTextColor(colorSelected);
            txt_teams.setTextColor(colorWhite);

            img_sports.setImageResource(R.drawable.sports);
            img_players.setImageResource(R.drawable.players);
            img_teams.setImageResource(R.drawable.team);
        } else {
            txt_sports.setTextColor(colorWhite);
            txt_players.setTextColor(colorWhite);
            txt_teams.setTextColor(colorSelected);

            img_sports.setImageResource(R.drawable.sports);
            img_players.setImageResource(R.drawable.players);
            img_teams.setImageResource(R.drawable.team);
        }
    }

    private void setFragment(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new SportsFragment();
        else if (position == 1)
            fragment = new PlayersFragment();
        else
            fragment = new TeamsFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.play_container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }
}
