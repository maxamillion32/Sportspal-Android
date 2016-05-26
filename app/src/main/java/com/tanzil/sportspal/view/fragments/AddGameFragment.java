package com.tanzil.sportspal.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.pkmmte.view.CircularImageView;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.GPSTracker;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Address;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.model.bean.Teams;
import com.tanzil.sportspal.model.bean.Users;
import com.tanzil.sportspal.view.adapters.MembersListAdapter;
import com.tanzil.sportspal.view.adapters.SportsDialogAdapter;
import com.tanzil.sportspal.view.adapters.TeamTypeDialogAdapter;
import com.tanzil.sportspal.view.adapters.TeamsDialogAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/4/2016.
 */
public class AddGameFragment extends Fragment implements View.OnClickListener {

    private String TAG = AddGameFragment.class.getSimpleName();
    private Activity activity;
    private ImageView addGame, img_sports, img_team;
    private MyTextView game_sportsName, game_teamName, txt_Date, txt_Time, txt_teamType, txt_Address;
    //    private AutoCompleteTextView txt_Address;
    private Calendar myCalendar;
    //    private LinearLayout upperLayout;
    private LinearLayout gameLayout, teamLayout, layout_TeamName;
    private String type = "game", sportsId = "", teamId = "", latitude = "0.00", longitude = "0.00";
    private ListView membersList;
    private View headerView, footerView;

    private MyEditText teamName /*corporateGroup, privateText*/;
    private MyTextView teamSport, txt_Type;
    private ArrayList<Teams> teamsArrayList;
    private ArrayList<Sports> sportsArrayList;
    private Dialog sportsDialog, teamDialog;
    private SportsDialogAdapter sportsDialogAdapter;
    private TeamsDialogAdapter teamsDialogAdapter;
    private ArrayList<Users> playersArrayList;
    private MembersListAdapter membersListAdapter;
    private TeamTypeDialogAdapter teamTypeDialogAdapter;
    private ArrayList<String> teamsType = new ArrayList<String>();
    private double lat = 0.000, lng = 0.000;
    private GPSTracker gps;
    private boolean mAlreadyLoaded = false;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_create_new_game, container, false);
        } catch (InflateException e) {
        /* just return view as it is */
        }

//        LocalBroadcastManager.getInstance(activity).registerReceiver(
//                mAddressReciever, new IntentFilter("Address"));

        gps = new GPSTracker(activity);

        myCalendar = Calendar.getInstance();
        addGame = (ImageView) rootView.findViewById(R.id.add_game);
        img_sports = (ImageView) rootView.findViewById(R.id.img_sports);
        img_team = (ImageView) rootView.findViewById(R.id.img_team);

        game_sportsName = (MyTextView) rootView.findViewById(R.id.txt_sports_name);
        game_teamName = (MyTextView) rootView.findViewById(R.id.txt_team_name);
        txt_teamType = (MyTextView) rootView.findViewById(R.id.txt_team_type);
        txt_Date = (MyTextView) rootView.findViewById(R.id.txt_date);
        txt_Time = (MyTextView) rootView.findViewById(R.id.txt_time);
//        txt_Address = (MyEditText) rootView.findViewById(R.id.txt_pick_address);

//        upperLayout = (LinearLayout) rootView.findViewById(R.id.upper_layout);
        gameLayout = (LinearLayout) rootView.findViewById(R.id.create_new_game_layout);
        teamLayout = (LinearLayout) rootView.findViewById(R.id.create_new_team_layout);
        layout_TeamName = (LinearLayout) rootView.findViewById(R.id.layout_team_name);

        membersList = (ListView) rootView.findViewById(R.id.memberList);

//        txt_Address = (AutoCompleteTextView) rootView.findViewById(R.id.txt_pick_address);
        txt_Address = (MyTextView) rootView.findViewById(R.id.txt_pick_address);
