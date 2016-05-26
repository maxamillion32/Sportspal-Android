package com.tanzil.sportspal.model.bean;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.TeamsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/3/2016.
 */
public class Teams {

    private final String TAG = TeamsManager.class.getSimpleName();

    String id, sport_id, team_name, team_type, members_limit, latitude, longitude, creator_id, address, sports_name;

    private ArrayList<Users> usersList;
    private ArrayList<Teams> teamsArrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSport_id() {
        return sport_id;
    }

    public void setSport_id(String sport_id) {
        this.sport_id = sport_id;
    }

    public String getSports_name() {
        return sports_name;
    }

    public void setSports_name(String sports_name) {
        this.sports_name = sports_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_type() {
        return team_type;
    }

    public void setTeam_type(String team_type) {
        this.team_type = team_type;
    }

    public String getMembers_limit() {
        return members_limit;
    }

    public void setMembers_limit(String members_limit) {
        this.members_limit = members_limit;
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

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public ArrayList<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<Users> usersList) {
        this.usersList = usersList;
    }

    public ArrayList<Teams> getTeamDetails(boolean shouldRefresh, String id) {
        if (shouldRefresh)
            getDetails(id);
        return teamsArrayList;
    }

    public ArrayList<Teams> getDetails(String id) {
        SPRestClient.get(ServiceApi.GET_TEAM_DETAIL + "/" + id, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetTeamDetails called before request is started");
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

                        teamsArrayList = new ArrayList<Teams>();
                        Teams.this.setId(jsonArray.getString("id"));
                        Teams.this.setSport_id(jsonArray.getString("sport_id"));
                        Teams.this.setTeam_name(jsonArray.getString("team_name"));
                        Teams.this.setTeam_type(jsonArray.getString("team_type"));
                        Teams.this.setMembers_limit(jsonArray.getString("members_limit"));
                        Teams.this.setAddress(jsonArray.getString("address"));
                        Teams.this.setCreator_id(jsonArray.getString("creator_id"));
                        Teams.this.setLatitude(jsonArray.getString("latitude"));
                        Teams.this.setLongitude(jsonArray.getString("longitude"));

                        if (jsonArray.has("sport")) {
                            if (!jsonArray.isNull("sport")) {
                                JSONObject jsonObject = jsonArray.getJSONObject("sport");
                                Teams.this.setSports_name(jsonObject.getString("name"));
                            } else {
                                Teams.this.setSports_name("NA");
                            }
                        }
                        if (jsonArray.has("user")) {
                            if (!jsonArray.isNull("user")) {
                                JSONArray jsonArray1 = jsonArray.getJSONArray("user");
                                if (jsonArray1 != null) {
                                    ArrayList<Users> usersArrayList = new ArrayList<Users>();
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        Users users = new Users();
                                        if (jsonArray1.getJSONObject(j).has("id"))
                                            users.setId(jsonArray1.getJSONObject(j).getString("id"));
                                        users.setFirst_name(jsonArray1.getJSONObject(j).getString("first_name"));
                                        users.setLast_name(jsonArray1.getJSONObject(j).getString("last_name"));
                                        users.setEmail(jsonArray1.getJSONObject(j).getString("email"));
                                        usersArrayList.add(users);
                                    }
                                    Teams.this.setUsersList(usersArrayList);
                                }
                            }
                        }
                        teamsArrayList.add(Teams.this);
//                            }
                        EventBus.getDefault().post("GetTeamDetails True");
                    } else {
                        EventBus.getDefault().post("GetTeamDetails False");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("GetTeamDetails False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetTeamDetails False");
                } else {
                    EventBus.getDefault().post("GetTeamDetails Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("GetTeamDetails False");
                } else {
                    EventBus.getDefault().post("GetTeamDetails Network Error");
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
        return teamsArrayList;
    }
}
