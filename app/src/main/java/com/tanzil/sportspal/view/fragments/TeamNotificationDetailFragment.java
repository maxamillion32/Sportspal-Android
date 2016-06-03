package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.DrawableImages;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyButton;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Teams;
import com.tanzil.sportspal.model.bean.Users;
import com.tanzil.sportspal.view.adapters.MembersListAdapter;
import com.tanzil.sportspal.view.fragments.play.PlayerDetailFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/25/2016.
 */
public class TeamNotificationDetailFragment extends Fragment {

    private String TAG = PlayerDetailFragment.class.getSimpleName();
    private Activity activity;
    //    private ImageView sportsPic, img_right, img_challenge;
//    private MyTextView txt_sportName, txt_teamName, txt_team_type, txt_members, txt_memberSize;
//    private MyButton btn_join;
    private ListView memberList;
    private ArrayList<Teams> teamsArrayList;
    private String id = "", request_id = "";
//    private View headerView, footerView;
//    private MembersListAdapter adapter;
//    private ArrayList<Users> usersArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Utils.setHeader(activity, "2-" + activity.getString(R.string.teams));

        View rootView = inflater.inflate(R.layout.fragment_team_details, container, false);

        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                id = bundle.getString("id");
            }

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

        ImageView img_right = (ImageView) activity.findViewById(R.id.img_right);
        img_right.setVisibility(View.INVISIBLE);

        memberList = (ListView) rootView.findViewById(R.id.team_memberList);

        teamsArrayList = ModelManager.getInstance().getTeamsManager().getAllTeams(false);
        if (teamsArrayList != null)
            for (int i = 0; i < teamsArrayList.size(); i++) {
                if (teamsArrayList.get(i).getId().equalsIgnoreCase(id)) {
                    Utils.showLoading(activity, activity.getString(R.string.please_wait));
                    ModelManager.getInstance().getTeamsManager().getAllTeams(false).get(i).getTeamDetails(true, id);
                    break;
                }
            }
        else {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getTeamsManager().getAllTeams(true);
        }
        return rootView;
    }


    private void setData() {
        memberList.setAdapter(null);
        teamsArrayList = ModelManager.getInstance().getTeamsManager().getAllTeams(false);
        for (int i = 0; i < teamsArrayList.size(); i++) {
            if (teamsArrayList.get(i).getId().equalsIgnoreCase(id)) {
                ArrayList<Teams> teamDetails = teamsArrayList.get(i).getTeamDetails(false, id);

                // header view starts
                View headerView = View
                        .inflate(activity, R.layout.team_detail_header_layout, null);

                ImageView sportsPic = (ImageView) headerView.findViewById(R.id.img_team_pic);

                MyTextView txt_sportName = (MyTextView) headerView.findViewById(R.id.txt_sports_name);
                MyTextView txt_teamName = (MyTextView) headerView.findViewById(R.id.team_name_text);
                MyTextView txt_team_type = (MyTextView) headerView.findViewById(R.id.txt_team_type);
                MyTextView txt_members = (MyTextView) headerView.findViewById(R.id.txt_members);
                MyTextView txt_memberSize = (MyTextView) headerView.findViewById(R.id.txt_member_size);

                ImageView img_challenge = (ImageView) headerView.findViewById(R.id.img_challenge);
                ImageView img_chat = (ImageView) headerView.findViewById(R.id.img_chat);
                img_challenge.setVisibility(View.INVISIBLE);
                img_chat.setVisibility(View.INVISIBLE);


                MyButton btn_join = (MyButton) headerView.findViewById(R.id.join_btn);
                btn_join.setTransformationMethod(null);
                btn_join.setVisibility(View.GONE);


                txt_sportName.setText(teamDetails.get(0).getSports_name());
                txt_teamName.setText(teamDetails.get(0).getTeam_name());
                txt_team_type.setText(teamDetails.get(0).getTeam_type());
                txt_memberSize.setText("MAX SIZE(11)");

                if (!Utils.isEmptyString(teamDetails.get(0).getSports_name()))
                    sportsPic.setImageResource(DrawableImages.setImage(teamDetails.get(0).getSports_name()));

                memberList.addHeaderView(headerView);

                request_id = teamDetails.get(0).getRequest_id();

                // footer view starts
                View footerView = View
                        .inflate(activity, R.layout.footer_accept_decline, null);
                MyButton acceptButton = (MyButton) footerView.findViewById(R.id.txt_accept);
                MyButton rejectButton = (MyButton) footerView.findViewById(R.id.txt_reject);

                acceptButton.setTransformationMethod(null);
                rejectButton.setTransformationMethod(null);

                final int pos = i;
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("status", "1");
                            jsonObject.put("request_id", request_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Utils.showLoading(activity, activity.getString(R.string.please_wait));
                        teamsArrayList.get(pos).acceptTeamRequest(jsonObject);
                    }
                });
                rejectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("request_id", request_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Utils.showLoading(activity, activity.getString(R.string.please_wait));
                        teamsArrayList.get(pos).rejectTeamRequest(jsonObject);
                    }
                });

                memberList.addFooterView(footerView);

                // users list section starts
                ArrayList<Users> usersArrayList = teamDetails.get(0).getUsersList();
                if (usersArrayList == null)
                    usersArrayList = new ArrayList<>();
                SPLog.e("User Array List : ", "" + usersArrayList.size());
                MembersListAdapter adapter = new MembersListAdapter(activity, usersArrayList);
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
        if (message.equalsIgnoreCase("GetAllTeams True")) {
            Utils.dismissLoading();
            teamsArrayList = ModelManager.getInstance().getTeamsManager().getAllTeams(false);
            for (int i = 0; i < teamsArrayList.size(); i++) {
                if (teamsArrayList.get(i).getId().equalsIgnoreCase(id)) {
                    Utils.showLoading(activity, activity.getString(R.string.please_wait));
                    ModelManager.getInstance().getTeamsManager().getAllTeams(false).get(i).getTeamDetails(true, id);
                    break;
                }
            }
        } else if (message.equalsIgnoreCase("GetAllTeams False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetAllTeams Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetTeamDetails True")) {
            Utils.dismissLoading();
            setData();
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
        } else if (message.equalsIgnoreCase("JoinTeam Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("ChallengeTeam True")) {
            Utils.dismissLoading();
//            img_challenge.setVisibility(View.INVISIBLE);
            Toast.makeText(activity, "You have challenged the team successfully", Toast.LENGTH_SHORT).show();
        } else if (message.equalsIgnoreCase("ChallengeTeam False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("ChallengeTeam Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("AcceptTeamRequest True")) {
            Utils.dismissLoading();
            ((FragmentActivity) activity)
                    .getSupportFragmentManager().popBackStack();
            Toast.makeText(activity, "You have accepted the invite successfully", Toast.LENGTH_SHORT).show();
        } else if (message.equalsIgnoreCase("AcceptTeamRequest False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("AcceptTeamRequest Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("RejectTeamRequest True")) {
            Utils.dismissLoading();
            ((FragmentActivity) activity)
                    .getSupportFragmentManager().popBackStack();
            Toast.makeText(activity, "You have rejected the invite successfully", Toast.LENGTH_SHORT).show();
        } else if (message.equalsIgnoreCase("RejectTeamRequest False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("RejectTeamRequest Network Error")) {
            Utils.dismissLoading();
        }
    }
}