package com.tanzil.sportspal.view.fragments.play;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.customUi.MyTextView;

/**
 * Created by arun.sharma on 5/4/2016.
 */
public class PlayerDetailFragment extends Fragment {

    private Activity activity;
    private ImageView profilePic;
    private MyTextView txt_playerName, txt_occupation, txt_age, txt_description, txt_advanced;
    private LinearLayout gamesLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        View rootView = inflater.inflate(R.layout.fragment_player_details, container, false);

        profilePic = (ImageView) rootView.findViewById(R.id.img_profile);

        txt_advanced = (MyTextView) rootView.findViewById(R.id.advanced_text);
        txt_playerName = (MyTextView) rootView.findViewById(R.id.player_name_text);
        txt_occupation = (MyTextView) rootView.findViewById(R.id.occupation_text);
        txt_age = (MyTextView) rootView.findViewById(R.id.age_text);
        txt_description = (MyTextView) rootView.findViewById(R.id.description_text);

        gamesLayout = (LinearLayout) rootView.findViewById(R.id.games_layout);

        return rootView;
    }
}
