package com.tanzil.sportspal.model;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.bean.Games;
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
    private ArrayList<Users> usersSearchList;
    private ArrayList<Users> selfInfoList;
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
    public ArrayList<Users> getMyDetails(boolean shouldRefresh) {
        if (shouldRefresh)
            getDetails();
        return selfInfoList;
    }

    public ArrayList<Users> getDetails(){
        SPRestClient.get(ServiceApi.GET_USER_DETAILS + "/" + ModelManager.getInstance().getAuthManager().getUserId(), null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetMyDetails called before request is started");
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
                        JSONObject jsonObject = response.getJSONObject("message");
                        int count = jsonObject.length();
                        selfInfoList = new ArrayList<Users>();
                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Users users = new Users();
                                users.setId(jsonObject.getString("id"));
                                users.setFirst_name(jsonObject.getString("first_name"));
                                users.setLast_name(jsonObject.getString("last_name"));
                                users.setBio(jsonObject.getString("bio"));
                                users.setEmail(jsonObject.getString("email"));
                                users.setDob(jsonObject.getString("dob"));
                                users.setGender(jsonObject.getString("gender"));
                                users.setImage(jsonObject.getString("image"));
                                users.setSocial_platform(jsonObject.getString("social_platform"));
                                users.setSocial_id(jsonObject.getString("social_id"));
                                users.setLatitude(jsonObject.getString("latitude"));
                                users.setLongitude(jsonObject.getString("longitude"));

                                if (jsonObject.has("sports_preferences")) {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("sports_preferences");
                                    if (jsonArray1 != null) {
                                        ArrayList<Sports> sportsArrayList = new ArrayList<Sports>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            Sports sports = new Sports();
                                            sports.setUserId(jsonArray1.getJSONObject(j).getString("user_id"));
                                            if (!jsonArray1.getJSONObject(j).isNull("sport")) {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j).getJSONObject("sport");
                                                sports.setId(jsonObject1.getString("id"));
                                                sports.setStatus(jsonObject1.getString("status"));
                                                sports.setName(jsonObject1.getString("name"));
                                            } else {
                                                sports.setName("NA");
                                            }
                                            sportsArrayList.add(sports);
                                        }
                                        users.setSports_preferences(sportsArrayList);
                                    }
                                }

                                if (jsonObject.has("games")) {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("games");
                                    if (jsonArray1.length() > 0) {
                                        ArrayList<Games> gamesArrayList = new ArrayList<Games>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            Games games = new Games();
                                            games.setId(jsonArray1.getJSONObject(j).getString("id"));
                                            games.setSportsId(jsonArray1.getJSONObject(j).getString("sport_id"));
                                            games.setName(jsonArray1.getJSONObject(j).getString("name"));
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
                                if (jsonObject.has("teams")) {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("teams");
                                    if (jsonArray1.length() > 0) {
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
                                selfInfoList.add(users);
                            }
                        EventBus.getDefault().post("GetMyDetails True");
                    } else {
                        EventBus.getDefault().post("GetMyDetails False");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("GetMyDetails False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetMyDetails False");
                } else {
                    EventBus.getDefault().post("GetMyDetails Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("GetMyDetails False");
                } else {
                    EventBus.getDefault().post("GetMyDetails Network Error");
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
        return selfInfoList;
    }

    public ArrayList<Users> getSearchUsers(boolean shouldRefresh, JSONObject jsonObject) {
        if (shouldRefresh)
            getSearch(jsonObject);
        return usersSearchList;
    }

    private void getSearch(JSONObject jsonObject) {
        SPRestClient.post(ServiceApi.GET_SEARCH_USERS, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetSearchUsers called before request is started");
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
                        usersSearchList = new ArrayList<Users>();
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
                                usersSearchList.add(users);
                            }
                        EventBus.getDefault().post("GetSearchUsers True");
                    } else {
                        EventBus.getDefault().post("GetSearchUsers False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetSearchUsers False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetSearchUsers False");
                } else {
                    EventBus.getDefault().post("GetSearchUsers Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("GetSearchUsers False");
                } else {
                    EventBus.getDefault().post("GetSearchUsers Network Error");
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
