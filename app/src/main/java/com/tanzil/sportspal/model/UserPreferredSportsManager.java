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
    private String message, sport_id;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSport_id() {
        return sport_id;
    }

    public void setSport_id(String sport_id) {
        this.sport_id = sport_id;
    }

    private ArrayList<Sports> getUserPreferredSportsList(boolean shouldRefresh, String auth_token, String UserId) {
        if (shouldRefresh)
            getPreferredSports(auth_token, UserId);
        return userSportsList;
    }
    private void getPreferredSports(String auth_token, String UserId) {
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
                                sports.setId(jsonArray.getJSONObject(i).getString("sport_id"));
//                                sports.setUserId(jsonArray.getJSONObject(i).getString("sport_id"));
                                sports.setName(jsonArray.getJSONObject(i).getJSONObject("Sports").getString("name"));
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
    private void addUserPreferredSport(JSONObject jsonObject) {
        SPRestClient.post(ServiceApi.ADD_USER_SPORT, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "AddUserPreferredSport called before request is started");
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
                        setMessage(response.getString("message"));
                        EventBus.getDefault().post("AddUserPreferredSport True");
                    } else {
                        EventBus.getDefault().post("AddUserPreferredSport False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("AddUserPreferredSport False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("AddUserPreferredSport False");
                } else {
                    EventBus.getDefault().post("AddUserPreferredSport Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("AddUserPreferredSport False");
                } else {
                    EventBus.getDefault().post("AddUserPreferredSport Network Error");
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

    public void addPreferences(JSONObject jsonObject) {
        SPRestClient.post(ServiceApi.ADD_USER_PREFERENCE, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "AddPreferences called before request is started");
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
                        setMessage(response.getString("message"));
                        EventBus.getDefault().post("AddPreferences True");
                    } else {
                        EventBus.getDefault().post("AddPreferences False");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("AddPreferences False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("AddPreferences False");
                } else {
                    EventBus.getDefault().post("AddPreferences Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("AddPreferences False");
                } else {
                    EventBus.getDefault().post("AddPreferences Network Error");
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
