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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.DrawableImages;
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
import com.tanzil.sportspal.view.adapters.TeamMembersAdapter;
import com.tanzil.sportspal.view.adapters.TeamTypeDialogAdapter;
import com.tanzil.sportspal.view.adapters.TeamsDialogAdapter;

import org.json.JSONArray;
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
    private ImageView /*addGame, */img_sports, img_team, ic_sport;
    private MyTextView game_sportsName, game_teamName, textMember, txt_Date, txt_Time,
            txt_teamType, txt_Address, game_sports, /*add_team_member, */
            teamT;
    private Calendar myCalendar;
    private LinearLayout gameLayout, teamLayout, layout_TeamName, layoutSearch, /*createTeam,*/
            layoutSport;
    private String type = "game", sportsId = "", teamId = "", latitude = "0.00", longitude = "0.00";
    private ListView membersList, teamMemberList;

    private MyEditText et_search, teamN, et_member_limit;
    private MyTextView txt_Type, txt_mode;
    private ArrayList<Teams> teamsArrayList;
    private ArrayList<Sports> sportsArrayList;
    private Dialog sportsDialog, teamDialog;
    private ArrayList<Users> playersArrayList;
    private TeamTypeDialogAdapter teamTypeDialogAdapter;
    private ArrayList<String> teamsType = new ArrayList<String>();
    private ArrayList<String> corporateTypes = new ArrayList<String>();
    private ArrayList<String> gameStatus = new ArrayList<String>();
    private double lat = 0.000, lng = 0.000;
    private GPSTracker gps;
    private boolean mAlreadyLoaded = false;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        Utils.setHeader(activity, "3-" + activity.getString(R.string.title_add));


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
        ImageView img_right = (ImageView) activity.findViewById(R.id.img_right);
        img_right.setImageResource(R.drawable.check);
        img_right.setVisibility(View.VISIBLE);

        gps = new GPSTracker(activity);

        myCalendar = Calendar.getInstance();
        ImageView addGame = (ImageView) rootView.findViewById(R.id.add_game);
        img_sports = (ImageView) rootView.findViewById(R.id.img_sports);
        img_team = (ImageView) rootView.findViewById(R.id.img_team);
        ic_sport = (ImageView) rootView.findViewById(R.id.ic_sport);
//        ((MainActivity) getActivity()).findViewById(R.id.game).setVisibility(View.VISIBLE);
        game_sportsName = (MyTextView) rootView.findViewById(R.id.txt_sports_name);
        game_sports = (MyTextView) rootView.findViewById(R.id.txt_sports);
        game_teamName = (MyTextView) rootView.findViewById(R.id.txt_team_name);
        txt_teamType = (MyTextView) rootView.findViewById(R.id.txt_team_type);
        teamN = (MyEditText) rootView.findViewById(R.id.team_name);
        teamT = (MyTextView) rootView.findViewById(R.id.team_type);

        txt_Date = (MyTextView) rootView.findViewById(R.id.txt_date);
        txt_Time = (MyTextView) rootView.findViewById(R.id.txt_time);
        MyTextView add_team_member = (MyTextView) rootView.findViewById(R.id.add_team_member);
        textMember = (MyTextView) rootView.findViewById(R.id.members);

        et_search = (MyEditText) rootView.findViewById(R.id.et_search);
//        txt_Address = (MyEditText) rootView.findViewById(R.id.txt_pick_address);
        LinearLayout createTeam = (LinearLayout) rootView.findViewById(R.id.game);
        layoutSearch = (LinearLayout) rootView.findViewById(R.id.layout_search);
        gameLayout = (LinearLayout) rootView.findViewById(R.id.create_new_game_layout);
        teamLayout = (LinearLayout) rootView.findViewById(R.id.create_new_team_layout);
        layout_TeamName = (LinearLayout) rootView.findViewById(R.id.layout_team_name);
        layoutSport = (LinearLayout) rootView.findViewById(R.id.layout_sport);
        membersList = (ListView) rootView.findViewById(R.id.memberList);
        teamMemberList = (ListView) rootView.findViewById(R.id.list_all_members);