//        txt_Address.setThreshold(1);

        addGame.setOnClickListener(this);
        game_sportsName.setOnClickListener(this);
        game_teamName.setOnClickListener(this);
        txt_teamType.setOnClickListener(this);
        txt_Date.setOnClickListener(this);
        txt_Time.setOnClickListener(this);
        txt_Address.setOnClickListener(this);
        img_team.setOnClickListener(this);
        img_sports.setOnClickListener(this);


        if (!canAccessLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(Utils.INITIAL_PERMS, Utils.INITIAL_REQUEST);
            }
        }


        if (savedInstanceState == null && !mAlreadyLoaded) {
//            ModelManager.getInstance().getAddressManager().clearData();
            mAlreadyLoaded = true;
            teamsArrayList = ModelManager.getInstance().getTeamsManager().getAllTeams(false);
            if (teamsArrayList == null) {
                Utils.showLoading(activity, activity.getString(R.string.please_wait));
                ModelManager.getInstance().getTeamsManager().getAllTeams(true);
            } else {
                sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
                if (sportsArrayList == null) {
                    Utils.showLoading(activity, activity.getString(R.string.please_wait));
                    ModelManager.getInstance().getSportsManager().getAllSportsList(true);
                }
            }
        } else {
            getLatLong();
        }

        setValues();

//        txt_Address.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (count % 3 == 0) {
//                    placesTask = new PlacesTask();
//                    placesTask.execute(s.toString());
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//            }
//        });

//        txt_Address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                upperLayout.setVisibility(View.VISIBLE);
//                Utils.closeKeyboard(activity, txt_Address.getWindowToken());
//            }
//        });
        return rootView;
    }

    private void setValues() {

        ArrayList<Address> addressArrayList = ModelManager.getInstance().getAddressManager().getAddresses();
        if (addressArrayList != null)
        if (addressArrayList.size() > 0) {
            game_teamName.setText(addressArrayList.get(0).getTeam_name());
            game_sportsName.setText(addressArrayList.get(0).getTeam_name());
            txt_Date.setText(addressArrayList.get(0).getTeam_name());
            txt_teamType.setText(addressArrayList.get(0).getTeam_name());
            txt_Time.setText(addressArrayList.get(0).getTeam_name());
            txt_Address.setText(addressArrayList.get(0).getAddress());
            sportsId = addressArrayList.get(0).getSports_id();
            teamId = addressArrayList.get(0).getTeam_id();
            latitude = String.valueOf(addressArrayList.get(0).getLatitude());
            longitude = String.valueOf(addressArrayList.get(0).getLongitude());
        }

    }
    public boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }
    public boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return(PackageManager.PERMISSION_GRANTED==activity.checkSelfPermission(perm));
        } else
            return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {

            case Utils.LOCATION_REQUEST:
                if (canAccessLocation()) {
                    getLatLong();
                }
                else {
                    gps.showSettingsAlert();
                }
                break;
        }
    }

    private void getLatLong() {
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lng = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

    }

