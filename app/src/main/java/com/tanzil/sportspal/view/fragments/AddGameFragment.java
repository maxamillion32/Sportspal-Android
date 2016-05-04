package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.customUi.MyTextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by arun.sharma on 5/4/2016.
 */
public class AddGameFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    private ImageView addGame;
    private MyTextView txt_sportsName, txt_teamName, txt_Date, txt_Time;
    private MyEditText txt_Address;
    private Calendar myCalendar;
    private LinearLayout upperLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        View rootView = inflater.inflate(R.layout.fragment_create_new_game, container, false);

        myCalendar = Calendar.getInstance();

        addGame = (ImageView) rootView.findViewById(R.id.add_game);

        txt_sportsName = (MyTextView) rootView.findViewById(R.id.txt_sports_name);
        txt_teamName = (MyTextView) rootView.findViewById(R.id.txt_team_name);
        txt_Date = (MyTextView) rootView.findViewById(R.id.txt_date);
        txt_Time = (MyTextView) rootView.findViewById(R.id.txt_time);
        txt_Address = (MyEditText) rootView.findViewById(R.id.txt_pick_address);

        upperLayout = (LinearLayout) rootView.findViewById(R.id.upper_layout);

        addGame.setOnClickListener(this);
        txt_sportsName.setOnClickListener(this);
        txt_teamName.setOnClickListener(this);
        txt_Date.setOnClickListener(this);
        txt_Time.setOnClickListener(this);

        txt_Address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    upperLayout.setVisibility(View.GONE);
                } else {
                    upperLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        txt_Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count % 3 == 0) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_game:
                break;

            case R.id.txt_sports_name:
                break;

            case R.id.txt_team_name:
                break;

            case R.id.txt_time:
                new TimePickerDialog(activity, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar
                        .get(Calendar.MINUTE), false).show();
                break;

            case R.id.txt_date:
                new DatePickerDialog(activity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    // date picker dialog for date Text
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(txt_Date, "date");
        }

    };

    // time picker dialog for time Text
    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateLabel(txt_Time, "time");
        }
    };

    private void updateLabel(MyTextView textView, String type) {
        String myFormat = "";
        if (type.equalsIgnoreCase("date"))
            myFormat = "dd/MM/yyyy"; //In which you need put here
        else
            myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(myCalendar.getTime()));
    }

    private class GeocoderTaskLocation extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(activity);
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(final List<Address> addresses) {


            searchLocations = new ArrayList<String>();
            searchcor = new ArrayList<LatLng>();

            for (Address address : addresses) {

                LatLng temp = new LatLng(address.getLatitude(), address.getLongitude());

                String location = null;


                if (!GpUtil.isEmptyString(address.getAddressLine(0)))
                    location = address.getAddressLine(0);
                if (!GpUtil.isEmptyString(address.getAddressLine(1)))
                    location = location + " " + address.getAddressLine(1);

                if (location.contains("null"))
                    location = location.replaceAll("null", "");
                if (!GpUtil.isEmptyString(location)) ;

            }
            location_list.setVisibility(View.VISIBLE);
            //adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, searchLocations);

            adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, searchLocations) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTextColor(Color.BLACK);
                    return view;
                }
            };
            location_list.setAdapter(adapter);
            geoTaskRunning = true;
        }

    }
}
