package com.tanzil.sportspal.view.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.view.adapters.NewsFeedAdapter;

import java.util.ArrayList;


public class NewsFeedFragment extends Fragment {

    //    private String TAG = NewsFeedFragment.class.getSimpleName();
    private Activity activity;
    //    private NewsFeedAdapter adapter;
    //    private ArrayList<Sports> sportsArrayList;
    private ArrayList<String> newsFeedUrls;
    private ArrayList<String> newsFeedDescription;
    private ListView newsFeedListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra(
                "message",
                "SP-"
                        + activity.getString(R.string.title_news));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);


        newsFeedListView = (ListView) rootView.findViewById(R.id.news_feed_list);
        // Inflate the layout for this fragment

//        sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
//        if (sportsArrayList == null) {
//            Utils.showLoading(activity, activity.getString(R.string.please_wait));
//            ModelManager.getInstance().getSportsManager().getAllSportsList(true);
//        } else
        newsFeedUrls = new ArrayList<>();
        newsFeedUrls.add("http://patanjalivaidya.blogspot.in/");
        newsFeedUrls.add("https://www.decathlon.in/blog");
        newsFeedUrls.add("http://news.nike.com/");

        newsFeedDescription = new ArrayList<>();
        newsFeedDescription.add("http://patanjalivaidya.blogspot.in/");
        newsFeedDescription.add("https://www.decathlon.in/blog");
        newsFeedDescription.add("http://news.nike.com/");

        setData();

        newsFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new BrowserLinkFragment();
                Bundle bundle = new Bundle();
                bundle.putString("link", newsFeedUrls.get(position));
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment, "BrowserLinkFragment");
                fragmentTransaction.addToBackStack("BrowserLinkFragment");
                fragmentTransaction.commit();

            }
        });


        return rootView;
    }

    private void setData() {
        NewsFeedAdapter adapter = new NewsFeedAdapter(activity, newsFeedDescription);
        newsFeedListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
//        if (sportsArrayList.size() > 0) {
//            adapter = new NewsFeedAdapter(activity, sportsArrayList);
//            newsFeedListView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        } else
//            Utils.showMessage(activity, "There is no record found");
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        //gpDatabase.setConversation(chatManager.getConversations());
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    public void onEventMainThread(String message) {
//        Log.e(TAG, "-- " + message);
//        if (message.equalsIgnoreCase("GetAllSports True")) {
//            Utils.dismissLoading();
//            setData();
//        } else if (message.equalsIgnoreCase("GetAllSports False")) {
//            Utils.dismissLoading();
//        } else if (message.equalsIgnoreCase("GetAllSports Network Error")) {
//            Utils.dismissLoading();
//        }
//    }
}
