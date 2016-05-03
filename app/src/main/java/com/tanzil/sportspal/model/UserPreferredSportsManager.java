package com.tanzil.sportspal.model;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
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
public class UserPreferredSportsManager {

    private ArrayList<Sports> userSportsList;
    private final String TAG = UserPreferredSportsManager.class.getSimpleName();

    private ArrayList<Sports> getUserSportsList(boolean shouldRefresh, String auth_token, String UserId) {
        if (shouldRefresh)
            getSports(auth_token, UserId);
        return userSportsList;
    }
    private void getSports(String auth_token, String UserId) {
        SPRestClient.get(ServiceApi.GET_USER_SPORTS + UserId, auth_token, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetUserSports called before request is started");
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
                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Sports sports = new Sports();
                                sports.setId(jsonArray.getJSONObject(i).getString("id"));
                                sports.setUserId(jsonArray.getJSONObject(i).getString("id"));
                                sports.setName(jsonArray.getJSONObject(i).getJSONObject("Sports").getString("name"));
                                sports.setStatus(jsonArray.getJSONObject(i).getString("id"));
                            }
                        EventBus.getDefault().post("GetUserSports True");
                    } else {
                        EventBus.getDefault().post("GetUserSports False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetUserSports False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetUserSports False");
                } else {
                    EventBus.getDefault().post("GetUserSports Network Error");
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
