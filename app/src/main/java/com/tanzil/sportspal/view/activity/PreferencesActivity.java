package com.tanzil.sportspal.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
public class PreferencesActivity extends Activity {

    private String TAG = PreferencesActivity.class.getSimpleName();
    private Activity activity = PreferencesActivity.this;
    private ListView preferenceList;
    private ArrayList<Sports> sportsArrayList;
    //    private ArrayAdapter<String> adapter;
    private String[] sportsName, sportsId;
    private PreferenceAdapter<Sports> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        ImageView img_submit = (ImageView) findViewById(R.id.img_submit);
        preferenceList = (ListView) findViewById(R.id.preference_list);
        preferenceList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
        if (sportsArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getSportsManager().getAllSportsList(true);
        } else {
            setData();
        }

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

     }

    private void setData() {
        if (sportsArrayList.size() > 0) {

            adapter = new PreferenceAdapter<Sports>(activity, sportsArrayList, new ArrayList<Sports>());

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
            Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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
