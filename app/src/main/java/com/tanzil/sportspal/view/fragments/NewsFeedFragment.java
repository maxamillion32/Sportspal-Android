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

        newsFeedUrls = new ArrayList<>();
        newsFeedUrls.add("http://economictimes.indiatimes.com/slideshows/biz-entrepreneurship/six-lessons-that-patanjali-teaches-indias-fmcg-sector/6-lessons-that-patanjali-teaches-indias-fmcg-sector/slideshow/51874418.cms");
        newsFeedUrls.add("https://www.youtube.com/watch?v=Nyu3380Id64");
        newsFeedUrls.add("http://news.nike.com/news/hyperadapt-adaptive-lacing");

        newsFeedDescription = new ArrayList<>();
        newsFeedDescription.add("Six lessons that Patanjali teaches India's FMCG sector");
        newsFeedDescription.add("DECATHLON City Square Mall Opening");
        newsFeedDescription.add("Nike HYPERADAPT 1.0 MANIFESTS THE UNIMAGINABLE");

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
    }

}
