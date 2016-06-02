package com.tanzil.sportspal.model;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.bean.GameChallenges;
import com.tanzil.sportspal.model.bean.GameNotifications;
import com.tanzil.sportspal.model.bean.TeamNotifications;
import com.tanzil.sportspal.model.bean.Teams;
import com.tanzil.sportspal.model.bean.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 6/1/2016.
 */
public class NotificationsManager {

    private final String TAG = NotificationsManager.class.getSimpleName();
    private ArrayList<GameNotifications> gameChallengesNotificationList;
    private ArrayList<TeamNotifications> teamChallengesNotificationList;

    public ArrayList<GameNotifications> getGameChallengesNotifications(boolean shouldRefresh) {
        if (shouldRefresh)
            getGameChallenges();
        return gameChallengesNotificationList;
    }

    public void getGameChallenges() {
        SPRestClient.get(ServiceApi.GET_GAME_CHALLENGES + ModelManager.getInstance().getAuthManager().getUserId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetGameChallenges called before request is started");
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
                        gameChallengesNotificationList = new ArrayList<GameNotifications>();
                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                GameNotifications notifications = new GameNotifications();
                                notifications.setId(jsonArray.getJSONObject(i).getString("id"));
                                notifications.setSport_id(jsonArray.getJSONObject(i).getString("sport_id"));
                                notifications.setName(jsonArray.getJSONObject(i).getString("name"));
                                notifications.setUser_id(jsonArray.getJSONObject(i).getString("user_id"));
                                notifications.setGame_type(jsonArray.getJSONObject(i).getString("game_type"));
                                notifications.setTeam_id(jsonArray.getJSONObject(i).getString("team_id"));
                                notifications.setDate(jsonArray.getJSONObject(i).getString("date"));
                                notifications.setTime(jsonArray.getJSONObject(i).getString("time"));
                                notifications.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                notifications.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                notifications.setAddress(jsonArray.getJSONObject(i).getString("address"));
                                if (jsonArray.getJSONObject(i).has("sport"))
                                    if (!jsonArray.getJSONObject(i).isNull("sport")) {
                                        notifications.setSports_name(jsonArray.getJSONObject(i).getJSONObject("sport").getString("name"));
                                        notifications.setSports_status(jsonArray.getJSONObject(i).getJSONObject("sport").getString("status"));
                                    }
                                if (jsonArray.getJSONObject(i).has("game_challenges"))
                                    if (!jsonArray.getJSONObject(i).isNull("game_challenges")) {
                                        JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("game_challenges");
                                        ArrayList<GameChallenges> gameChallengesArrayList = new ArrayList<GameChallenges>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            GameChallenges games = new GameChallenges();
                                            games.setId(jsonArray1.getJSONObject(j).getString("id"));
                                            games.setGameId(jsonArray1.getJSONObject(j).getString("game_id"));
                                            games.setUserId(jsonArray1.getJSONObject(j).getString("user_id"));
                                            games.setTeamId(jsonArray1.getJSONObject(j).getString("team_id"));
                                            games.setStatus(jsonArray1.getJSONObject(j).getString("status"));
                                            if (jsonArray.getJSONObject(j).has("user"))
                                                if (!jsonArray1.getJSONObject(j).isNull("user")) {
//                                                    ArrayList<Users> usersArrayList = new ArrayList<Users>();
                                                    JSONObject jsonObject = jsonArray1.getJSONObject(j).getJSONObject("user");
                                                    Users users = new Users();
                                                    users.setId(jsonObject.getString("id"));
                                                    users.setFirst_name(jsonObject.getString("first_name"));
                                                    users.setLast_name(jsonObject.getString("last_name"));
                                                    users.setEmail(jsonObject.getString("email"));
                                                    users.setGender(jsonObject.getString("gender"));
                                                    users.setBio(jsonObject.getString("bio"));
                                                    users.setImage(jsonObject.getString("image"));
                                                    users.setSocial_platform(jsonObject.getString("social_platform"));
                                                    users.setSocial_id(jsonObject.getString("social_id"));
                                                    users.setLatitude(jsonObject.getString("latitude"));
                                                    users.setLongitude(jsonObject.getString("longitude"));
                                                    users.setAddress(jsonObject.getString("address"));

//                                                    usersArrayList.add(users);
                                                    games.setUsers(users);
                                                }
                                            if (jsonArray.getJSONObject(j).has("team"))
                                                if (!jsonArray1.getJSONObject(j).isNull("team")) {
                                                    ArrayList<Teams> teamsArrayList = new ArrayList<Teams>();
                                                    JSONObject jsonObject = jsonArray1.getJSONObject(j).getJSONObject("team");
                                                    Teams teams = new Teams();
                                                    teams.setId(jsonObject.getString("id"));
                                                    teams.setSport_id(jsonObject.getString("sport_id"));
                                                    teams.setTeam_name(jsonObject.getString("team_name"));
                                                    teams.setTeam_type(jsonObject.getString("team_type"));
                                                    teams.setMembers_limit(jsonObject.getString("members_limit"));
                                                    teams.setCreator_id(jsonObject.getString("creator_id"));
                                                    teams.setLatitude(jsonObject.getString("latitude"));
                                                    teams.setLongitude(jsonObject.getString("longitude"));
                                                    teams.setAddress(jsonObject.getString("address"));

                                                    teamsArrayList.add(teams);
                                                    games.setTeamsArrayList(teamsArrayList);
                                                }
                                            gameChallengesArrayList.add(games);
                                        }
                                        notifications.setUserGamesList(gameChallengesArrayList);
                                    }
                                gameChallengesNotificationList.add(notifications);
                            }
                        EventBus.getDefault().post("GetGameChallenges True");
                    } else {
                        EventBus.getDefault().post("GetGameChallenges False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetGameChallenges False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetGameChallenges False");
                } else {
                    EventBus.getDefault().post("GetGameChallenges Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("GetGameChallenges False");
                } else {
                    EventBus.getDefault().post("GetGameChallenges Network Error");
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

    public ArrayList<TeamNotifications> getTeamChallengesNotifications(boolean shouldRefresh) {
        if (shouldRefresh)
            getTeamChallenges();
        return teamChallengesNotificationList;
    }

    public void getTeamChallenges() {
        SPRestClient.get(ServiceApi.GET_TEAM_CHALLENGES + ModelManager.getInstance().getAuthManager().getUserId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetTeamChallenges called before request is started");
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
                        teamChallengesNotificationList = new ArrayList<TeamNotifications>();
                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                TeamNotifications teamNotifications = new TeamNotifications();
                                teamNotifications.setId(jsonArray.getJSONObject(i).getString("id"));
                                teamNotifications.setStatus(jsonArray.getJSONObject(i).getString("status"));
                                teamNotifications.setUser_id(jsonArray.getJSONObject(i).getString("user_id"));
                                teamNotifications.setTeam_id(jsonArray.getJSONObject(i).getString("team_id"));
                                teamChallengesNotificationList.add(teamNotifications);
                            }
                        EventBus.getDefault().post("GetTeamChallenges True");
                    } else {
                        EventBus.getDefault().post("GetTeamChallenges False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetTeamChallenges False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetTeamChallenges False");
                } else {
                    EventBus.getDefault().post("GetTeamChallenges Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("GetTeamChallenges False");
                } else {
                    EventBus.getDefault().post("GetTeamChallenges Network Error");
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
