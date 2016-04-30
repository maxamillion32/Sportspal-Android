package com.tanzil.sportspal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyButton;

/**
 * Created by arun.sharma on 4/19/2016.
 */
public class SplashScreen extends Activity implements View.OnClickListener {

    private ImageView mainView, img_FbLogin;
    private MyButton signUpBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        signUpBtn = (MyButton) findViewById(R.id.sign_up_btn);
        loginBtn = (MyButton) findViewById(R.id.login_btn);
        img_FbLogin = (ImageView) findViewById(R.id.img_facebook);
        mainView = (ImageView) findViewById(R.id.mainView);

        // For animating background
        Utils.startAnimationBG(SplashScreen.this, mainView);

        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        img_FbLogin.setOnClickListener(this);
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
            case R.id.img_facebook:
                break;
        }
    }
}
