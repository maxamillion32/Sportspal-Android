package com.tanzil.sportspal.model;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.bean.Games;
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
public class SportsManager {
    private ArrayList<Sports> sportsList;
    private final String TAG = SportsManager.class.getSimpleName();
    private ArrayList<Games> gamesList;
    private String message, game_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public ArrayList<Sports> getAllSportsList(boolean shouldRefresh) {
        if (shouldRefresh)
            getSports();
        return sportsList;
    }

    private void getSports() {
        SPRestClient.get(ServiceApi.GET_ALL_SPORTS, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetAllSports called before request is started");
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
                        sportsList = new ArrayList<Sports>();
                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Sports sports = new Sports();
                                sports.setId(jsonArray.getJSONObject(i).getString("id"));
                                sports.setName(jsonArray.getJSONObject(i).getString("name"));
                                sports.setStatus(jsonArray.getJSONObject(i).getString("status"));
                                sportsList.add(sports);
                            }
                        EventBus.getDefault().post("GetAllSports True");
                    } else {
                        EventBus.getDefault().post("GetAllSports False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetAllSports False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetAllSports False");
                } else {
                    EventBus.getDefault().post("GetAllSports Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("GetAllSports False");
                } else {
                    EventBus.getDefault().post("GetAllSports Network Error");
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

    public ArrayList<Games> getAllGames(boolean shouldRefresh) {
        if (shouldRefresh)
            getGames();
        return gamesList;
    }

    private void getGames() {
        SPRestClient.get(ServiceApi.GET_ALL_GAMES, null, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "GetAllGames called before request is started");
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
                        gamesList = new ArrayList<Games>();
                        if (count > 0)
                            for (int i = 0; i < count; i++) {
                                Games games = new Games();
                                games.setId(jsonArray.getJSONObject(i).getString("id"));
                                games.setSportsId(jsonArray.getJSONObject(i).getString("sport_id"));
                                games.setUserId(jsonArray.getJSONObject(i).getString("user_id"));
                                games.setGameType(jsonArray.getJSONObject(i).getString("game_type"));
                                games.setTeamId(jsonArray.getJSONObject(i).getString("team_id"));
                                games.setDate(jsonArray.getJSONObject(i).getString("date"));
                                games.setTime(jsonArray.getJSONObject(i).getString("time"));
                                games.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                games.setLongitude(jsonArray.getJSONObject(i).getString("longitude"));
                                games.setAddress(jsonArray.getJSONObject(i).getString("address"));

                                if (jsonArray.getJSONObject(i).has("sport"))
                                    games.setSports_name(jsonArray.getJSONObject(i).getJSONObject("sport").getString("name"));
//                                if (jsonArray.getJSONObject(i).has("user")) {
//                                    games.setUser_first_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("first_name"));
//                                    games.setUser_last_name(jsonArray.getJSONObject(i).getJSONObject("user").getString("last_name"));
//                                    games.setUser_email(jsonArray.getJSONObject(i).getJSONObject("user").getString("email"));
//                                }

                                gamesList.add(games);
                            }
                        EventBus.getDefault().post("GetAllGames True");
                    } else {
                        EventBus.getDefault().post("GetAllGames False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("GetAllGames False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("GetAllGames False");
                } else {
                    EventBus.getDefault().post("GetAllGames Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("GetAllGames False");
                } else {
                    EventBus.getDefault().post("GetAllGames Network Error");
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

    public void addGame(JSONObject jsonObject) {
        SPRestClient.post(ServiceApi.ADD_TEAM, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "AddGame called before request is started");
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
                        setGame_id(response.getJSONObject("data").getString("game_id"));
                        EventBus.getDefault().post("AddGame True");
                    } else {
                        EventBus.getDefault().post("AddGame False");
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().post("AddGame False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    Log.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().post("AddGame False");
                } else {
                    EventBus.getDefault().post("AddGame Network Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (responseString != null) {
                    Log.e(TAG, "onFailure  --> " + responseString.toString());
                    EventBus.getDefault().post("AddGame False");
                } else {
                    EventBus.getDefault().post("AddGame Network Error");
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
