package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.customUi.MyButton;
import com.tanzil.sportspal.customUi.MyEditText;

import java.util.Calendar;

/**
 * Created by arun.sharma on 5/10/2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private String TAG = ProfileFragment.class.getSimpleName();
    private Activity activity;
    private MyEditText et_email, et_firstName, et_lastName, et_dob;
    private MyButton btn_preferences, btn_location;
    private Calendar myCalendar;
    private ImageView img_update;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        View rootView = inflater.inflate(R.layout.fragment_create_new_game, container, false);

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

        return rootView;
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
}
