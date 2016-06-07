package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.GPSTracker;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.model.ModelManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by arun.sharma on 5/16/2016.
 */
public class AddLocationFragment extends Fragment implements View.OnClickListener, GoogleMap.OnCameraChangeListener {

    private GoogleMap googleMap;
    private Double mDropedPinLat, mDropedPinLong;
    private String sports_name, sports_id, team_type, team_name, date, time, team_id, game_status, member_limit;
    private Activity activity;
    private boolean check = false;
    private LocationManager locationManagerd;
    private SupportMapFragment mMapfm;
    private View rootView;
    private EditText searchView;
    private ListView location_list;
    private ImageView myLocationButton, faviconAdd;
    private ArrayList<String> searchLocations;
    private ArrayList<LatLng> searchcor;
    private ArrayAdapter<String> adapter;
    private boolean geoTaskRunning = true;
    private double latitudeValue;
    private double longitudeValue;
    private boolean isSearched = true;
    private GPSTracker gps;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Utils.setHeader(activity, "0-" + activity.getString(R.string.pick_location));


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_add_location, container, false);
        } catch (InflateException e) {
        /* just return view as it is */
        }

        gps = new GPSTracker(activity);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sports_id = bundle.getString("sports_id");
            sports_name = bundle.getString("sports_name");
            team_type = bundle.getString("team_type");
            team_name = bundle.getString("team_name");
            team_id = bundle.getString("team_id");
            date = bundle.getString("date");
            time = bundle.getString("time");
            game_status = bundle.getString("game_status");
            member_limit = bundle.getString("member_limit");
        }

        myLocationButton = (ImageView) rootView.findViewById(R.id.add_venue_mylocation);
        searchView = (EditText) rootView.findViewById(R.id.edt_searchview_add_venue);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        location_list = (ListView) rootView.findViewById(R.id.location_list);

        mMapfm = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view_add_venue));
        googleMap = mMapfm.getMap();
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        //  googleMap.setMyLocationEnabled(true);
        googleMap.setOnCameraChangeListener(this);

        myLocationButton.setOnClickListener(this);
        rootView.findViewById(R.id.add_venue_drop_pin).setOnClickListener(this);

        location_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                LatLng tempa = searchcor.get(position);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempa, 15.0f));
                isSearched = true;
                latitudeValue = tempa.latitude;
                longitudeValue = tempa.longitude;

//                Intent intent = new Intent("Address");
//                intent.putExtra("message",
//                        searchLocations.get(position) + "@#@" + latitudeValue + "@#@" + longitudeValue);

//                LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                ArrayList<com.tanzil.sportspal.model.bean.Address> addressArrayList = new ArrayList<>();
                com.tanzil.sportspal.model.bean.Address address = new com.tanzil.sportspal.model.bean.Address();
                address.setAddress(searchLocations.get(position));
                address.setLatitude(latitudeValue);
                address.setLongitude(longitudeValue);
                address.setSports_name(sports_name);
                address.setSports_id(sports_id);
                address.setTeam_type(team_type);
                address.setTeam_name(team_name);
                address.setTeam_id(team_id);
                address.setDate(date);
                address.setTime(time);
                address.setGame_status(game_status);
                address.setGame_status(game_status);
                addressArrayList.add(address);
                ModelManager.getInstance().getAddressManager().setAddresses(addressArrayList);

                searchLocations.clear();
                searchcor.clear();
                adapter.notifyDataSetChanged();
                try {
                    searchView.setText("");
                    Utils.closeKeyboard(activity, searchView.getWindowToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((FragmentActivity) activity).getSupportFragmentManager()
                        .popBackStack();
            }
        });

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener()

                                             {
                                                 @Override
                                                 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                     if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                         String searchStr = searchView.getText().toString();
                                                         if (Utils.checkGPSEnabled(getActivity())) {
                                                             Utils.closeKeyboard(getActivity(), searchView.getWindowToken());
                                                             if (geoTaskRunning) {
                                                                 geoTaskRunning = false;
                                                                 new GeocoderTaskLocation().execute(searchStr);
                                                             }
                                                         } else {
                                                             Utils.showGPSDisabledAlertToUser(activity);
                                                         }
                                                         return true;
                                                     }
                                                     return false;


                                                 }
                                             }

        );
        // Capture Text in EditText
        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = searchView.getText().toString().toLowerCase(Locale.getDefault());
                if (text.length() >= 2) {
                    if (geoTaskRunning) {
                        geoTaskRunning = false;
                        new GeocoderTaskLocation().execute(text);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

        return rootView;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_venue_drop_pin:
                LatLng pos = googleMap.getCameraPosition().target;
                mDropedPinLat = pos.latitude;
                mDropedPinLong = pos.longitude;
                Log.e("AddLocation :", "" + mDropedPinLat + "," + mDropedPinLong);

                new GetCurrentAddress().execute();
                break;
            case R.id.add_venue_mylocation:
                latitudeValue = gps.getLatitude();
                longitudeValue = gps.getLongitude();
                LatLng latLng = new LatLng(latitudeValue, longitudeValue);
                if (googleMap != null)
                    googleMap.clear();

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
                getMapView();

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.setMargins(30, 20, 30, 30);
                myLocationButton.setLayoutParams(layoutParams);

                break;
        }
    }

    private void getMapView() {
        if (gps.canGetLocation()) {
            latitudeValue = gps.getLatitude();
            longitudeValue = gps.getLongitude();
            Log.e("AddLocation :", "latitude --> " + latitudeValue + ",,,,longitude---> " + longitudeValue);
        } else {
            gps.showSettingsAlert();
        }
    }

    ///****************************************************************************************

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


                if (!Utils.isEmptyString(address.getAddressLine(0)))
                    location = address.getAddressLine(0);
                if (!Utils.isEmptyString(address.getAddressLine(1)))
                    location = location + " " + address.getAddressLine(1);

                if (location.contains("null"))
                    location = location.replaceAll("null", "");
                if (!Utils.isEmptyString(location)) ;

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

    private class GetCurrentAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // this lat and log we can get from current location but here we given hard coded


            String address = getAddress(mDropedPinLat, mDropedPinLong);
            return address;
        }

        @Override
        protected void onPostExecute(String resultString) {

            ArrayList<com.tanzil.sportspal.model.bean.Address> addressArrayList = new ArrayList<>();
            com.tanzil.sportspal.model.bean.Address address = new com.tanzil.sportspal.model.bean.Address();
            address.setAddress(resultString);
            address.setLatitude(latitudeValue);
            address.setLongitude(longitudeValue);
            address.setSports_name(sports_name);
            address.setSports_id(sports_id);
            address.setTeam_type(team_type);
            address.setTeam_name(team_name);
            address.setTeam_id(team_id);
            address.setDate(date);
            address.setTime(time);
            address.setGame_status(game_status);
            address.setGame_status(game_status);
            addressArrayList.add(address);
            ModelManager.getInstance().getAddressManager().setAddresses(addressArrayList);

//            Intent intent = new Intent("Address");
//            intent.putExtra("message",
//                    resultString + "@#@" + latitudeValue + "@#@" + longitudeValue);
//
//            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .popBackStack();
        }
    }

    public String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            List addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = (Address) addresses.get(0);

                String locality = address.getLocality();
                String city = address.getCountryName();
                String region_code = address.getCountryCode();
                String zipcode = address.getPostalCode();
                double lat = address.getLatitude();
                double lon = address.getLongitude();

                result.append(locality + " ");
                result.append(city + " " + region_code + " ");
                result.append(zipcode);

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }
}
