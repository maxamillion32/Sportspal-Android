package com.tanzil.sportspal.model;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.bean.Players;
import com.tanzil.sportspal.model.bean.Sports;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/3/2016.
 */
public class PlayersManager {
    private ArrayList<Players> sportsList;
    private final String TAG = SportsManager.class.getSimpleName();
    public ArrayList<Players> getAllPlayers(boolean shouldRefresh) {
        if (shouldRefresh)
            getPlayers();
        return sportsList;
    }

    private void getPlayers() {
        SPRestClient.get(ServiceApi.GET_ALL_SPORTS, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetAllPlayers called before request is started");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                Log.e(TAG, "onCancel  --> ");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e(TAG, "onSuccess  --> " + response.toString());

                try {
                    int state = response.getInt("status");
                    if (state == 200) {
                        JSONArray jsonArray = response.getJSONArray("message");
                        int count = jsonArray.length();
                        sportsList = new ArrayList<Players>();
                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Players sports = new Players();
                                sports.setId(jsonArray.getJSONObject(i).getString("id"));
                                sports.setName(jsonArray.getJSONObject(i).getString("name"));
//                                sports.setSports_type(jsonArray.getJSONObject(i).getString("type"));
                                sportsList.add(sports);
                            }
                        EventBus.getDefault().post("GetAllPlayers True");
                    } else {
                        EventBus.getDefault().post("GetAllPlayers False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetAllPlayers False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetAllPlayers False");
                } else {
                    EventBus.getDefault().post("GetAllPlayers Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("GetAllSports False");
                } else {
                    EventBus.getDefault().post("GetAllSports Network Error");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.e(TAG, "onFinish  --> ");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e(TAG, "onRetry  --> ");
            }

        });
    }

}
