package com.tanzil.sportspal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Preferences;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyButton;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.model.AuthManager;
import com.tanzil.sportspal.model.ModelManager;

import org.json.JSONObject;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 4/19/2016.
 */
public class LoginScreen extends Activity implements View.OnClickListener {

    private String TAG = LoginScreen.class.getSimpleName();
    private Activity activity = LoginScreen.this;
    private ImageView img_forget_password, img_back;
    private MyButton loginBtn;
    private MyEditText et_Email, et_Password;
    private  GoogleCloudMessaging gcm;
    private String regid;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        authManager = ModelManager.getInstance().getAuthManager();
        String deviceId = Preferences.readString(getApplicationContext(), Preferences.DEVICE_ID, "");
        if (Utils.isEmptyString(deviceId)) {
            deviceId = Utils.getRegId(this);
            authManager.setDeviceToken(deviceId);
        } else {
            authManager.setDeviceToken(deviceId);
        }

        loginBtn = (MyButton) findViewById(R.id.login_btn);
        img_forget_password = (ImageView) findViewById(R.id.img_forget_password);
        img_back = (ImageView) findViewById(R.id.img_back);

        et_Email = (MyEditText) findViewById(R.id.et_email);
        et_Password = (MyEditText) findViewById(R.id.et_password);

        ImageView mainView = (ImageView) findViewById(R.id.mainView);

        // For animating background
        Utils.startAnimationBG(activity, mainView);

        loginBtn.setOnClickListener(this);
        img_forget_password.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (et_Email.getText().toString().trim().length() == 0) {
                    et_Email.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
                } else if (et_Password.getText().toString().trim().length() == 0) {
                    et_Password.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject post_data = new JSONObject();
                    try {
                        post_data.put("email", et_Email.getText().toString().trim());
                        post_data.put("password", et_Password.getText().toString().trim());
                        post_data.put("device_type", "Android");
                        post_data.put("device_token", authManager.getDeviceToken());
                        SPLog.e(TAG, "LoginData" + post_data.toString());

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    Utils.showLoading(activity, getString(R.string.please_wait));
                    ModelManager.getInstance().getAuthManager().logIn(LoginScreen.this, post_data);
                }
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.img_forget_password:
                startActivity(new Intent(activity, ForgetPasswordScreen.class));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    public void onEventMainThread(String message) {
        if (message.equalsIgnoreCase("Login True")) {
            Utils.dismissLoading();
            Preferences.writeString(activity, Preferences.EMAIL, et_Email.getText().toString());
            Preferences.writeString(activity, Preferences.PASSWORD, et_Password.getText().toString());
            SPLog.e(TAG, "Login True");
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        } else if (message.contains("Login False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, "Please check your credentials!");
            SPLog.e(TAG, "Login False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("Login Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            SPLog.e(TAG, "Login Network Error");
            Utils.dismissLoading();
        }

    }

    public String getRegId(final Activity contex) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(contex);
                    }
                    regid = gcm.register(ServiceApi.GCM_PROJECT_NUMBER);
                    ModelManager.getInstance().getAuthManager().setDeviceToken(regid);
                    msg = "Device registered, registration ID=" + regid;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.e("UTILS", "DEVICE_Token---> " + regid);
            }

        }.execute(null, null, null);
        return regid;
    }
}
