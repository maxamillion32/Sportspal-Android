package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;

import com.pkmmte.view.CircularImageView;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.bean.PlaceJSONParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by arun.sharma on 5/4/2016.
 */
public class AddGameFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    private ImageView addGame, img_sports, img_team;
    private MyTextView game_sportsName, game_teamName, txt_Date, txt_Time;
    private AutoCompleteTextView txt_Address;
    private Calendar myCalendar;
    //    private LinearLayout upperLayout;
    private LinearLayout gameLayout, teamLayout;
    private ParserTask parserTask;
    private PlacesTask placesTask;
    private String type = "game";
    private ListView membersList;
    private View headerView, footerView;

    private MyEditText teamName, corporateGroup, privateText;
    private MyTextView teamSport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        View rootView = inflater.inflate(R.layout.fragment_create_new_game, container, false);

        myCalendar = Calendar.getInstance();

        addGame = (ImageView) rootView.findViewById(R.id.add_game);
        img_sports = (ImageView) rootView.findViewById(R.id.img_sports);
        img_team = (ImageView) rootView.findViewById(R.id.img_team);

        game_sportsName = (MyTextView) rootView.findViewById(R.id.txt_sports_name);
        game_teamName = (MyTextView) rootView.findViewById(R.id.txt_team_name);
        txt_Date = (MyTextView) rootView.findViewById(R.id.txt_date);
        txt_Time = (MyTextView) rootView.findViewById(R.id.txt_time);
//        txt_Address = (MyEditText) rootView.findViewById(R.id.txt_pick_address);

//        upperLayout = (LinearLayout) rootView.findViewById(R.id.upper_layout);
        gameLayout = (LinearLayout) rootView.findViewById(R.id.create_new_game_layout);
        teamLayout = (LinearLayout) rootView.findViewById(R.id.create_new_team_layout);

        membersList = (ListView) rootView.findViewById(R.id.memberList);

        txt_Address = (AutoCompleteTextView) rootView.findViewById(R.id.txt_pick_address);
        txt_Address.setThreshold(1);

        addGame.setOnClickListener(this);
        game_sportsName.setOnClickListener(this);
        game_teamName.setOnClickListener(this);
        txt_Date.setOnClickListener(this);
        txt_Time.setOnClickListener(this);
        txt_Address.setOnClickListener(this);
        img_team.setOnClickListener(this);
        img_sports.setOnClickListener(this);

        txt_Address.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count % 3 == 0) {
                    placesTask = new PlacesTask();
                    placesTask.execute(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        txt_Address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                upperLayout.setVisibility(View.VISIBLE);
                Utils.closeKeyboard(activity, txt_Address.getWindowToken());
            }
        });
        return rootView;
    }

    private void setHeader() {
        headerView = View
                .inflate(activity, R.layout.add_team_header_layout, null);

        teamName = (MyEditText) headerView.findViewById(R.id.et_team_name);
        corporateGroup = (MyEditText) headerView.findViewById(R.id.et_corporate);
        privateText = (MyEditText) headerView.findViewById(R.id.et_private);

        teamSport = (MyTextView) headerView.findViewById(R.id.txt_team_sport);

        membersList.addHeaderView(headerView);
    }

    private void setFooter() {
        footerView = View
                .inflate(activity, R.layout.row_members_list, null);

        CircularImageView userPic = (CircularImageView) footerView.findViewById(R.id.img_user_pic);
        MyTextView memberName = (MyTextView) footerView.findViewById(R.id.txt_user_name);
        ImageView img_sportType = (ImageView) footerView.findViewById(R.id.img_sport_type);

        if (android.os.Build.VERSION.SDK_INT < 23) {
            userPic.setBorderColor(activity.getResources().getColor(R.color.white));
            userPic.setSelectorColor(activity.getResources().getColor(R.color.circular_image_border_color));
        } else {
            userPic.setBorderColor(ContextCompat.getColor(activity, R.color.white));
            userPic.setSelectorStrokeColor(ContextCompat.getColor(activity, R.color.circular_image_border_color));
        }
        userPic.setBorderWidth(5);
        userPic.setSelectorStrokeWidth(5);
        userPic.addShadow();


        membersList.addFooterView(footerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_game:
                if (type.equalsIgnoreCase("game")) {

                } else {

                }
                break;

            case R.id.img_sports:
                if (android.os.Build.VERSION.SDK_INT < 23) {
                    img_sports.setBackgroundColor(activity.getResources().getColor(R.color.grey_text));
                    img_team.setBackgroundColor(activity.getResources().getColor(R.color.transparent_white));
                } else {
                    img_sports.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_text));
                    img_team.setBackgroundColor(ContextCompat.getColor(activity, R.color.transparent_white));
                }
                gameLayout.setVisibility(View.VISIBLE);
                teamLayout.setVisibility(View.GONE);
                type = "game";
                break;

            case R.id.img_team:
                if (android.os.Build.VERSION.SDK_INT < 23) {
                    img_sports.setBackgroundColor(activity.getResources().getColor(R.color.transparent_white));
                    img_team.setBackgroundColor(activity.getResources().getColor(R.color.grey_text));
                } else {
                    img_sports.setBackgroundColor(ContextCompat.getColor(activity, R.color.transparent_white));
                    img_team.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_text));
                }
                gameLayout.setVisibility(View.GONE);
                teamLayout.setVisibility(View.VISIBLE);
                type = "team";
                break;

            case R.id.txt_sports_name:
                break;

            case R.id.txt_team_name:
                break;

            case R.id.txt_pick_address:
//                if (txt_Address.isFocused()) {
//                    upperLayout.setVisibility(View.GONE);
//                } else {
//                    upperLayout.setVisibility(View.VISIBLE);
//                }
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

    // date picker diaSPLog for date Text
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

    // time picker diaSPLog for time Text
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

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url 
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url 
            urlConnection.connect();

            // Reading data from url 
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            SPLog.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=" + ServiceApi.BROWSER_KEY;

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }


            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from web service in background
                data = downloadUrl(url);
            } catch (Exception e) {
                SPLog.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            SPLog.e("result : ", "result : " + result);
            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                SPLog.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView			
            SimpleAdapter adapter = new SimpleAdapter(activity, result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            txt_Address.setAdapter(adapter);
        }
    }

}
