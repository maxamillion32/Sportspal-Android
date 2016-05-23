package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.DrawableImages;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.model.bean.Users;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/10/2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private String TAG = ProfileFragment.class.getSimpleName();
    private Activity activity;
    private MyTextView txt_name, txt_age, /*txt_occupation,*/
            txt_information;
    private ImageView img_user_pic, img_challenge, img_chat, img_fav;
    private ArrayList<Users> usersArrayList;
    private LinearLayout game_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Intent intent = new Intent("Header");
        intent.putExtra("message", "SP-" + activity.getString(R.string.title_profile));
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        txt_name = (MyTextView) rootView.findViewById(R.id.txt_player_name);
        txt_age = (MyTextView) rootView.findViewById(R.id.age_text);
//        txt_occupation = (MyTextView) rootView.findViewById(R.id.occupation_text);
        txt_information = (MyTextView) rootView.findViewById(R.id.information_text);

        img_challenge = (ImageView) rootView.findViewById(R.id.img_challenge);
        img_chat = (ImageView) rootView.findViewById(R.id.img_chat);
        img_fav = (ImageView) rootView.findViewById(R.id.img_favorite);
        img_user_pic = (ImageView) rootView.findViewById(R.id.img_user_pic);

        game_layout = (LinearLayout) rootView.findViewById(R.id.games_layout);

        img_fav.setOnClickListener(this);
        img_chat.setOnClickListener(this);
        img_challenge.setOnClickListener(this);

        usersArrayList = ModelManager.getInstance().getUsersManager().getMyDetails(false);
        if (usersArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getUsersManager().getMyDetails(true);
        } else {
            setData();
        }

        return rootView;
    }

    private void setData() {
        usersArrayList = ModelManager.getInstance().getUsersManager().getMyDetails(false);
        if (usersArrayList != null) {
            if (usersArrayList.size() > 0) {
                int i = 0;
                txt_name.setText(usersArrayList.get(i).getFirst_name());
                txt_age.setText(usersArrayList.get(i).getDob());
//                txt_occupation.setText(usersArrayList.get(i).get);
                txt_information.setText(usersArrayList.get(i).getDob());

                String img1 = "", img2 = "";

                img1 = usersArrayList.get(i).getImage();


                ArrayList<Sports> sportsArrayList = usersArrayList.get(i).getSports_preferences();
                if (sportsArrayList != null)
                    if (sportsArrayList.size() > 0) {
                        for (int j = 0; j < sportsArrayList.size(); j++) {
                            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v = vi.inflate(R.layout.row_sports_preferences, null);

                            MyTextView gameText1 = (MyTextView) v.findViewById(R.id.game_text1);
                            MyTextView gameText2 = (MyTextView) v.findViewById(R.id.game_text2);

                            gameText1.setText(sportsArrayList.get(j).getName());
                            j = j + 1;
                            if (sportsArrayList.size() >= j)
                                gameText2.setText(sportsArrayList.get(j).getName());
                            else
                                gameText2.setVisibility(View.GONE);

                            game_layout.addView(v);
                        }
                        img2 = sportsArrayList.get(0).getName();
                    }

                if (!Utils.isEmptyString(img1)) {
                    Picasso.with(activity).load(img1)
                            .placeholder(R.drawable.players)
                            .into(img_user_pic);
                } else {
                    img_user_pic.setImageResource(DrawableImages.setImage(img2));
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_favorite:
                break;

            case R.id.img_challenge:
                break;

            case R.id.img_chat:
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
        //gpDatabase.setConversation(chatManager.getConversations());
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(String message) {
        Log.e(TAG, "-- " + message);
        if (message.equalsIgnoreCase("GetMyDetails True")) {
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("GetMyDetails False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetMyDetails Network Error")) {
            Utils.dismissLoading();
        }
    }
}
