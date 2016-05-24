package com.tanzil.sportspal.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.view.adapters.PreferenceAdapter;

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
    private PreferenceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        ImageView img_submit = (ImageView) findViewById(R.id.img_submit);
        preferenceList = (ListView) findViewById(R.id.preference_list);


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
//                SparseBooleanArray checked = preferenceList.getCheckedItemPositions();
//                ArrayList<String> selectedItems = new ArrayList<String>();
//                for (int i = 0; i < checked.size(); i++) {
//                    // Item position in adapter
//                    int position = checked.keyAt(i);
//                    SPLog.e("Position : ", "" + position);
//                    // Add sport if it is checked i.e.) == TRUE!
//                    if (checked.valueAt(i))
//                        selectedItems.add(adapter.getItem(position));
//                }
//
//                String[] outputStrArr = new String[selectedItems.size()];
//
//                for (int i = 0; i < selectedItems.size(); i++) {
//                    outputStrArr[i] = selectedItems.get(i);
//                }

            }
        });

        preferenceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                preferenceList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                // Capture ListView item click
                preferenceList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode,
                                                          int position, long id, boolean checked) {

                        // Prints the count of selected Items in title
                        mode.setTitle(preferenceList.getCheckedItemCount() + " Selected");

                        // Toggle the state of item after every click on it
                        adapter.toggleSelection(position);
                    }

                    /**
                     * Called to report a user click on an action button.
                     * @return true if this callback handled the event,
                     *          false if the standard MenuItem invocation should continue.
                     */
                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if (item.getItemId() == R.id.img_submit) {
                            SparseBooleanArray selected = adapter.getSelectedIds();
                            short size = (short) selected.size();
                            for (byte I = 0; I < size; I++) {
                                if (selected.valueAt(I)) {

                                }
                            }

                            // Close CAB (Contextual Action Bar)
                            mode.finish();
                            return true;
                        }
                        return false;
                    }

                    /**
                     * Called when action mode is first created.
                     * The menu supplied will be used to generate action buttons for the action mode.
                     * @param mode ActionMode being created
                     * @param menu Menu used to populate action buttons
                     * @return true if the action mode should be created,
                     *          false if entering this mode should be aborted.
                     */
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.menu_main, menu);
                        return true;
                    }

                    /**
                     * Called when an action mode is about to be exited and destroyed.
                     */
                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        //  mAdapter.removeSelection();
                    }

                    /**
                     * Called to refresh an action mode's action menu whenever it is invalidated.
                     * @return true if the menu or action mode was updated, false otherwise.
                     */
                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }
                });
                return false;
            }
        });
    }

    private void setData() {
        if (sportsArrayList.size() > 0) {
            sportsName = new String[sportsArrayList.size()];
            sportsId = new String[sportsArrayList.size()];
            for (int i = 0; i < sportsArrayList.size(); i++) {
                sportsName[i] = sportsArrayList.get(i).getName();
                sportsId[i] = sportsArrayList.get(i).getId();
            }

            adapter = new PreferenceAdapter(this, R.layout.row_preference_textview, sportsArrayList);
//            listView.setAdapter(mAdapter);

            preferenceList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            preferenceList.setAdapter(adapter);
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
            Utils.dismissLoading();
            sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
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
        }

    }

}
