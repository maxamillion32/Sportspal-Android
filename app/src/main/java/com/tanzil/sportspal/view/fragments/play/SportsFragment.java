package com.tanzil.sportspal.view.fragments.play;

/**
 * Created by Arun on 29/07/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Games;
import com.tanzil.sportspal.view.adapters.SportsFragmentAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class SportsFragment extends Fragment {

    private String TAG = SportsFragment.class.getSimpleName();
    private Activity activity;
    private SportsFragmentAdapter adapter;
    private ArrayList<Games> gamesArrayList;
    private ListView sportsListView;
    private ImageView img_right, img_delete;
    private LinearLayout header_layout, search_layout;
    private MyEditText et_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.activity = super.getActivity();
        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);

        header_layout = (LinearLayout) activity.findViewById(R.id.header_layout);
        search_layout = (LinearLayout) activity.findViewById(R.id.search_layout);
        img_delete = (ImageView) activity.findViewById(R.id.img_delete);
        img_right = (ImageView) activity.findViewById(R.id.img_right);
        img_right.setVisibility(View.VISIBLE);

        et_search = (MyEditText) activity.findViewById(R.id.et_search);

        sportsListView = (ListView) rootView.findViewById(R.id.news_feed_list);
        // Inflate the layout for this fragment

        gamesArrayList = ModelManager.getInstance().getSportsManager().getAllGames(false);
        if (gamesArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getSportsManager().getAllGames(true);
        } else
            setData();

        img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header_layout.setVisibility(View.GONE);
                search_layout.setVisibility(View.VISIBLE);
            }
        });

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                header_layout.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
                Utils.closeKeyboard(activity, et_search.getWindowToken());
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {

                    Utils.closeKeyboard(activity, et_search.getWindowToken());
                }
                return true;
            }
        });

        return rootView;
    }

    private void setData() {
        gamesArrayList = ModelManager.getInstance().getSportsManager().getAllGames(false);
        if (gamesArrayList.size() > 0) {
            adapter = new SportsFragmentAdapter(activity, gamesArrayList);
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
        if (message.equalsIgnoreCase("GetAllGames True")) {
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("GetAllGames False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetAllGames Network Error")) {
            Utils.dismissLoading();
        }
    }
}