//        txt_Address = (AutoCompleteTextView) rootView.findViewById(R.id.txt_pick_address);
        txt_Address = (MyTextView) rootView.findViewById(R.id.txt_pick_address);
//        txt_Address.setThreshold(1);
        txt_mode = (MyTextView) rootView.findViewById(R.id.txt_mode);
        et_member_limit = (MyEditText) rootView.findViewById(R.id.et_member_limit);

        addGame.setOnClickListener(this);
        game_sportsName.setOnClickListener(this);
        game_teamName.setOnClickListener(this);
        teamN.setOnClickListener(this);
        txt_teamType.setOnClickListener(this);
        teamT.setOnClickListener(this);
        txt_Date.setOnClickListener(this);
        txt_Time.setOnClickListener(this);
        txt_Address.setOnClickListener(this);
        img_team.setOnClickListener(this);
        img_sports.setOnClickListener(this);
        game_sports.setOnClickListener(this);
        add_team_member.setOnClickListener(this);
        createTeam.setOnClickListener(this);
        img_right.setOnClickListener(this);
        txt_mode.setOnClickListener(this);

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

        membersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new UserProfileDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", selectedPlayers.get(position).getId());
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment, "UserProfileDetailFragment");
                fragmentTransaction.addToBackStack("UserProfileDetailFragment");
                fragmentTransaction.commit();

            }
        });

        setValues();
        addTextChangeListener();
        setClickOnList();
        return rootView;
    }

    ArrayList<Users> arrayTeam;

    private void addTextChangeListener() {
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Users> array = ModelManager.getInstance().getTeamMembersManager().getNearUsers("", false);
                if (array == null)
                    array = new ArrayList<Users>();

                arrayTeam = new ArrayList<Users>();
                for (int j = 0; j < array.size(); j++) {
                    if (array.get(j).getFirst_name().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        boolean isContain = false;
                        for (int k = 0; k < selectedPlayers.size(); k++) {
                            if (selectedPlayers.get(k).getEmail().equals(array.get(j).getEmail())) {
                                isContain = true;
                                break;
                            }
                        }
                        if (!isContain)
                            arrayTeam.add(array.get(j));
                    }
                }

                TeamMembersAdapter teamMembersAdapter = new TeamMembersAdapter(activity, arrayTeam);
                teamMemberList.setAdapter(teamMembersAdapter);
                teamMembersAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    ArrayList<Users> selectedPlayers = new ArrayList<>();

    private void setClickOnList() {
        teamMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int k = 0; k < selectedPlayers.size(); k++) {
                    if (selectedPlayers.get(k).getId().equalsIgnoreCase(arrayTeam.get(i).getId())) {
                        Toast.makeText(activity, "Already Added!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                layoutSearch.setVisibility(View.GONE);
                selectedPlayers.add(arrayTeam.get(i));
                TeamMembersAdapter teamMembersAdapter = new TeamMembersAdapter(activity, selectedPlayers);
                membersList.setAdapter(teamMembersAdapter);
                teamMembersAdapter.notifyDataSetChanged();
                textMember.setText("Member (" + selectedPlayers.size() + ")");
            }
        });
    }

    // to set the values when we coming back from pick address screen
    private void setValues() {
        ArrayList<Address> addressArrayList = ModelManager.getInstance().getAddressManager().getAddresses();
        if (addressArrayList == null)
            addressArrayList = new ArrayList<>();

        SPLog.e("Values : ", "" + addressArrayList.size());

        if (addressArrayList.size() > 0) {
            game_teamName.setText(addressArrayList.get(0).getTeam_name());
            game_sportsName.setText(addressArrayList.get(0).getSports_name());
            txt_Date.setText(addressArrayList.get(0).getDate());
            txt_teamType.setText(addressArrayList.get(0).getTeam_type());
            txt_mode.setText(addressArrayList.get(0).getGame_status());
            et_member_limit.setText(addressArrayList.get(0).getMember_limit());
            txt_Time.setText(addressArrayList.get(0).getTime());
            txt_Address.setText(addressArrayList.get(0).getAddress());
            sportsId = addressArrayList.get(0).getSports_id();
            teamId = addressArrayList.get(0).getTeam_id();
            latitude = String.valueOf(addressArrayList.get(0).getLatitude());
            longitude = String.valueOf(addressArrayList.get(0).getLongitude());
            if (addressArrayList.get(0).getTeam_type().equalsIgnoreCase("Individual"))
                layout_TeamName.setVisibility(View.GONE);
            else
                layout_TeamName.setVisibility(View.VISIBLE);
        }

    }

    public boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    public boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == activity.checkSelfPermission(perm));
        } else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case Utils.LOCATION_REQUEST:
                if (canAccessLocation()) {
                    getLatLong();
                } else {
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

    // Header for the add Team layout so that we can show Members list there
    private void setHeader() {
        SPLog.e("headerView Data : ", "Setting headerView to List data");
        View headerView = View
                .inflate(activity, R.layout.add_team_header_layout, null);

        MyEditText teamName = (MyEditText) headerView.findViewById(R.id.et_team_name);
        txt_Type = (MyTextView) headerView.findViewById(R.id.team_type_txt);

        MyTextView teamSport = (MyTextView) headerView.findViewById(R.id.txt_team_sport);

        corporateTypes = new ArrayList<String>();
        corporateTypes.add(activity.getString(R.string.corporate));
        corporateTypes.add(activity.getString(R.string.private_text));

        txt_Type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCorporateDialog();
            }
        });
        membersList.addHeaderView(headerView);
    }

    // Footer for the Add team to show Add Member row in the last
    private void setFooter() {
        SPLog.e("footerView Data : ", "Setting footerView to List data");
        View footerView = View
                .inflate(activity, R.layout.row_members_list, null);

        CircularImageView userPic = (CircularImageView) footerView.findViewById(R.id.img_user_pic);
        MyTextView memberName = (MyTextView) footerView.findViewById(R.id.txt_user_name);
        ImageView img_sportType = (ImageView) footerView.findViewById(R.id.img_sport_type);

//        if (Build.VERSION.SDK_INT < 23) {
        userPic.setBorderColor(Utils.setColor(activity, R.color.white));
        userPic.setSelectorColor(Utils.setColor(activity, R.color.circular_image_border_color));
//        } else {
//            userPic.setBorderColor(ContextCompat.getColor(activity, R.color.white));
//            userPic.setSelectorStrokeColor(ContextCompat.getColor(activity, R.color.circular_image_border_color));
//        }
        userPic.setBorderWidth(5);
        userPic.setSelectorStrokeWidth(5);
        userPic.addShadow();


        membersList.addFooterView(footerView);
    }

    // to show the team names in add game layout for selecting team name
    private void showTeamDialog(final boolean isGame) {
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
                TeamsDialogAdapter teamsDialogAdapter = new TeamsDialogAdapter(activity,
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
//                if (isGame)
                game_teamName.setText(teamsArrayList.get(position).getTeam_name());
//                else
//                    teamN.setText(teamsArrayList.get(position).getTeam_name());
                teamId = teamsArrayList.get(position).getId();
                teamDialog.dismiss();
            }
        });
    }

    // to show the Corporate types in add game layout for selecting type
    private void showCorporateDialog() {
        final Dialog teamDialog = new Dialog(activity);
        teamDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        teamDialog.setContentView(R.layout.alert_dialog_custom_view);
        teamDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        MyTextView titleView = (MyTextView) teamDialog.findViewById(R.id.title_name);
        titleView.setText(activity.getString(R.string.select_team));

        ListView listView = (ListView) teamDialog.findViewById(R.id.items_list);

        teamTypeDialogAdapter = new TeamTypeDialogAdapter(activity,
                corporateTypes);
        listView.setAdapter(teamTypeDialogAdapter);
        teamTypeDialogAdapter.notifyDataSetChanged();
        teamDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // setData(position);
                txt_Type.setText(corporateTypes.get(position));
                teamDialog.dismiss();
            }
        });
    }

    // to show the Sports names in add game layout for selecting name
    private void showSportsDialog(final boolean isGame) {
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
                SportsDialogAdapter sportsDialogAdapter = new SportsDialogAdapter(activity,
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
                if (isGame)
                    game_sportsName.setText(sportsArrayList.get(position).getName());
                else
                    game_sports.setText(sportsArrayList.get(position).getName());
                sportsId = sportsArrayList.get(position).getId();
                sportsDialog.dismiss();
                layoutSport.setVisibility(View.VISIBLE);
//                setImage(position);
                ic_sport.setBackgroundResource(DrawableImages.setImage(sportsArrayList.get(position).getName()));
            }
        });
    }

    // to show the team type in add game layout for selecting type
    private void showTeamType(final boolean isGame) {
        final Dialog teamDialog = new Dialog(activity);
        teamDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        teamDialog.setContentView(R.layout.alert_dialog_custom_view);
        teamDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        MyTextView titleView = (MyTextView) teamDialog.findViewById(R.id.title_name);
        titleView.setText(activity.getString(R.string.select_team_type));

        ListView listView = (ListView) teamDialog.findViewById(R.id.items_list);

        teamsType = new ArrayList<String>();
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
                if (isGame)
                    txt_teamType.setText(teamsType.get(position));
                else
                    teamT.setText(teamsType.get(position));
                teamDialog.dismiss();
            }
        });
        teamDialog.show();
    }

    // to choose game status in add game layout for selecting type
    private void showGameStatus() {
        final Dialog gameStatusDialog = new Dialog(activity);
        gameStatusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        gameStatusDialog.setContentView(R.layout.alert_dialog_custom_view);
        gameStatusDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        MyTextView titleView = (MyTextView) gameStatusDialog.findViewById(R.id.title_name);
        titleView.setText(activity.getString(R.string.select_team_type));

        ListView listView = (ListView) gameStatusDialog.findViewById(R.id.items_list);

        gameStatus = new ArrayList<String>();
        gameStatus.add("Open");
        gameStatus.add("Closed");
        TeamTypeDialogAdapter teamTypeDialogAdapter = new TeamTypeDialogAdapter(activity,
                gameStatus);
        listView.setAdapter(teamTypeDialogAdapter);
        teamTypeDialogAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // setData(position);
                txt_mode.setText(gameStatus.get(position));
                gameStatusDialog.dismiss();
            }
        });
        gameStatusDialog.show();
    }

    // to check the validation for add game layout
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
        } else if (txt_mode.getText().toString().length() == 0) {
            isValidate = false;
            Utils.showMessage(activity, "Please select game status");
        } else if (et_member_limit.getText().toString().length() == 0) {
            et_member_limit.requestFocus();
            isValidate = false;
            Utils.showMessage(activity, "Please fill member limit");
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
            case R.id.txt_mode:
                showGameStatus();
                break;

            case R.id.img_sports:
                if (Build.VERSION.SDK_INT < 23) {
                    img_sports.setBackgroundColor(activity.getResources().getColor(R.color.white));
                    img_team.setBackgroundColor(activity.getResources().getColor(R.color.transparent_white));
                } else {
                    img_sports.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                    img_team.setBackgroundColor(ContextCompat.getColor(activity, R.color.transparent_white));
                }
                img_team.setImageResource(R.drawable.team_pic);
                img_sports.setImageResource(R.drawable.sports_black);
                gameLayout.setVisibility(View.VISIBLE);
                teamLayout.setVisibility(View.GONE);
                type = "game";
                break;

            case R.id.img_team:
                if (Build.VERSION.SDK_INT < 23) {
                    img_sports.setBackgroundColor(activity.getResources().getColor(R.color.transparent_white));
                    img_team.setBackgroundColor(activity.getResources().getColor(R.color.white));
                } else {
                    img_sports.setBackgroundColor(ContextCompat.getColor(activity, R.color.transparent_white));
                    img_team.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                }
                img_team.setImageResource(R.drawable.team_black);
                img_sports.setImageResource(R.drawable.sports_pic);
                gameLayout.setVisibility(View.GONE);
                teamLayout.setVisibility(View.VISIBLE);
                type = "team";

                break;

            case R.id.txt_sports_name:
                showSportsDialog(true);
                break;

            case R.id.txt_sports:
                showSportsDialog(false);
                break;


            case R.id.txt_team_name:
                showTeamDialog(true);
                break;

//            case R.id.team_name:
//                showTeamDialog(false);
//                break;


            case R.id.add_team_member:
                Utils.showLoading(activity, "Loading team members..");
                ModelManager.getInstance().getTeamMembersManager().getNearUsers(sportsId, true);
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
                bundle.putString("game_status", txt_mode.getText().toString());
                bundle.putString("member_limit", et_member_limit.getText().toString());
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
                showTeamType(true);
                break;

            case R.id.team_type:
                showTeamType(false);
                break;

            case R.id.img_right:
                saveData();
                break;

        }
    }

    public void saveData() {
        if (type.equalsIgnoreCase("game")) {
            if (isGameValidate()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("sport_id", sportsId);
                    jsonObject.put("name", game_sportsName.getText().toString());
                    jsonObject.put("user_id", ModelManager.getInstance().getAuthManager().getUserId());
                    jsonObject.put("game_type", txt_teamType.getText().toString());
                    jsonObject.put("team_id", teamId);
                    jsonObject.put("game_status", txt_mode.getText().toString());
                    jsonObject.put("member_limit", et_member_limit.getText().toString());
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
            if (arrayTeam != null && arrayTeam.size() > 0) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("sport_id", sportsId);
                    jsonObject.put("creator_id", ModelManager.getInstance().getAuthManager().getUserId());
                    jsonObject.put("team_name", teamN.getText().toString());
                    jsonObject.put("team_type", teamT.getText().toString());
                    jsonObject.put("members_limit", 11);
                    jsonObject.put("latitude", latitude);
                    jsonObject.put("longitude", longitude);
                    jsonObject.put("address", txt_Address.getText().toString());

                    JSONArray jsonArray = new JSONArray();

//                    for (int i = 0; i < arrayTeam.size(); i++) {
//                        jsonArray.put(arrayTeam.get(i).getId());
//
//                    }
                    for (int i = 0; i < selectedPlayers.size(); i++) {
                        jsonArray.put(selectedPlayers.get(i).getId());

                    }
                    jsonObject.put("team_members", jsonArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.showLoading(activity, activity.getString(R.string.please_wait));
                ModelManager.getInstance().getTeamsManager().addTeam(jsonObject);
            } else {
                Toast.makeText(getActivity(), "Please add member", Toast.LENGTH_SHORT).show();
            }
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
                MembersListAdapter membersListAdapter = new MembersListAdapter(activity, playersArrayList);
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
        ModelManager.getInstance().getAddressManager().setAddresses(new ArrayList<Address>());
        game_sportsName.setText("");
        game_teamName.setText("");
        txt_Address.setText("");
        txt_Date.setText("");
        txt_Time.setText("");
        sportsId = "";
        teamN.setText("");
        teamT.setText("");
        latitude = "0.00";
        longitude = "0.00";
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
        } else if (message.equalsIgnoreCase("AddTeam True")) {
            Utils.showMessage(activity, "Team created successfully");
            Utils.dismissLoading();
            clearData();
        } else if (message.equalsIgnoreCase("AddTeam False")) {
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
        } else if (message.equalsIgnoreCase("GetTeam True")) {
            Utils.dismissLoading();
            layoutSearch.setVisibility(View.VISIBLE);
            arrayTeam = ModelManager.getInstance().getTeamMembersManager().getNearUsers("", false);
            TeamMembersAdapter teamMembersAdapter = new TeamMembersAdapter(activity, arrayTeam);
            teamMemberList.setAdapter(teamMembersAdapter);
        }
    }
}
