package com.tanzil.sportspal.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Preferences;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.SingleShotLocationProvider;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyButton;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.model.AuthManager;
import com.tanzil.sportspal.model.ModelManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 4/19/2016.
 */
public class SignUpScreen extends Activity implements View.OnClickListener {

    private String TAG = SignUpScreen.class.getSimpleName();
    private Activity activity = SignUpScreen.this;
    private ImageView img_back;
    private MyButton signUpBtn;
    private MyEditText et_Email, et_Password, et_ConfirmPassword, et_Name, et_LastName, et_DOB, et_Gender;
    private Calendar myCalendar;
    private AuthManager authManager;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);

        authManager = ModelManager.getInstance().getAuthManager();
        String deviceId = Preferences.readString(getApplicationContext(), Preferences.DEVICE_ID, "");
        if (Utils.isEmptyString(deviceId)) {
            deviceId = Utils.getRegId(this);
            authManager.setDeviceToken(deviceId);
        } else {
            authManager.setDeviceToken(deviceId);
        }

        signUpBtn = (MyButton) findViewById(R.id.sign_up_btn);
        img_back = (ImageView) findViewById(R.id.img_back);

        et_Email = (MyEditText) findViewById(R.id.et_email);
        et_Password = (MyEditText) findViewById(R.id.et_password);
        et_Name = (MyEditText) findViewById(R.id.et_name);
        et_ConfirmPassword = (MyEditText) findViewById(R.id.et_confirm_password);
        et_LastName = (MyEditText) findViewById(R.id.et_last_name);
        et_DOB = (MyEditText) findViewById(R.id.et_dob);
        et_Gender = (MyEditText) findViewById(R.id.et_sex);

        myCalendar = Calendar.getInstance();

        ImageView mainView = (ImageView) findViewById(R.id.mainView);

        // For animating background
        Utils.startAnimationBG(activity, mainView);

        getLatLong();

        signUpBtn.setOnClickListener(this);
        img_back.setOnClickListener(this);
        et_DOB.setOnClickListener(this);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void getLatLong() {
        if (Utils.checkGPSEnabled(activity)) {
            SingleShotLocationProvider.GPSCoordinates locations = Utils.getLocation(activity);
            if (locations != null) {
                lat = locations.latitude;
                lng = locations.longitude;
            } else {
                lat = 0.000;
                lng = 0.000;
            }
        }else {
            Utils.showGPSDisabledAlertToUser(activity);
        }
    }
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_DOB.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_btn:
                if (et_Name.getText().toString().trim().length() == 0) {
                    et_Name.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();
                } else if (et_Email.getText().toString().trim().length() == 0) {
                    et_Email.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
                } else if (et_Password.getText().toString().trim().length() == 0) {
                    et_Password.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
                } else if (et_ConfirmPassword.getText().toString().trim().length() == 0) {
                    et_ConfirmPassword.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_confirm_password), Toast.LENGTH_SHORT).show();
                } else if (!et_Password.getText().toString().equalsIgnoreCase(et_ConfirmPassword.getText().toString())) {
                    et_ConfirmPassword.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_valid_password), Toast.LENGTH_SHORT).show();
                } else if (et_DOB.getText().toString().trim().length() == 0) {
                    et_DOB.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_dob), Toast.LENGTH_SHORT).show();
                } else if (et_Gender.getText().toString().trim().length() == 0) {
                    et_Gender.requestFocus();
                    Toast.makeText(getBaseContext(), getString(R.string.please_enter_sex), Toast.LENGTH_SHORT).show();
                } else {
                    if (lat == 0.000 || lng == 0.000) {
                        getLatLong();
                    } else {
                        Utils.showLoading(activity, getString(R.string.please_wait));

                        JSONObject post_data = new JSONObject();
                        try {
                            post_data.put("email", et_Email.getText().toString().trim());
                            post_data.put("password", et_Password.getText().toString().trim());
                            post_data.put("first_name", et_Name.getText().toString().trim());
                            post_data.put("last_name", et_LastName.getText().toString().trim());
                            post_data.put("dob", et_DOB.getText().toString().trim());
                            post_data.put("gender", et_Gender.getText().toString().trim());
                            post_data.put("latitude", lat);
                            post_data.put("longitude", lng);
                            post_data.put("device_type", "Android");
                            post_data.put("device_token", authManager.getDeviceToken());
                            SPLog.e(TAG, "Data" + post_data.toString());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        authManager.registerUser(SignUpScreen.this, post_data);
                    }
                }
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.et_dob:
                new DatePickerDialog(SignUpScreen.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        if (message.equalsIgnoreCase("Register True")) {
            Utils.dismissLoading();
            Preferences.writeString(activity, Preferences.EMAIL, et_Email.getText().toString());
            SPLog.e(TAG, "Register True");
            startActivity(new Intent(activity, LoginScreen.class));
            finish();
        } else if (message.contains("Register False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, "Please check your credentials!");
            SPLog.e(TAG, "Register False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("Register Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            SPLog.e(TAG, "Register Network Error");
            Utils.dismissLoading();
        }

    }

}
