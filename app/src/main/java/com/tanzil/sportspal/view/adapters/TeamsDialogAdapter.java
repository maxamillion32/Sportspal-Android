package com.tanzil.sportspal.view.adapters;

/**
 * Created by Arun on 29/07/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.model.bean.Teams;

import java.util.ArrayList;


public class TeamsDialogAdapter extends BaseAdapter {
    private ArrayList<Teams> list;
    private Activity activity;

    public TeamsDialogAdapter(final Activity context,
                              ArrayList<Teams> list) {
        this.list = list;
        this.activity = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @SuppressLint({"InflateParams", "NewApi"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        final CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.row_alert_view, null);

            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        try {

            viewHolder.item_name.setText(list.get(position).getTeam_name());


        } catch (Exception ex) {
        }

        return v;
    }

    class CompleteListViewHolder {
        public MyTextView item_name;
//        public ImageView img_team;

        public CompleteListViewHolder(View convertview) {
            item_name = (MyTextView) convertview
                    .findViewById(R.id.item_name);

//            img_team = (ImageView) convertview
//                    .findViewById(R.id.img_team);
        }
    }
}
