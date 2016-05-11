package com.tanzil.sportspal.model;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.bean.Games;
import com.tanzil.sportspal.model.bean.Players;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.model.bean.Teams;
import com.tanzil.sportspal.model.bean.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/3/2016.
 */
public class UsersManager {
    private ArrayList<Users> usersList;
    private final String TAG = SportsManager.class.getSimpleName();

    public ArrayList<Users> getNearUsers(boolean shouldRefresh) {
        if (shouldRefresh)
            getUsers();
        return usersList;
    }

    private void getUsers() {
        SPRestClient.get(ServiceApi.GET_USERS + "/" + ModelManager.getInstance().getAuthManager().getUserId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetNearUsers called before request is started");
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
//                    "id": 2,
//                            "first_name": "14/9/2014",
//                            "last_name": "kinger",
//                            "email": "abc@gmail.com",
//                            "dob": "15/12/1999",
//                            "gender": "male",
//                            "modified": "2016-05-05T18:47:17+0000",
//                            "created": "2016-04-16T16:01:43+0000",
//                            "image": "",
//                            "social_platform": "",
//                            "social_id": "",
//                            "latitude": "30.7333",
//                            "longitude": "76.7794",
//                            "sports_preferences": [
//                    {
//                        "id": 17,
//                            "user_id": 2,
//                            "sport_id": 1,
//                            "created": null,
//                            "modified": null
//                    }
//                    ],
//                    "games": [
//                    {
//                        "id": 1,
//                            "sport_id": 1,
//                            "user_id": 2,
//                            "game_type": "indivisual",
//                            "team_id": 0,
//                            "date": "15/12/1999",
//                            "time": "10:20 PM",
//                            "latitude": "1234555",
//                            "longitude": "1234555",
//                            "address": "Android",
//                            "modified": "2016-04-30T05:58:35+0000",
//                            "created": "2016-04-30T05:58:35+0000"
//                    }
//                    ],
//                    "teams": [
//                    {
//                        "id": 1,
//                            "sport_id": 1,
//                            "team_name": "KKR",
//                            "team_type": "private",
//                            "members_limit": 11,
//                            "latitude": "1234555",
//                            "longitude": "1234555",
//                            "address": "Android",
//                            "creator_id": 2,
//                            "created": "2016-04-30T07:27:12+0000",
//                            "modified": "2016-04-30T07:27:12+0000"
//                    },
//                   ]
//                },
                    int state = response.getInt("status");
                    if (state == 200) {
                        JSONArray jsonArray = response.getJSONArray("message");
                        int count = jsonArray.length();
                        usersList = new ArrayList<Users>();
                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Users users = new Users();
                                users.setId(jsonArray.getJSONObject(i).getString("id"));
                                users.setFirst_name(jsonArray.getJSONObject(i).getString("first_name"));
                                users.setLast_name(jsonArray.getJSONObject(i).getString("last_name"));
                                users.setEmail(jsonArray.getJSONObject(i).getString("email"));
                                users.setDob(jsonArray.getJSONObject(i).getString("dob"));
                                users.setGender(jsonArray.getJSONObject(i).getString("gender"));
                                users.setImage(jsonArray.getJSONObject(i).getString("image"));
                                users.setSocial_platform(jsonArray.getJSONObject(i).getString("social_platform"));
                                users.setSocial_id(jsonArray.getJSONObject(i).getString("social_id"));
                                users.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                users.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));

                                if (jsonArray.getJSONObject(i).has("sports_preferences")) {
                                    JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("sports_preferences");
                                    if (jsonArray1 != null) {
                                        ArrayList<Sports> sportsArrayList = new ArrayList<Sports>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            Sports sports = new Sports();
                                            sports.setId(jsonArray1.getJSONObject(j).getString("sport_id"));
                                            sports.setUserId(jsonArray1.getJSONObject(j).getString("user_id"));
                                            sportsArrayList.add(sports);
                                        }
                                        users.setSports_preferences(sportsArrayList);
                                    }
                                }
                                if (jsonArray.getJSONObject(i).has("games")) {
                                    JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("games");
                                    if (jsonArray1 != null) {
                                        ArrayList<Games> gamesArrayList = new ArrayList<Games>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            Games games = new Games();
                                            games.setId(jsonArray1.getJSONObject(j).getString("id"));
                                            games.setSportsId(jsonArray1.getJSONObject(j).getString("sport_id"));
                                            games.setUserId(jsonArray1.getJSONObject(j).getString("user_id"));
                                            games.setGameType(jsonArray1.getJSONObject(j).getString("game_type"));
                                            games.setTeamId(jsonArray1.getJSONObject(j).getString("team_id"));
                                            games.setDate(jsonArray1.getJSONObject(j).getString("date"));
                                            games.setTime(jsonArray1.getJSONObject(j).getString("time"));
                                            games.setLatitude(jsonArray1.getJSONObject(j).getString("latitude"));
                                            games.setLongitude(jsonArray1.getJSONObject(j).getString("longitude"));
                                            games.setAddress(jsonArray1.getJSONObject(j).getString("address"));
                                            gamesArrayList.add(games);
                                        }
                                        users.setGamesArrayList(gamesArrayList);
                                    }
                                }
                                if (jsonArray.getJSONObject(i).has("teams")) {
                                    JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("teams");
                                    if (jsonArray1 != null) {
                                        ArrayList<Teams> teamsArrayList = new ArrayList<Teams>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            Teams teams = new Teams();
                                            teams.setId(jsonArray1.getJSONObject(j).getString("id"));
                                            teams.setSport_id(jsonArray1.getJSONObject(j).getString("sport_id"));
                                            teams.setTeam_name(jsonArray1.getJSONObject(j).getString("team_name"));
                                            teams.setTeam_type(jsonArray1.getJSONObject(j).getString("team_type"));
                                            teams.setMembers_limit(jsonArray1.getJSONObject(j).getString("members_limit"));
                                            teams.setLatitude(jsonArray1.getJSONObject(j).getString("latitude"));
                                            teams.setLongitude(jsonArray1.getJSONObject(j).getString("longitude"));
                                            teams.setAddress(jsonArray1.getJSONObject(j).getString("address"));
                                            teamsArrayList.add(teams);
                                        }
                                        users.setTeamsArrayList(teamsArrayList);
                                    }
                                }
                                usersList.add(users);
                            }
                        EventBus.getDefault().post("GetNearUsers True");
                    } else {
                        EventBus.getDefault().post("GetNearUsers False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetNearUsers False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetNearUsers False");
                } else {
                    EventBus.getDefault().post("GetNearUsers Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("GetNearUsers False");
                } else {
                    EventBus.getDefault().post("GetNearUsers Network Error");
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
