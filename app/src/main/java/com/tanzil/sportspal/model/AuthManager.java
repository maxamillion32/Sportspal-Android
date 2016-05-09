package com.tanzil.sportspal.model;

import android.app.Activity;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.tanzil.sportspal.Utility.Preferences;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.httprequest.SPRestClient;
import com.tanzil.sportspal.model.bean.Users;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;

/**
 * Created by charu on 28/12/15.
 */
public class AuthManager {

    private static final String TAG = AuthManager.class.getSimpleName();

    private String deviceToken;
    private String userToken;
    private String userId, email;
    private Users users;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public void logIn(final Activity activity, JSONObject post_data) {

        SPRestClient.loginPost(ServiceApi.LOGIN, post_data.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "called before request is started");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                SPLog.e(TAG, "onCancel  --> ");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                SPLog.e(TAG, "onSuccess  --> " + response.toString());

                try {

                    boolean state = response.getBoolean("success");
                    if (state) {
                        Preferences.writeBoolean(activity, Preferences.LOGIN, true);
                        if (response.getJSONObject("message").has("id")) {
                            Preferences.writeString(activity, Preferences.USER_ID, response.getJSONObject("message").getString("id"));
                            Preferences.writeString(activity, Preferences.EMAIL, response.getJSONObject("message").getString("email"));
                            Users users = new Users();
                            users.setEmail(response.getJSONObject("message").getString("email"));
                            users.setId(response.getJSONObject("message").getString("id"));
                            users.setFirst_name(response.getJSONObject("message").getString("first_name"));
                            users.setLast_name(response.getJSONObject("message").getString("last_name"));
                            users.setDob(response.getJSONObject("message").getString("dob"));
                            users.setGender(response.getJSONObject("message").getString("gender"));
                            users.setImage(response.getJSONObject("message").getString("image"));
                            users.setSocial_id(response.getJSONObject("message").getString("social_id"));
                            users.setSocial_platform(response.getJSONObject("message").getString("social_platform"));
                            users.setLatitude(response.getJSONObject("message").getString("latitude"));
                            users.setLongitude(response.getJSONObject("message").getString("longitude"));
                        }

                        EventBus.getDefault().postSticky("Login True");
                    } else {

                        EventBus.getDefault().postSticky("Login False@#@"+response.getString("message"));
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().postSticky("Login False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    SPLog.e(TAG, "onFailure  --> " + errorResponse.toString());
                    EventBus.getDefault().postSticky("Login False");
                } else {
                    EventBus.getDefault().postSticky("Login Network Error");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                SPLog.e(TAG, "onFinish  --> ");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                SPLog.e(TAG, "onRetry  --> ");
            }

        });

    }
    public void registerUser(final Activity activity, JSONObject jsonObject) {

        SPLog.e(TAG, "RegisterData" + jsonObject.toString());

        SPRestClient.post(ServiceApi.REGISTER, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "called before request is started");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                SPLog.e(TAG, "onCancel  --> ");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                SPLog.e(TAG, "onSuccess  --> " + response.toString());

                try {
                    boolean state = response.getBoolean("success");
                    if (state) {
                        Preferences.writeBoolean(activity, Preferences.REGISTRATION, true);

                        EventBus.getDefault().postSticky("Register True");
                    } else {
                        EventBus.getDefault().postSticky("Register False@#@"+response.getString("message"));
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().postSticky("Register False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    SPLog.e(TAG, "onFailure  --> " + errorResponse.toString());
                    try {

                    EventBus.getDefault().postSticky("Register False@#@"+errorResponse.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    EventBus.getDefault().postSticky("Register Network Error");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                SPLog.e(TAG, "onFinish  --> ");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                SPLog.e(TAG, "onRetry  --> ");
            }

        });

    }
    public void verifyUser(final Activity activity, JSONObject jsonObject) {

        SPLog.e(TAG, "VerifyData" + jsonObject.toString());

        SPRestClient.post(ServiceApi.VERIFY_USER, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "called before request is started");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                SPLog.e(TAG, "onCancel  --> ");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                SPLog.e(TAG, "onSuccess  --> " + response.toString());

                try {
                    boolean state = response.getBoolean("success");
                    if (state) {
                        Preferences.writeBoolean(activity, Preferences.LOGIN, true);
                        Preferences.writeBoolean(activity, Preferences.REGISTRATION, false);

                        EventBus.getDefault().postSticky("Verify True");
                    } else {
                        EventBus.getDefault().postSticky("Verify False@#@"+response.getString("message"));
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().postSticky("Verify False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    SPLog.e(TAG, "onFailure  --> " + errorResponse.toString());
                    try {

                        EventBus.getDefault().postSticky("Verify False@#@"+errorResponse.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    EventBus.getDefault().postSticky("Verify Network Error");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                SPLog.e(TAG, "onFinish  --> ");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                SPLog.e(TAG, "onRetry  --> ");
            }

        });

    }
    public void forgetPassword(final Activity activity, JSONObject jsonObject) {

        SPLog.e(TAG, "VerifyData" + jsonObject.toString());

        SPRestClient.post(ServiceApi.FORGOT_PASSWORD, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "called before request is started");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                SPLog.e(TAG, "onCancel  --> ");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                SPLog.e(TAG, "onSuccess  --> " + response.toString());

                try {
                    String state = response.getString("status");
                    if (state.equalsIgnoreCase("200")) {
                        Preferences.writeBoolean(activity, Preferences.FORGET_PASS, true);

                        EventBus.getDefault().postSticky("ForgetPassword True@#@" + response.getString("message"));
                    } else {
                        EventBus.getDefault().postSticky("ForgetPassword False@#@"+response.getString("message"));
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().postSticky("ForgetPassword False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    SPLog.e(TAG, "onFailure  --> " + errorResponse.toString());
                    try {

                        EventBus.getDefault().postSticky("ForgetPassword False@#@"+errorResponse.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    EventBus.getDefault().postSticky("ForgetPassword Network Error");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                SPLog.e(TAG, "onFinish  --> ");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                SPLog.e(TAG, "onRetry  --> ");
            }

        });

    }
    public void changePassword(final Activity activity, JSONObject jsonObject) {

        SPLog.e(TAG, "VerifyData" + jsonObject.toString());

        SPRestClient.post(ServiceApi.CHANGE_PASSWORD, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "called before request is started");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                SPLog.e(TAG, "onCancel  --> ");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                SPLog.e(TAG, "onSuccess  --> " + response.toString());

                try {
                    String state = response.getString("status");
                    if (state.equalsIgnoreCase("200")) {
                        Preferences.writeBoolean(activity, Preferences.LOGIN, true);

                        EventBus.getDefault().postSticky("ChangePassword True");
                    } else {
                        EventBus.getDefault().postSticky("ChangePassword False@#@"+response.getString("msg"));
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().postSticky("ChangePassword False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    SPLog.e(TAG, "onFailure  --> " + errorResponse.toString());
                    try {

                        EventBus.getDefault().postSticky("ChangePassword False@#@"+errorResponse.getString("msg"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    EventBus.getDefault().postSticky("ChangePassword Network Error");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                SPLog.e(TAG, "onFinish  --> ");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                SPLog.e(TAG, "onRetry  --> ");
            }

        });

    }
    public void logout(final Activity activity, JSONObject jsonObject) {

        SPLog.e(TAG, "VerifyData" + jsonObject.toString());

        SPRestClient.delete(ServiceApi.LOGOUT, jsonObject.toString(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.e(TAG, "called before request is started");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                SPLog.e(TAG, "onCancel  --> ");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                SPLog.e(TAG, "onSuccess  --> " + response.toString());

                try {
                    boolean state = response.getBoolean("success");
                    if (state) {
                        Preferences.writeBoolean(activity, Preferences.FORGET_PASS, true);

                        EventBus.getDefault().postSticky("Logout True@#@" + response.getString("message"));
                    } else {
                        EventBus.getDefault().postSticky("Logout False@#@"+response.getString("message"));
                    }
                } catch (JSONException e) {
                    EventBus.getDefault().postSticky("Logout False");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (errorResponse != null) {
                    SPLog.e(TAG, "onFailure  --> " + errorResponse.toString());
                    try {

                        EventBus.getDefault().postSticky("Logout False@#@"+errorResponse.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    EventBus.getDefault().postSticky("Logout Network Error");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                SPLog.e(TAG, "onFinish  --> ");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                SPLog.e(TAG, "onRetry  --> ");
            }

        });

    }
}
