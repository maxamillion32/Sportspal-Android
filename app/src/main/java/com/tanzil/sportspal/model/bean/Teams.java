package com.tanzil.sportspal.model.bean;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.ModelManager;
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

    String id, sport_id, team_name, team_type, members_limit, latitude, longitude, creator_id,
            address, sports_name, sport_status, request_id, status;

    private ArrayList<Users> usersList;
    private ArrayList<Teams> teamsArrayList;
    Users users;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users getUsers() {
        return users;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getSport_status() {
        return sport_status;
    }

    public void setSport_status(String sport_status) {
        this.sport_status = sport_status;
    }

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
        SPRestClient.get(ServiceApi.GET_TEAM_DETAIL + id, null, new JsonHttpResponseHandler() {
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
                        if (jsonArray.has("team_members")) {
                            if (!jsonArray.isNull("team_members")) {
                                JSONArray jsonArray1 = jsonArray.getJSONArray("team_members");
                                ArrayList<Users> usersArrayList = new ArrayList<Users>();
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    Teams.this.setRequest_id(jsonArray1.getJSONObject(j).getString("id"));
                                    Teams.this.setStatus(jsonArray1.getJSONObject(j).getString("status"));
                                    if (jsonArray1.getJSONObject(j).has("user")) {
                                        if (!jsonArray1.getJSONObject(j).isNull("user")) {
                                            JSONObject jsonObject = jsonArray1.getJSONObject(j).getJSONObject("user");
                                            Users users = new Users();
                                            if (jsonObject.has("id"))
                                                users.setId(jsonObject.getString("id"));
                                            users.setFirst_name(jsonObject.getString("first_name"));
                                            users.setLast_name(jsonObject.getString("last_name"));
                                            users.setEmail(jsonObject.getString("email"));
                                            users.setImage(jsonObject.getString("image"));
                                            usersArrayList.add(users);
                                        }
                                    }
                                }
                                Teams.this.setUsersList(usersArrayList);
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

    public void challengeTeam(JSONObject jsonObject) {
        SPRestClient.post(ServiceApi.CHALLENGE_TEAM + id + "/" + ModelManager.getInstance().getAuthManager().getUserId(), jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "ChallengeTeam called before request is started");
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
                        EventBus.getDefault().post("ChallengeTeam True");
                    } else {
                        EventBus.getDefault().post("ChallengeTeam False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("ChallengeTeam False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("ChallengeTeam False");
                } else {
                    EventBus.getDefault().post("ChallengeTeam Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("ChallengeTeam False");
                } else {
                    EventBus.getDefault().post("ChallengeTeam Network Error");
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

    public void acceptTeamRequest(JSONObject jsonObject) {
        SPRestClient.post(ServiceApi.ACCEPT_TEAM_REQUEST + id, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "AcceptTeamRequest called before request is started");
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
                        EventBus.getDefault().post("AcceptTeamRequest True");
                    } else {
                        EventBus.getDefault().post("AcceptTeamRequest False");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("AcceptTeamRequest False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("AcceptTeamRequest False");
                } else {
                    EventBus.getDefault().post("AcceptTeamRequest Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("AcceptTeamRequest False");
                } else {
                    EventBus.getDefault().post("AcceptTeamRequest Network Error");
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

    public void rejectTeamRequest(JSONObject jsonObject) {
        SPRestClient.delete(ServiceApi.ACCEPT_TEAM_REQUEST + id, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "RejectTeamRequest called before request is started");
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
                        EventBus.getDefault().post("RejectTeamRequest True");
                    } else {
                        EventBus.getDefault().post("RejectTeamRequest False");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("RejectTeamRequest False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("RejectTeamRequest False");
                } else {
                    EventBus.getDefault().post("RejectTeamRequest Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString);
                    EventBus.getDefault().post("RejectTeamRequest False");
                } else {
                    EventBus.getDefault().post("RejectTeamRequest Network Error");
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
