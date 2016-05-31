package com.tanzil.sportspal.model.bean;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.UserPreferredSportsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/3/2016.
 */
public class Users {

    private ArrayList<Users> usersList;
    private final String TAG = UserPreferredSportsManager.class.getSimpleName();

    String id, gender, dob, description, bio, address;
    String first_name, last_name, email, image, social_platform, social_id, latitude, longitude;

    ArrayList<Sports> sports_preferences;
    ArrayList<Games> gamesArrayList;
    ArrayList<Teams> teamsArrayList;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSocial_platform() {
        return social_platform;
    }

    public void setSocial_platform(String social_platform) {
        this.social_platform = social_platform;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
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

    public ArrayList<Sports> getSports_preferences() {
        return sports_preferences;
    }

    public void setSports_preferences(ArrayList<Sports> sports_preferences) {
        this.sports_preferences = sports_preferences;
    }

    public ArrayList<Games> getGamesArrayList() {
        return gamesArrayList;
    }

    public void setGamesArrayList(ArrayList<Games> gamesArrayList) {
        this.gamesArrayList = gamesArrayList;
    }

    public ArrayList<Teams> getTeamsArrayList() {
        return teamsArrayList;
    }

    public void setTeamsArrayList(ArrayList<Teams> teamsArrayList) {
        this.teamsArrayList = teamsArrayList;
    }

    public ArrayList<Users> getUserDetails(boolean shouldRefresh, String id) {
        if (shouldRefresh)
            getDetails(id);
        return usersList;
    }

    public ArrayList<Users> getDetails(String id) {
        SPRestClient.get(ServiceApi.GET_USER_DETAILS + "/" + id, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetUserDetails called before request is started");
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
//                        int count = jsonArray.length();
                        usersList = new ArrayList<Users>();
//                        if (count > 0) {
//                            for (int i = 0; i < count; i++) {
//                                Users users = new Users();
                        Users.this.setId(jsonArray.getString("id"));
                        Users.this.setFirst_name(jsonArray.getString("first_name"));
                        Users.this.setLast_name(jsonArray.getString("last_name"));
                        Users.this.setEmail(jsonArray.getString("email"));
                        Users.this.setDob(jsonArray.getString("dob"));
                        Users.this.setGender(jsonArray.getString("gender"));
                        Users.this.setImage(jsonArray.getString("image"));
                        Users.this.setSocial_platform(jsonArray.getString("social_platform"));
                        Users.this.setSocial_id(jsonArray.getString("social_id"));
                        Users.this.setLatitude(jsonArray.getString("latitude"));
                        Users.this.setLongitude(jsonArray.getString("longitude"));
                        if (jsonArray.has("description"))
                            Users.this.setDescription(jsonArray.getString("description"));

                        if (jsonArray.has("sports_preferences")) {
                            JSONArray jsonArray1 = jsonArray.getJSONArray("sports_preferences");
                            if (jsonArray1 != null) {
                                ArrayList<Sports> sportsArrayList = new ArrayList<Sports>();
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    Sports sports = new Sports();
                                    sports.setUserId(jsonArray1.getJSONObject(j).getString("user_id"));
                                    if (!jsonArray1.getJSONObject(j).isNull("sport")) {
                                        JSONObject jsonObject = jsonArray1.getJSONObject(j).getJSONObject("sport");
                                        sports.setId(jsonObject.getString("id"));
                                        sports.setStatus(jsonObject.getString("status"));
                                        sports.setName(jsonObject.getString("name"));
                                    } else {
                                        sports.setName("NA");
                                    }
                                    sportsArrayList.add(sports);
                                }
                                Users.this.setSports_preferences(sportsArrayList);
                            }
                        }
                        if (jsonArray.has("games")) {
                            JSONArray jsonArray1 = jsonArray.getJSONArray("games");
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
                                Users.this.setGamesArrayList(gamesArrayList);
                            }
                        }
                        if (jsonArray.has("teams")) {
                            JSONArray jsonArray1 = jsonArray.getJSONArray("teams");
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
                                Users.this.setTeamsArrayList(teamsArrayList);
                            }
                        }
                        usersList.add(Users.this);
//                            }
                        EventBus.getDefault().post("GetUserDetails True");
                    } else {
                        EventBus.getDefault().post("GetUserDetails False");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("GetUserDetails False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetUserDetails False");
                } else {
                    EventBus.getDefault().post("GetUserDetails Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("GetUserDetails False");
                } else {
                    EventBus.getDefault().post("GetUserDetails Network Error");
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
        return usersList;
    }
}


