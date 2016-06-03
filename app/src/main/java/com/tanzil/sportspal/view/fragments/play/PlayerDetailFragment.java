package com.tanzil.sportspal.view.fragments.play;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.DrawableImages;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.model.bean.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/4/2016.
 */
public class PlayerDetailFragment extends Fragment implements View.OnClickListener {

    private String TAG = PlayerDetailFragment.class.getSimpleName();
    private Activity activity;
    private ImageView profilePic, img_challenge, img_chat, img_fav, img_right;
    private MyTextView txt_playerName, txt_occupation, txt_age, txt_description, txt_advanced;
    private LinearLayout gamesLayout;
    private ArrayList<Users> usersArrayList;
    private String id = "";
    private boolean is_Fav = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Utils.setHeader(activity, "2-" + activity.getString(R.string.title_play));

        View rootView = inflater.inflate(R.layout.fragment_player_details, container, false);

        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                id = bundle.getString("id");
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

        img_right = (ImageView) activity.findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);

        profilePic = (ImageView) rootView.findViewById(R.id.img_profile);
        img_challenge = (ImageView) rootView.findViewById(R.id.img_challenge);
        img_chat = (ImageView) rootView.findViewById(R.id.img_chat);
        img_fav = (ImageView) rootView.findViewById(R.id.img_favorite);

        txt_advanced = (MyTextView) rootView.findViewById(R.id.advanced_text);
        txt_playerName = (MyTextView) rootView.findViewById(R.id.player_name_text);
        txt_occupation = (MyTextView) rootView.findViewById(R.id.occupation_text);
        txt_age = (MyTextView) rootView.findViewById(R.id.age_text);
        txt_description = (MyTextView) rootView.findViewById(R.id.description_text);

        gamesLayout = (LinearLayout) rootView.findViewById(R.id.games_layout);

        img_fav.setOnClickListener(this);
        img_chat.setOnClickListener(this);
        img_challenge.setOnClickListener(this);

        usersArrayList = ModelManager.getInstance().getUsersManager().getNearUsers(false);
        for (int i = 0; i < usersArrayList.size(); i++) {
            if (usersArrayList.get(i).getId().equalsIgnoreCase(id)) {
                Utils.showLoading(activity, activity.getString(R.string.please_wait));
                ModelManager.getInstance().getUsersManager().getNearUsers(false).get(i).getUserDetails(true, id);
                break;
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_favorite:
                if (is_Fav) {
                    is_Fav = false;
                    img_fav.setImageResource(R.drawable.fav);
                } else {
                    is_Fav = true;
                    img_fav.setImageResource(R.drawable.fav_on);
                }
                break;

            case R.id.img_challenge:

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("team_id", id);
                    jsonObject.put("user_id", ModelManager.getInstance().getAuthManager().getUserId());
                    jsonObject.put("status", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.showLoading(activity, activity.getString(R.string.please_wait));
//                ModelManager.getInstance().getTeamsManager().challengeTeam(jsonObject);

                break;

            case R.id.img_chat:
                break;
        }
    }

    private void setData() {
        usersArrayList = ModelManager.getInstance().getUsersManager().getNearUsers(false);
        for (int i = 0; i < usersArrayList.size(); i++) {
            if (usersArrayList.get(i).getId().equalsIgnoreCase(id)) {
                ArrayList<Users> userDetails = usersArrayList.get(i).getUserDetails(false, id);
                txt_playerName.setText(userDetails.get(0).getFirst_name());
                txt_age.setText(userDetails.get(0).getDob());
//                txt_occupation.setText(usersArrayList.get(i).get);
                txt_occupation.setVisibility(View.GONE);
                if (!Utils.isEmptyString(userDetails.get(0).getDescription())) {
                    txt_description.setText(userDetails.get(0).getDescription());
                    txt_description.setVisibility(View.VISIBLE);
                } else {
                    txt_description.setVisibility(View.GONE);
                }

                String img1 = "", img2 = "";

                img1 = usersArrayList.get(i).getImage();


                ArrayList<Sports> sportsArrayList = userDetails.get(0).getSports_preferences();
                if (sportsArrayList != null)
                    if (sportsArrayList.size() > 0) {
                        for (int j = 0; j < sportsArrayList.size(); j++) {
                            SPLog.e("Sports name : ", sportsArrayList.get(j).getName());
                            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v = vi.inflate(R.layout.row_sports_preferences, null);

                            MyTextView gameText1 = (MyTextView) v.findViewById(R.id.game_text1);
                            MyTextView gameText2 = (MyTextView) v.findViewById(R.id.game_text2);

                            gameText1.setText(sportsArrayList.get(j).getName());
                            j = j + 1;
                            if (sportsArrayList.size() > j)
                                gameText2.setText(sportsArrayList.get(j).getName());
                            else
                                gameText2.setVisibility(View.GONE);

                            gamesLayout.addView(v);
                        }
                        img2 = sportsArrayList.get(0).getName();
                    }

                if (!Utils.isEmptyString(img1)) {
                    Picasso.with(activity).load(ServiceApi.baseurl + img1)
                            .placeholder(R.drawable.players)
                            .into(profilePic);
                } else if (!Utils.isEmptyString(img2)) {
                    profilePic.setImageResource(DrawableImages.setImage(img2));
                }
                break;
            }
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
        if (message.equalsIgnoreCase("GetUserDetails True")) {
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("GetUserDetails False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetUserDetails Network Error")) {
            Utils.dismissLoading();
        }
    }
}
