package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyButton;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Users;

import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/10/2016.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    private String TAG = SettingsFragment.class.getSimpleName();
    private Activity activity;
    private MyEditText et_email, et_firstName, et_lastName, et_dob;
    private MyButton btn_preferences, btn_location;
    private Calendar myCalendar;
    private ImageView img_update;
    private ArrayList<Users> usersArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        myCalendar = Calendar.getInstance();

        et_email = (MyEditText) rootView.findViewById(R.id.et_email);
        et_firstName = (MyEditText) rootView.findViewById(R.id.et_first_name);
        et_lastName = (MyEditText) rootView.findViewById(R.id.et_last_name);
        et_dob = (MyEditText) rootView.findViewById(R.id.et_dob);

        btn_preferences = (MyButton) rootView.findViewById(R.id.btn_preferences);
        btn_location = (MyButton) rootView.findViewById(R.id.btn_location);

        img_update = (ImageView) rootView.findViewById(R.id.img_add);

        btn_location.setTransformationMethod(null);
        btn_preferences.setTransformationMethod(null);

        btn_preferences.setOnClickListener(this);
        btn_location.setOnClickListener(this);
        img_update.setOnClickListener(this);

        usersArrayList = ModelManager.getInstance().getUsersManager().getMyDetails(false);
        if (usersArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getUsersManager().getMyDetails(true);
        } else {
            setData();
        }

        return rootView;
    }

    private void setData() {
        usersArrayList = ModelManager.getInstance().getUsersManager().getMyDetails(false);
        if (usersArrayList != null) {
            if (usersArrayList.size() > 0) {
//                for (int i = 0; i < usersArrayList.size(); i++) {
//                    if (i == 0) {
                int i = 0;
                et_email.setText(usersArrayList.get(i).getEmail());
                et_firstName.setText(usersArrayList.get(i).getFirst_name());
                et_lastName.setText(usersArrayList.get(i).getLast_name());
                et_dob.setText(usersArrayList.get(i).getDob());
//                    }
//                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add:
                break;

            case R.id.btn_location:
                break;

            case R.id.btn_preferences:
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
        //gpDatabase.setConversation(chatManager.getConversations());
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(String message) {
        Log.e(TAG, "-- " + message);
        if (message.equalsIgnoreCase("GetUserDetails True")) {
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("GetUserDetails False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetUserDetails Network Error")) {
            Utils.dismissLoading();
        }
    }
}
