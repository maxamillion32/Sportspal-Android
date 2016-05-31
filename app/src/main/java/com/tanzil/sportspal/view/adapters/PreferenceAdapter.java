package com.tanzil.sportspal.view.adapters;


import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.bean.Sports;

import java.util.ArrayList;

/**
 * Created by arun.sharma on 5/25/2016.
 */

public class PreferenceAdapter<T> extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Sports> mList;
    private ArrayList<Sports> userPreferredSports;
    private SparseBooleanArray mSparseBooleanArray;

    public PreferenceAdapter(Activity context, ArrayList<Sports> list,
                             ArrayList<Sports> userPreferredSports) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mSparseBooleanArray = new SparseBooleanArray();
        mList = new ArrayList<Sports>();
        this.userPreferredSports = userPreferredSports;
        this.mList = list;

        if (userPreferredSports.size() > 0)
            for (int i = 0; i < mList.size(); i++) {
                boolean flag = false;
                for (int j = 0; j < userPreferredSports.size(); j++)
                    if (mList.get(i).getId().equalsIgnoreCase(userPreferredSports.get(j).getId())) {
                        flag = true;
                        break;
                    }
                mSparseBooleanArray.put(i, flag);
            }


    }


    public ArrayList<Sports> getCheckedItems() {
        ArrayList<Sports> mTempArry = new ArrayList<Sports>();
        for (int i = 0; i < mList.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                mTempArry.add(mList.get(i));
            }
        }
        return mTempArry;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_preference_textview, null);
        }

        MyTextView tvTitle = (MyTextView) convertView.findViewById(R.id.custom_tv);
        tvTitle.setText(mList.get(position).getName());
        CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.chkEnable);
        mCheckBox.setTag(position);
        mCheckBox.setChecked(mSparseBooleanArray.get(position));
        mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
        return convertView;
    }

    OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
        }
    };

}