package com.tanzil.sportspal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Preferences;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyButton;
import com.tanzil.sportspal.model.ModelManager;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by arun.sharma on 4/19/2016.
 */
public class SplashScreen extends Activity implements View.OnClickListener {

    private static final String TAG = SplashScreen.class.getSimpleName();
    private ImageView img_FbLogin;
    private MyButton signUpBtn, loginBtn;
    CallbackManager fbCallbackManager;
    private String currentAccessToken;
    private String refreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        FacebookSdk.sdkInitialize(getApplicationContext());
        fbCallbackManager = CallbackManager.Factory.create();

        // set User Id to model
        ModelManager.getInstance().getAuthManager().setUserId(Preferences.readString(SplashScreen.this, Preferences.USER_ID, ""));

        if (Utils.isConnectingToInternet(SplashScreen.this)) {
            /** Starts new activity */
            if (Preferences.readBoolean(SplashScreen.this, Preferences.LOGIN, false)) {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            } else if (Preferences.readBoolean(SplashScreen.this, Preferences.REGISTRATION, false)) {
                startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                finish();
            }
        } else {
            Utils.showMessage(SplashScreen.this, "Your Internet connection is unavailable");
        }


        signUpBtn = (MyButton) findViewById(R.id.sign_up_btn);
        loginBtn = (MyButton) findViewById(R.id.login_btn);
        img_FbLogin = (ImageView) findViewById(R.id.btn_fblogin);

        ImageView mainView = (ImageView) findViewById(R.id.mainView);

        // For animating background
        Utils.startAnimationBG(SplashScreen.this, mainView);

        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        img_FbLogin.setOnClickListener(this);


        com.facebook.login.LoginManager.getInstance().registerCallback(fbCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        currentAccessToken = loginResult.getAccessToken().getToken();
                                        refreshToken = loginResult.getAccessToken().getToken();

                                        Log.e(TAG, "Fb-access_Token---> " + currentAccessToken);
//                                        String logout = Preferences.readString(getApplicationContext(), Preferences.LOGOUT, "");
                                        // if(GpUtil.isEmptyString(logout))
//                                        ModelManager.getInstance().getAuthManager().logIn(currentAccessToken, ModelManager.getInstance().getAuthManager().getUserToken());

                                        startActivity(new Intent(SplashScreen.this, SignUpScreen.class));
                                        com.facebook.login.LoginManager.getInstance().logOut();
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link,email,gender,cover");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                break;
            case R.id.sign_up_btn:
                startActivity(new Intent(SplashScreen.this, SignUpScreen.class));
                break;
            case R.id.btn_fblogin:
                if (Utils.isConnectingToInternet(SplashScreen.this)) {

                    com.facebook.login.LoginManager.getInstance().
                            logInWithReadPermissions(SplashScreen.this, Arrays.asList("public_profile", "email", "user_friends"));

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
