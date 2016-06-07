package com.tanzil.sportspal.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.DrawableImages;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyButton;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.GameChallenges;
import com.tanzil.sportspal.model.bean.GameNotifications;
import com.tanzil.sportspal.model.bean.Users;
import com.tanzil.sportspal.view.fragments.play.PlayerDetailFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/25/2016.
 */
public class GameNotificationDetailFragment extends Fragment {

    private String TAG = PlayerDetailFragment.class.getSimpleName();
    private Activity activity;
    private ListView memberList;
    private ArrayList<GameNotifications> gamesArrayList;
    private String id = "", spName = "", challenge_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Utils.setHeader(activity, "2-" + activity.getString(R.string.game));

        View rootView = inflater.inflate(R.layout.fragment_game_details, container, false);

        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                id = bundle.getString("id");
                spName = bundle.getString("name");
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

        ImageView img_right = (ImageView) activity.findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);

        memberList = (ListView) rootView.findViewById(R.id.memberList);

        gamesArrayList = ModelManager.getInstance().getNotificationsManager().getGameChallengesNotifications(false);
        if (gamesArrayList != null)
            setData();

        return rootView;
    }

    private void setData() {
        SPLog.e("List Size : ", "" + gamesArrayList.size());
        gamesArrayList = ModelManager.getInstance().getNotificationsManager().getGameChallengesNotifications(false);
        for (int i = 0; i < gamesArrayList.size(); i++) {
            if (gamesArrayList.get(i).getId().equalsIgnoreCase(id)) {
//                ArrayList<Games> gameDetails = gamesArrayList.get(i).getGameDetails(false, id);

                View headerView = View
                        .inflate(activity, R.layout.game_detail_header_layout, null);

                ImageView sportsPic = (ImageView) headerView.findViewById(R.id.img_sports_pic);

                MyTextView txt_address = (MyTextView) headerView.findViewById(R.id.txt_pick_address);
                MyTextView txt_sportName = (MyTextView) headerView.findViewById(R.id.sport_name_text);
                MyTextView txt_teamName = (MyTextView) headerView.findViewById(R.id.txt_team_name);
                MyTextView txt_date = (MyTextView) headerView.findViewById(R.id.txt_date);
                MyTextView txt_time = (MyTextView) headerView.findViewById(R.id.txt_time);
                MyTextView txt_members = (MyTextView) headerView.findViewById(R.id.txt_members);
                MyTextView txt_memberSize = (MyTextView) headerView.findViewById(R.id.txt_member_size);

                if (!Utils.isEmptyString(spName))
                    sportsPic.setImageResource(DrawableImages.setImage(spName));

                MyButton btn_join = (MyButton) headerView.findViewById(R.id.join_btn);
                btn_join.setTransformationMethod(null);
                btn_join.setVisibility(View.GONE);


                txt_address.setText(gamesArrayList.get(i).getAddress());
                txt_sportName.setText(gamesArrayList.get(i).getSports_name());
                txt_teamName.setText(gamesArrayList.get(i).getName());
                txt_time.setText(gamesArrayList.get(i).getTime());
                txt_date.setText(gamesArrayList.get(i).getDate());
                txt_memberSize.setText("MAX SIZE(11)");

                if (!Utils.isEmptyString(gamesArrayList.get(i).getSports_name()))
                    sportsPic.setImageResource(DrawableImages.setImage(gamesArrayList.get(0).getSports_name()));

                memberList.addHeaderView(headerView);

                ArrayList<GameChallenges> gameChallengesArrayList = gamesArrayList.get(i).getUserGamesList();
                if (gameChallengesArrayList == null)
                    gameChallengesArrayList = new ArrayList<>();
                SPLog.e("gameChallengesArrayList Array List : ", "" + gameChallengesArrayList.size());

                ArrayList<Users> usersArrayList = new ArrayList<Users>();
                if (gameChallengesArrayList.size() > 0)
                    for (int j = 0; j < gameChallengesArrayList.size(); j++) {
                        if (gameChallengesArrayList.get(j).getGameId().equalsIgnoreCase(gamesArrayList.get(i).getId())) {
                            challenge_id = gameChallengesArrayList.get(j).getId();
                            usersArrayList.add(gameChallengesArrayList.get(j).getUsers());
                        }
                    }

                SPLog.e("User Array List : ", "" + usersArrayList.size());
                ChallengeMembersListAdapter adapter = new ChallengeMembersListAdapter(activity, usersArrayList);
                memberList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                txt_members.setText("Members(" + usersArrayList.size() + ")");
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
        if (message.equalsIgnoreCase("GetGameDetails True")) {
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("GetGameDetails False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetGameDetails Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("JoinGame True")) {
            Utils.dismissLoading();
            Toast.makeText(activity, "Game joined successfully", Toast.LENGTH_SHORT).show();
        } else if (message.equalsIgnoreCase("JoinGame False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("JoinGame Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("AcceptTeamRequest True")) {
            Utils.dismissLoading();
            ((FragmentActivity) activity)
                    .getSupportFragmentManager().popBackStack();
            Toast.makeText(activity, "You accepted the challenge successfully", Toast.LENGTH_SHORT).show();
        } else if (message.equalsIgnoreCase("AcceptGameRequest False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("AcceptGameRequest Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("RejectGameRequest True")) {
            Utils.dismissLoading();
            ((FragmentActivity) activity)
                    .getSupportFragmentManager().popBackStack();
            Toast.makeText(activity, "You have rejected the challenge successfully", Toast.LENGTH_SHORT).show();
        } else if (message.equalsIgnoreCase("RejectGameRequest False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("RejectGameRequest Network Error")) {
            Utils.dismissLoading();
        }
    }

    public class ChallengeMembersListAdapter extends BaseAdapter {
        private ArrayList<Users> list;
        private Activity activity;

        public ChallengeMembersListAdapter(final Activity context,
                                           ArrayList<Users> list) {
            this.list = list;
            this.activity = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @SuppressLint({"InflateParams", "NewApi"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = convertView;
            final CompleteListViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.row_challenge_members_list, null);

                viewHolder = new CompleteListViewHolder(v);
                v.setTag(viewHolder);
            } else {
                viewHolder = (CompleteListViewHolder) v.getTag();
            }

            try {
                SPLog.e("Name :", "" + list.get(position).getFirst_name());
                viewHolder.userNameText.setText(list.get(position).getFirst_name());

                viewHolder.userPic.setBorderColor(Utils.setColor(activity, R.color.white));
                viewHolder.userPic.setSelectorColor(Utils.setColor(activity, R.color.circular_image_border_color));
                viewHolder.userPic.setBorderWidth(5);
                viewHolder.userPic.setSelectorStrokeWidth(5);
                viewHolder.userPic.addShadow();
                if (!Utils.isEmptyString(list.get(position).getImage()))
                    Picasso.with(activity)
                            .load(ServiceApi.baseurl + list.get(position).getImage())
                            .placeholder(R.drawable.customer_img)
                            .error(R.drawable.customer_img)
                            .into(viewHolder.userPic);
                else
                    viewHolder.userPic.setImageResource(R.drawable.customer_img);

                viewHolder.accept.setTransformationMethod(null);
                viewHolder.reject.setTransformationMethod(null);

                viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendRequest(0);
                    }
                });

                viewHolder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendRequest(1);
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return v;
        }

        class CompleteListViewHolder {
            public MyTextView userNameText;
            public CircularImageView userPic;
            public ImageView sportsType;
            public MyButton accept, reject;

            public CompleteListViewHolder(View convertview) {
                userNameText = (MyTextView) convertview
                        .findViewById(R.id.txt_user_name);
                userPic = (CircularImageView) convertview
                        .findViewById(R.id.img_user_pic);
                sportsType = (ImageView) convertview
                        .findViewById(R.id.img_sport_type);
                accept = (MyButton) convertview.findViewById(R.id.txt_accept);
                reject = (MyButton) convertview.findViewById(R.id.txt_reject);
            }
        }
    }

    private void sendRequest(int type) {
        for (int i = 0; i < gamesArrayList.size(); i++) {
            if (gamesArrayList.get(i).getId().equalsIgnoreCase(id)) {
                if (type == 0) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("challenge_id", challenge_id);
                        jsonObject.put("user_id", ModelManager.getInstance().getAuthManager().getUserId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Utils.showLoading(activity, activity.getString(R.string.please_wait));
                    gamesArrayList.get(i).acceptGameRequest(jsonObject);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("challenge_id", challenge_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Utils.showLoading(activity, activity.getString(R.string.please_wait));
                    gamesArrayList.get(i).rejectGameRequest(jsonObject);
                }
            }
        }
    }
}