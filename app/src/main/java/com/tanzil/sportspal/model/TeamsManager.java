package com.tanzil.sportspal.model;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
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
public class TeamsManager {
    private ArrayList<Teams> teamsList;
    private ArrayList<Teams> searchTeamsList;
    private ArrayList<Teams> userTeamsList;
    private ArrayList<Teams> userSearchedTeamsList;
    private final String TAG = UserPreferredSportsManager.class.getSimpleName();

    private String message, team_id;

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Teams> getAllTeams(boolean shouldRefresh) {
        if (shouldRefresh)
            getTeams();
        return teamsList;
    }

    private void getTeams() {
        SPRestClient.get(ServiceApi.GET_ALL_TEAMS, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetAllTeams called before request is started");
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
                        teamsList = new ArrayList<Teams>();

                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Teams teams = new Teams();
                                teams.setId(jsonArray.getJSONObject(i).getString("id"));
                                teams.setSport_id(jsonArray.getJSONObject(i).getString("sport_id"));
                                teams.setAddress(jsonArray.getJSONObject(i).getString("address"));
                                teams.setCreator_id(jsonArray.getJSONObject(i).getString("creator_id"));
                                teams.setTeam_name(jsonArray.getJSONObject(i).getString("team_name"));
                                teams.setTeam_type(jsonArray.getJSONObject(i).getString("team_type"));
                                teams.setMembers_limit(jsonArray.getJSONObject(i).getString("members_limit"));
                                teams.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                teams.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                if (jsonArray.getJSONObject(i).has("sport"))
                                    if (!jsonArray.getJSONObject(i).isNull("sport"))
                                        teams.setSports_name(jsonArray.getJSONObject(i).getJSONObject("sport").getString("name"));
                                if (jsonArray.getJSONObject(i).has("user"))
                                    if (!jsonArray.getJSONObject(i).isNull("user")) {
                                        ArrayList<Users> usersArrayList = new ArrayList<Users>();
                                        Users users = new Users();
                                        users.setFirst_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("first_name"));
                                        users.setLast_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("last_name"));
                                        users.setEmail(jsonArray.getJSONObject(i).getJSONObject("user").getString("email"));
                                        usersArrayList.add(users);
                                        teams.setUsersList(usersArrayList);
                                    }

