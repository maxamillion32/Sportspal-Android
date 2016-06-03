package com.tanzil.sportspal.view.fragments.play;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.tanzil.sportspal.model.bean.Games;
import com.tanzil.sportspal.model.bean.Users;
import com.tanzil.sportspal.view.adapters.MembersListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/25/2016.
 */
public class SportsDetailFragment extends Fragment {

    private String TAG = PlayerDetailFragment.class.getSimpleName();
    private Activity activity;
    private MyButton btn_join;
    private ListView memberList;
    private ArrayList<Games> gamesArrayList;
    private String id = "", spName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Utils.setHeader(activity, "2-" + activity.getString(R.string.title_play));

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
        setHeader();

        gamesArrayList = ModelManager.getInstance().getSportsManager().getUserPreferredGames(false);
        for (int i = 0; i < gamesArrayList.size(); i++) {
            if (gamesArrayList.get(i).getId().equalsIgnoreCase(id)) {
                Utils.showLoading(activity, activity.getString(R.string.please_wait));
                ModelManager.getInstance().getSportsManager().getUserPreferredGames(false).get(i).getGameDetails(true, id);
                break;
            }
        }
        return rootView;
    }

    private void setHeader() {
    }

    private void setData() {
        gamesArrayList = ModelManager.getInstance().getSportsManager().getUserPreferredGames(false);
        for (int i = 0; i < gamesArrayList.size(); i++) {
            if (gamesArrayList.get(i).getId().equalsIgnoreCase(id)) {
                ArrayList<Games> gameDetails = gamesArrayList.get(i).getGameDetails(false, id);

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

                btn_join = (MyButton) headerView.findViewById(R.id.join_btn);
                btn_join.setTransformationMethod(null);

                if (gameDetails.get(0).getUserId().equalsIgnoreCase(ModelManager.getInstance().getAuthManager().getUserId()))
                    btn_join.setVisibility(View.GONE);
                else
                    btn_join.setVisibility(View.VISIBLE);

                btn_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  accepting the invitation to join the game
//                        Toast.makeText(activity, "Coming soon! Stay in Touch.", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("sport_id", id);
                            jsonObject.put("user_id", ModelManager.getInstance().getAuthManager().getUserId());
                            jsonObject.put("status", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Utils.showLoading(activity, activity.getString(R.string.please_wait));
                        ModelManager.getInstance().getSportsManager().joinGame(jsonObject);
                    }
                });

                txt_address.setText(gameDetails.get(0).getAddress());
                txt_sportName.setText(gameDetails.get(0).getSports_name());
                txt_teamName.setText(gameDetails.get(0).getName());
                txt_time.setText(gameDetails.get(0).getTime());
                txt_date.setText(gameDetails.get(0).getDate());
                txt_memberSize.setText("MAX SIZE(11)");

                if (!Utils.isEmptyString(gameDetails.get(0).getSports_name()))
                    sportsPic.setImageResource(DrawableImages.setImage(gameDetails.get(0).getSports_name()));

                memberList.addHeaderView(headerView);

                ArrayList<Users> usersArrayList = gameDetails.get(0).getUsersArrayList();
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
        if (message.equalsIgnoreCase("GetGameDetails True")) {
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("GetGameDetails False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetGameDetails Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("JoinGame True")) {
            Utils.dismissLoading();
            btn_join.setVisibility(View.INVISIBLE);
            Toast.makeText(activity, "Game joined successfully", Toast.LENGTH_SHORT).show();
        } else if (message.equalsIgnoreCase("JoinGame False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("JoinGame Network Error")) {
            Utils.dismissLoading();
        }
    }
}