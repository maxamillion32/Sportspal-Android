package com.tanzil.sportspal.view.fragments.play;

/**
 * Created by Arun on 29/07/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Users;
import com.tanzil.sportspal.view.adapters.PlayersFragmentAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class PlayersFragment extends Fragment {

    private String TAG = PlayersFragment.class.getSimpleName();
    private Activity activity;
    private PlayersFragmentAdapter adapter;
    private ArrayList<Users> gamesArrayList;
    private ListView sportsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra(
                "message",
                "SP-"
                        + activity.getString(R.string.title_play));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);


        sportsListView = (ListView) rootView.findViewById(R.id.news_feed_list);
        // Inflate the layout for this fragment

        gamesArrayList = ModelManager.getInstance().getUsersManager().getNearUsers(false);
        if (gamesArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getUsersManager().getNearUsers(true);
        } else
            setData();

        sportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new PlayerDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", gamesArrayList.get(position).getId());
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment, "PlayerDetailFragment");
                fragmentTransaction.addToBackStack("PlayerDetailFragment");
                fragmentTransaction.commit();
            }
        });


        return rootView;
    }

    private void setData() {
        gamesArrayList = ModelManager.getInstance().getUsersManager().getNearUsers(false);
        if (gamesArrayList.size() > 0) {
            adapter = new PlayersFragmentAdapter(activity, gamesArrayList);
            sportsListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else
            Utils.showMessage(activity, "There is no record found");
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
        if (message.equalsIgnoreCase("GetNearUsers True")) {
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("GetNearUsers False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetNearUsers Network Error")) {
            Utils.dismissLoading();
        }
    }
}