//    private final BroadcastReceiver mAddressReciever = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Get extra data included in the Intent
//            String[] message = intent.getStringExtra("message").split("@#@");
//            if (message.length > 2) {
//                txt_Address.setText(message[0]);
//                latitude = message[1];
//                longitude = message[2];
//            }
//            Log.d("receiver", "Got message: " + message[0]);
//        }
//    };

    private void setHeader() {
        SPLog.e("headerView Data : ", "Setting headerView to List data");
        headerView = View
                .inflate(activity, R.layout.add_team_header_layout, null);

        teamName = (MyEditText) headerView.findViewById(R.id.et_team_name);
//        corporateGroup = (MyEditText) headerView.findViewById(R.id.et_corporate);
//        privateText = (MyEditText) headerView.findViewById(R.id.et_private);

        teamSport = (MyTextView) headerView.findViewById(R.id.txt_team_sport);

        membersList.addHeaderView(headerView);
    }

    private void setFooter() {
        SPLog.e("footerView Data : ", "Setting footerView to List data");
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

    private void showTeamDialog() {
        teamDialog = new Dialog(activity);
        teamDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        teamDialog.setContentView(R.layout.alert_dialog_custom_view);
        teamDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        MyTextView titleView = (MyTextView) teamDialog.findViewById(R.id.title_name);
        titleView.setText(activity.getString(R.string.select_team));

        ListView listView = (ListView) teamDialog.findViewById(R.id.items_list);
        teamsArrayList = ModelManager.getInstance().getTeamsManager().getAllTeams(false);
        if (teamsArrayList != null)
            if (teamsArrayList.size() > 0) {
                teamsDialogAdapter = new TeamsDialogAdapter(activity,
                        teamsArrayList);
                listView.setAdapter(teamsDialogAdapter);
                teamsDialogAdapter.notifyDataSetChanged();
                teamDialog.show();
            } else {
                listView.setAdapter(null);
            }
        else {
            listView.setAdapter(null);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // setData(position);
                game_teamName.setText(teamsArrayList.get(position).getTeam_name());
                teamId = teamsArrayList.get(position).getId();
                teamDialog.dismiss();
            }
        });
    }

    private void showSportsDialog() {
        sportsDialog = new Dialog(activity);
        sportsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        sportsDialog.setContentView(R.layout.alert_dialog_custom_view);
        sportsDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        MyTextView titleView = (MyTextView) sportsDialog.findViewById(R.id.title_name);
        titleView.setText(activity.getString(R.string.select_sports));

        ListView listView = (ListView) sportsDialog.findViewById(R.id.items_list);
        sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
        if (sportsArrayList != null)
            if (sportsArrayList.size() > 0) {
                sportsDialogAdapter = new SportsDialogAdapter(activity,
                        sportsArrayList);
                listView.setAdapter(sportsDialogAdapter);
                sportsDialogAdapter.notifyDataSetChanged();
                sportsDialog.show();
            } else {
                listView.setAdapter(null);
            }
        else {
            listView.setAdapter(null);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // setData(position);
                game_sportsName.setText(sportsArrayList.get(position).getName());
                sportsId = sportsArrayList.get(position).getId();
                sportsDialog.dismiss();
            }
        });
    }

    private void showTeamType() {
        final Dialog teamDialog = new Dialog(activity);
        teamDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        teamDialog.setContentView(R.layout.alert_dialog_custom_view);
        teamDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        MyTextView titleView = (MyTextView) teamDialog.findViewById(R.id.title_name);
        titleView.setText(activity.getString(R.string.select_team_type));

        ListView listView = (ListView) teamDialog.findViewById(R.id.items_list);

        teamsType.add("Individual");
        teamsType.add("Team");
        teamTypeDialogAdapter = new TeamTypeDialogAdapter(activity,
                teamsType);
        listView.setAdapter(teamTypeDialogAdapter);
        teamTypeDialogAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // setData(position);
                if (position == 0)
                    layout_TeamName.setVisibility(View.GONE);
                else
                    layout_TeamName.setVisibility(View.VISIBLE);

                txt_teamType.setText(teamsType.get(position));
                teamDialog.dismiss();
            }
        });
        teamDialog.show();
    }

    private boolean isGameValidate() {
        if (lat == 0.000 || lng == 0.000) {
            getLatLong();
        }
        boolean isValidate = false;
        if (game_sportsName.getText().toString().length() == 0) {
            isValidate = false;
            Utils.showMessage(activity, "Please select a sport name");
        } else if (txt_teamType.getText().toString().length() == 0) {
            isValidate = false;
            Utils.showMessage(activity, "Please select a team type");
        } else if (txt_teamType.getText().toString().equalsIgnoreCase("Team")
                && game_teamName.getText().toString().length() == 0) {
            isValidate = false;
            Utils.showMessage(activity, "Please select a team name");
        } else if (txt_Date.getText().toString().length() == 0) {
            isValidate = false;
            Utils.showMessage(activity, "Please select date");
        } else if (txt_Time.getText().toString().length() == 0) {
            isValidate = false;
            Utils.showMessage(activity, "Please select time");
        } else if (txt_Address.getText().toString().length() == 0) {
            isValidate = false;
            Utils.showMessage(activity, "Please select address");
        } else
            isValidate = true;
        return isValidate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_game:
                if (type.equalsIgnoreCase("game")) {
                    if (isGameValidate()) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("sport_id", sportsId);
                            jsonObject.put("name", game_sportsName.getText().toString());
                            jsonObject.put("user_id", ModelManager.getInstance().getAuthManager().getUserId());
                            jsonObject.put("game_type", txt_teamType.getText().toString());
                            jsonObject.put("team_id", teamId);
                            jsonObject.put("date", txt_Date.getText().toString());
                            jsonObject.put("time", txt_Time.getText().toString());
                            jsonObject.put("latitude", latitude);
                            jsonObject.put("longitude", longitude);
                            jsonObject.put("address", txt_Address.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Utils.showLoading(activity, activity.getString(R.string.please_wait));
                        ModelManager.getInstance().getSportsManager().addGame(jsonObject);
                    }
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
                teamsArrayList = ModelManager.getInstance().getTeamsManager().getAllTeams(false);
                if (teamsArrayList == null) {
                    Utils.showLoading(activity, activity.getString(R.string.please_wait));
                    ModelManager.getInstance().getTeamsManager().getAllTeams(true);
                } else {
                    playersArrayList = ModelManager.getInstance().getUsersManager().getNearUsers(false);
                    if (playersArrayList == null) {
                        Utils.showLoading(activity, activity.getString(R.string.please_wait));
                        ModelManager.getInstance().getUsersManager().getNearUsers(true);
                    } else
                        setData();
                }

                break;

            case R.id.txt_sports_name:
                showSportsDialog();
                break;

            case R.id.txt_team_name:
                showTeamDialog();
                break;

            case R.id.txt_pick_address:

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fragment = new AddLocationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("sports_name", game_sportsName.getText().toString());
                bundle.putString("sports_id", sportsId);
                bundle.putString("team_type", txt_teamType.getText().toString());
                bundle.putString("team_name", game_teamName.getText().toString());
                bundle.putString("team_id", teamId);
                bundle.putString("date", txt_Date.getText().toString());
                bundle.putString("time", txt_Time.getText().toString());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container_body, fragment, "AddLocationFragment");
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack("AddLocationFragment");
                fragmentTransaction.commit();

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
            case R.id.txt_team_type:
                showTeamType();
                break;

        }
    }

    private void setData() {
        gameLayout.setVisibility(View.GONE);
        teamLayout.setVisibility(View.VISIBLE);
        type = "team";
        SPLog.e("Setting Data : ", "Setting List data");
        setHeader();
        setFooter();
        if (playersArrayList != null)
            if (playersArrayList.size() > 0) {
                membersListAdapter = new MembersListAdapter(activity, playersArrayList);
                membersList.setAdapter(membersListAdapter);
                membersListAdapter.notifyDataSetChanged();
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

    private void clearData() {
        game_sportsName.setText("");
        game_teamName.setText("");
        txt_Address.setText("");
        txt_Date.setText("");
        txt_Time.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(String message) {
        Log.e(TAG, "-- " + message);
        if (message.equalsIgnoreCase("GetAllTeams True")) {
            Utils.dismissLoading();
            teamsArrayList = ModelManager.getInstance().getTeamsManager().getAllTeams(false);
            sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
            if (sportsArrayList == null) {
                Utils.showLoading(activity, activity.getString(R.string.please_wait));
                ModelManager.getInstance().getSportsManager().getAllSportsList(true);
            }
        } else if (message.equalsIgnoreCase("GetAllTeams False")) {
            Utils.dismissLoading();
            Utils.showMessage(activity, activity.getString(R.string.something_went_wrong));
        } else if (message.equalsIgnoreCase("GetAllTeams Network Error")) {
            Utils.dismissLoading();
            Utils.showMessage(activity, activity.getString(R.string.something_went_wrong));
        } else if (message.equalsIgnoreCase("GetAllSports True")) {
            Utils.dismissLoading();
            sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
        } else if (message.equalsIgnoreCase("GetAllSports False")) {
            Utils.dismissLoading();
            Utils.showMessage(activity, activity.getString(R.string.something_went_wrong));
        } else if (message.equalsIgnoreCase("GetAllSports Network Error")) {
            Utils.dismissLoading();
            Utils.showMessage(activity, activity.getString(R.string.something_went_wrong));
        } else if (message.equalsIgnoreCase("GetAllPlayers True")) {
            Utils.dismissLoading();
            playersArrayList = ModelManager.getInstance().getUsersManager().getNearUsers(false);
            setData();
        } else if (message.equalsIgnoreCase("GetAllPlayers False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetAllPlayers Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("AddGame True")) {
            Utils.showMessage(activity, "Game added successfully");
            Utils.dismissLoading();
            clearData();
        } else if (message.equalsIgnoreCase("AddGame False")) {
            Utils.dismissLoading();
            Utils.showMessage(activity, activity.getString(R.string.something_went_wrong));
        } else if (message.equalsIgnoreCase("AddGame Network Error")) {
            Utils.dismissLoading();
            Utils.showMessage(activity, activity.getString(R.string.something_went_wrong));
        } else if (message.equalsIgnoreCase("GetNearUsers True")) {
            Utils.dismissLoading();
            playersArrayList = ModelManager.getInstance().getUsersManager().getNearUsers(false);
            setData();
        } else if (message.equalsIgnoreCase("GetNearUsers False")) {
            Utils.dismissLoading();
            Utils.showMessage(activity, activity.getString(R.string.something_went_wrong));
        } else if (message.equalsIgnoreCase("GetNearUsers Network Error")) {
            Utils.dismissLoading();
            Utils.showMessage(activity, activity.getString(R.string.something_went_wrong));
        }
    }
}
