package com.tanzil.sportspal.view.fragments.play;

/**
 * Created by Arun on 29/07/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.view.adapters.NewsFeedAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class SportsFragment extends Fragment {

    private String TAG = SportsFragment.class.getSimpleName();
    private Activity activity;
    private NewsFeedAdapter adapter;
    private ArrayList<Sports> sportsArrayList;
    private ListView newsFeedListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.activity = super.getActivity();
        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);


        newsFeedListView = (ListView) rootView.findViewById(R.id.news_feed_list);
        // Inflate the layout for this fragment

        sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false, "");
        if (sportsArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getSportsManager().getAllSportsList(true, "");
        } else
            setData();


        return rootView;
    }

    private void setData() {
        sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false, "");
        if (sportsArrayList.size() > 0) {
            adapter = new NewsFeedAdapter(activity, sportsArrayList);
            newsFeedListView.setAdapter(adapter);
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
        if (message.equalsIgnoreCase("GetAllSports True")) {
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("GetAllSports False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetAllSports Network Error")) {
            Utils.dismissLoading();
        }
    }
}
