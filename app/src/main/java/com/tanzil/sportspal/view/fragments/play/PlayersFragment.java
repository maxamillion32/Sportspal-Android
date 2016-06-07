package com.tanzil.sportspal.view.fragments.play;

/**
 * Created by Arun on 29/07/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Users;
import com.tanzil.sportspal.view.adapters.PlayersFragmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class PlayersFragment extends Fragment {

    private String TAG = PlayersFragment.class.getSimpleName();
    private Activity activity;
    private PlayersFragmentAdapter adapter;
    private ArrayList<Users> gamesArrayList;
    private ListView sportsListView;
    private ImageView img_right, img_delete;
    private LinearLayout header_layout, search_layout;
    private MyEditText et_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.activity = super.getActivity();

        Utils.setHeader(activity, "2-" + activity.getString(R.string.title_play));

        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);

        header_layout = (LinearLayout) activity.findViewById(R.id.header_layout);
        search_layout = (LinearLayout) activity.findViewById(R.id.search_layout);
        img_delete = (ImageView) activity.findViewById(R.id.img_delete);
        img_right = (ImageView) activity.findViewById(R.id.img_right);
        img_right.setVisibility(View.VISIBLE);

        et_search = (MyEditText) activity.findViewById(R.id.et_search);


        sportsListView = (ListView) rootView.findViewById(R.id.news_feed_list);
        // Inflate the layout for this fragment

//        gamesArrayList = ModelManager.getInstance().getUsersManager().getNearUsers(false);
//        if (gamesArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getUsersManager().getNearUsers(true);
//        } else
//            setData();

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
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_id", ModelManager.getInstance().getAuthManager().getUserId());
                        jsonObject.put("is_preferred", 1);
                        jsonObject.put("keyword", et_search.getText().toString().trim());
                        jsonObject.put("is_keyword", 1);
                        jsonObject.put("is_nearby", 0);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    Utils.showLoading(activity, activity.getString(R.string.please_wait));
                    ModelManager.getInstance().getUsersManager().getSearchUsers(true, jsonObject);
                    Utils.closeKeyboard(activity, et_search.getWindowToken());
                }
                return true;
            }
        });

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
        search_layout.setVisibility(View.GONE);
        img_right.setVisibility(View.INVISIBLE);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        search_layout.setVisibility(View.GONE);
        img_right.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }

    public void onEventMainThread(String message) {
        Log.e(TAG, "-- " + message);
        if (message.equalsIgnoreCase("GetNearUsers True")) {
            Utils.dismissLoading();
            gamesArrayList = ModelManager.getInstance().getUsersManager().getNearUsers(false);
            setData();
        } else if (message.equalsIgnoreCase("GetNearUsers False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetNearUsers Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetSearchUsers True")) {
            Utils.dismissLoading();
            gamesArrayList = ModelManager.getInstance().getUsersManager().getSearchUsers(false, null);
            setData();
        } else if (message.equalsIgnoreCase("GetSearchUsers False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetSearchUsers Network Error")) {
            Utils.dismissLoading();
        }
    }
}