                                teamsList.add(teams);
                            }
                        EventBus.getDefault().post("GetAllTeams True");
                    } else {
                        EventBus.getDefault().post("GetAllTeams False");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("GetAllTeams False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetAllTeams False");
                } else {
                    EventBus.getDefault().post("GetAllTeams Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("GetAllTeams False");
                } else {
                    EventBus.getDefault().post("GetAllTeams Network Error");
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

    public ArrayList<Teams> getSearchTeams(boolean shouldRefresh, JSONObject jsonObject) {
        if (shouldRefresh)
            getSearchedTeams(jsonObject);
        return searchTeamsList;
    }

    private void getSearchedTeams(JSONObject jsonObject) {
        SPRestClient.post(ServiceApi.GET_SEARCH_TEAMS, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetSearchTeams called before request is started");
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
                        searchTeamsList = new ArrayList<Teams>();

                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Teams teams = new Teams();
                                teams.setId(jsonArray.getJSONObject(i).getString("id"));
                                teams.setSport_id(jsonArray.getJSONObject(i).getString("sport_id"));
                                teams.setAddress(jsonArray.getJSONObject(i).getString("address"));
                                teams.setCreator_id(jsonArray.getJSONObject(i).getString("creator_id"));
                                teams.setTeam_name(jsonArray.getJSONObject(i).getString("team_name"));
                                teams.setTeam_type(jsonArray.getJSONObject(i).getString("team_type"));
                                teams.setMembers_limit(jsonArray.getJSONObject(i).getString("members_limit"));
                                teams.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                teams.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                if (jsonArray.getJSONObject(i).has("sport"))
                                    teams.setSports_name(jsonArray.getJSONObject(i).getJSONObject("sport").getString("name"));
//                                if (jsonArray.getJSONObject(i).has("user")){
//                                    ArrayList<Users> usersArrayList = new ArrayList<Users>();
//                                    Users users = new Users();
//                                    users.setFirst_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("first_name"));
//                                    users.setLast_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("last_name"));
//                                    users.setEmail(jsonArray.getJSONObject(i).getJSONObject("user").getString("email"));
//                                    usersArrayList.add(users);
//                                    teams.setUsersList(usersArrayList);
//                                }

                                searchTeamsList.add(teams);
                            }
                        EventBus.getDefault().post("GetSearchTeams True");
                    } else {
                        EventBus.getDefault().post("GetSearchTeams False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetSearchTeams False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetSearchTeams False");
                } else {
                    EventBus.getDefault().post("GetSearchTeams Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("GetSearchTeams False");
                } else {
                    EventBus.getDefault().post("GetSearchTeams Network Error");
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

    public ArrayList<Teams> getUserPreferredTeams(boolean shouldRefresh, String userId) {
        if (shouldRefresh)
            getUserTeams(userId);
        return userTeamsList;
    }

    private void getUserTeams(String userId) {
        SPRestClient.get(ServiceApi.GET_USER_TEAMS + userId, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetUserTeams called before request is started");
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
                        userTeamsList = new ArrayList<Teams>();

                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Teams teams = new Teams();
                                teams.setId(jsonArray.getJSONObject(i).getString("id"));
                                teams.setSport_id(jsonArray.getJSONObject(i).getString("sport_id"));
                                teams.setAddress(jsonArray.getJSONObject(i).getString("address"));
                                teams.setCreator_id(jsonArray.getJSONObject(i).getString("creator_id"));
                                teams.setTeam_name(jsonArray.getJSONObject(i).getString("team_name"));
                                teams.setTeam_type(jsonArray.getJSONObject(i).getString("team_type"));
                                teams.setMembers_limit(jsonArray.getJSONObject(i).getString("members_limit"));
                                teams.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                teams.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                if (jsonArray.getJSONObject(i).has("sport"))
                                    teams.setSports_name(jsonArray.getJSONObject(i).getJSONObject("sport").getString("name"));
//                                if (jsonArray.getJSONObject(i).has("user")){
//                                    ArrayList<Users> usersArrayList = new ArrayList<Users>();
//                                    Users users = new Users();
//                                    users.setFirst_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("first_name"));
//                                    users.setLast_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("last_name"));
//                                    users.setEmail(jsonArray.getJSONObject(i).getJSONObject("user").getString("email"));
//                                    usersArrayList.add(users);
//                                    teams.setUsersList(usersArrayList);
//                                }

                                userTeamsList.add(teams);
                            }
                        EventBus.getDefault().post("GetUserTeams True");
                    } else {
                        EventBus.getDefault().post("GetUserTeams False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetUserTeams False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetUserTeams False");
                } else {
                    EventBus.getDefault().post("GetUserTeams Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("GetUserTeams False");
                } else {
                    EventBus.getDefault().post("GetUserTeams Network Error");
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

    public ArrayList<Teams> searchUserPreferredTeams(boolean shouldRefresh, String userId, String search) {
        if (shouldRefresh)
            searchUserTeams(userId, search);
        return userTeamsList;
    }

    private void searchUserTeams(String userId, String search) {
        SPRestClient.get(ServiceApi.GET_USER_TEAMS + userId + "/" + search, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "SearchedUserTeams called before request is started");
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
                        userTeamsList = new ArrayList<Teams>();

                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Teams teams = new Teams();
                                teams.setId(jsonArray.getJSONObject(i).getString("id"));
                                teams.setSport_id(jsonArray.getJSONObject(i).getString("sport_id"));
                                teams.setAddress(jsonArray.getJSONObject(i).getString("address"));
                                teams.setCreator_id(jsonArray.getJSONObject(i).getString("creator_id"));
                                teams.setTeam_name(jsonArray.getJSONObject(i).getString("team_name"));
                                teams.setTeam_type(jsonArray.getJSONObject(i).getString("team_type"));
                                teams.setMembers_limit(jsonArray.getJSONObject(i).getString("members_limit"));
                                teams.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                teams.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                if (jsonArray.getJSONObject(i).has("sport"))
                                    teams.setSports_name(jsonArray.getJSONObject(i).getJSONObject("sport").getString("name"));
//                                if (jsonArray.getJSONObject(i).has("user")){
//                                    ArrayList<Users> usersArrayList = new ArrayList<Users>();
//                                    Users users = new Users();
//                                    users.setFirst_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("first_name"));
//                                    users.setLast_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("last_name"));
//                                    users.setEmail(jsonArray.getJSONObject(i).getJSONObject("user").getString("email"));
//                                    usersArrayList.add(users);
//                                    teams.setUsersList(usersArrayList);
//                                }

                                userTeamsList.add(teams);
                            }
                        EventBus.getDefault().post("SearchedUserTeams True");
                    } else {
                        EventBus.getDefault().post("SearchedUserTeams False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("SearchedUserTeams False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("SearchedUserTeams False");
                } else {
                    EventBus.getDefault().post("SearchedUserTeams Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("SearchedUserTeams False");
                } else {
                    EventBus.getDefault().post("SearchedUserTeams Network Error");
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

    public void addTeam(JSONObject jsonObject) {
        SPRestClient.post(ServiceApi.ADD_TEAM, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "AddTeam called before request is started");
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
                        setTeam_id(response.getJSONObject("data").getString("team_id"));
                        EventBus.getDefault().post("AddTeam True");
                    } else {
                        EventBus.getDefault().post("AddTeam False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("AddTeam False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("AddTeam False");
                } else {
                    EventBus.getDefault().post("AddTeam Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("AddTeam False");
                } else {
                    EventBus.getDefault().post("AddTeam Network Error");
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

    public void joinTeam(JSONObject jsonObject) {
        SPRestClient.post(ServiceApi.JOIN_TEAM, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "JoinTeam called before request is started");
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
                        EventBus.getDefault().post("JoinTeam True");
                    } else {
                        EventBus.getDefault().post("JoinTeam False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("JoinTeam False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("JoinTeam False");
                } else {
                    EventBus.getDefault().post("JoinTeam Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("JoinTeam False");
                } else {
                    EventBus.getDefault().post("JoinTeam Network Error");
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
