package com.tanzil.sportspal.model.bean;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.SportsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/3/2016.
 */
public class Games {

    private final String TAG = SportsManager.class.getSimpleName();
    String id;
    String name;
    String status;
    String userId;
    String sportsId;
    String gameType;
    String teamId;
    String date, time, latitude, longitude, address, sports_name, user_first_name, user_last_name, user_email;
    private ArrayList<Games> userGamesList;
    private ArrayList<Users> usersArrayList;

    public ArrayList<Users> getUsersArrayList() {
        return usersArrayList;
    }

    public void setUsersArrayList(ArrayList<Users> usersArrayList) {
        this.usersArrayList = usersArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSportsId() {
        return sportsId;
    }

    public void setSportsId(String sportsId) {
        this.sportsId = sportsId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSports_name() {
        return sports_name;
    }

    public void setSports_name(String sports_name) {
        this.sports_name = sports_name;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public String getUser_last_name() {
        return user_last_name;
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name = user_last_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public ArrayList<Games> getGameDetails(boolean shouldRefresh, String id) {
        if (shouldRefresh)
            getDetails(id);
        return userGamesList;
    }

    public ArrayList<Games> getDetails(String id) {
        SPRestClient.get(ServiceApi.GET_GAME_DETAIL + "/" + id, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetGameDetails called before request is started");
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
                        JSONObject jsonArray = response.getJSONObject("message");

                        Games.this.setId(jsonArray.getString("id"));
                        Games.this.setName(jsonArray.getString("name"));
                        Games.this.setSportsId(jsonArray.getString("sport_id"));
                        Games.this.setUserId(jsonArray.getString("user_id"));
                        Games.this.setGameType(jsonArray.getString("game_type"));
                        Games.this.setTeamId(jsonArray.getString("team_id"));
                        Games.this.setDate(jsonArray.getString("date"));
                        Games.this.setTime(jsonArray.getString("time"));
                        Games.this.setLatitude(jsonArray.getString("latitude"));
                        Games.this.setLongitude(jsonArray.getString("longitude"));
                        Games.this.setAddress(jsonArray.getString("address"));

                        if (jsonArray.has("sport"))
                            Games.this.setSports_name(jsonArray.getJSONObject("sport").getString("name"));
                        if (jsonArray.has("user")) {
                            Games.this.setUser_first_name(jsonArray.getJSONObject("user").getString("first_name"));
                            Games.this.setUser_last_name(jsonArray.getJSONObject("user").getString("last_name"));
                            Games.this.setUser_email(jsonArray.getJSONObject("user").getString("email"));
                        }

                        userGamesList.add(Games.this);
//                            }
                        EventBus.getDefault().post("GetGameDetails True");
                    } else {
                        EventBus.getDefault().post("GetGameDetails False");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("GetGameDetails False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetGameDetails False");
                } else {
                    EventBus.getDefault().post("GetGameDetails Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("GetGameDetails False");
                } else {
                    EventBus.getDefault().post("GetGameDetails Network Error");
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
        return userGamesList;
    }

}


