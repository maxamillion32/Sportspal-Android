package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.view.adapters.PreferenceAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/20/2016.
 */
public class PreferencesFragment extends Fragment {

    private String TAG = PreferencesFragment.class.getSimpleName();
    private Activity activity;
    private ListView preferenceList;
    private ArrayList<Sports> sportsArrayList;
    //    private ArrayAdapter<String> adapter;
    private String[] /*sportsName, */sportsId;
    private PreferenceAdapter<Sports> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.activity = super.getActivity();

        Utils.setHeader(activity, "0-" + activity.getString(R.string.preferences));

        View rootView = inflater.inflate(R.layout.fragment_preferences, container, false);

        ImageView img_submit = (ImageView) activity.findViewById(R.id.img_right);
        img_submit.setImageResource(R.drawable.check);
        img_submit.setVisibility(View.VISIBLE);

        preferenceList = (ListView) rootView.findViewById(R.id.preference_list);


        img_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adapter != null) {
                    ArrayList<Sports> mArrayProducts = adapter.getCheckedItems();

                    SPLog.d(TAG, "Selected Items: " + mArrayProducts.toString());
                    sportsId = new String[mArrayProducts.size()];
                    for (int i = 0; i < mArrayProducts.size(); i++)
                        sportsId[i] = mArrayProducts.get(i).getId();
                }
                if (sportsId != null)
                    if (sportsId.length > 0) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("user_id", ModelManager.getInstance().getAuthManager().getUserId());
                            JSONArray jsonArray = new JSONArray();
                            for (int i = 0; i < sportsId.length; i++)
                                jsonArray.put(sportsId[i]);
                            jsonObject.put("sport_id", jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Utils.showLoading(activity, activity.getString(R.string.please_wait));
                        ModelManager.getInstance().getUserPreferredSportsManager().addPreferences(jsonObject);
                    } else
                        Toast.makeText(activity, "Please select atleast one sport of your preference", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity, "Please select atleast one sport of your preference", Toast.LENGTH_SHORT).show();
            }
        });

        preferenceList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Capture ListView item click

        sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
        if (sportsArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getSportsManager().getAllSportsList(true);
        } else {
            setData();
        }

        return rootView;
    }

    private void setData() {
        if (sportsArrayList.size() > 0) {

            adapter = new PreferenceAdapter<Sports>(activity, sportsArrayList);

            preferenceList.setAdapter(adapter);
            preferenceList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter.notifyDataSetChanged();
        } else
            preferenceList.setAdapter(null);
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
        if (message.equalsIgnoreCase("GetAllSports True")) {
            SPLog.e(TAG, "GetAllSports True");
            Utils.dismissLoading();
            sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
            if (sportsArrayList == null)
                sportsArrayList = new ArrayList<>();
            setData();
        } else if (message.contains("GetAllSports False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, "GetAllSports check your credentials!");
            SPLog.e(TAG, "GetAllSports False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetAllSports Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            SPLog.e(TAG, "GetAllSports Network Error");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("AddPreferences True")) {
            SPLog.e(TAG, "AddPreferences True");
            Utils.dismissLoading();
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .popBackStack();
        } else if (message.contains("AddPreferences False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, "AddPreferences check your credentials!");
            SPLog.e(TAG, "AddPreferences False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("AddPreferences Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            SPLog.e(TAG, "AddPreferences Network Error");
            Utils.dismissLoading();
        }

    }

}
