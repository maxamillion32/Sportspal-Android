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
import android.widget.ImageView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.DrawableImages;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.bean.Games;

import java.util.ArrayList;


public class SportsFragmentAdapter extends BaseAdapter {
    private ArrayList<Games> list;
    private Activity activity;

    public SportsFragmentAdapter(final Activity context,
                                 ArrayList<Games> list) {
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
            v = li.inflate(R.layout.sports_listview_row, null);

            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        try {

            viewHolder.distance_text.setText(list.get(position).getAddress());
            viewHolder.descriptionText.setText(list.get(position).getSports_name());
            viewHolder.dayText.setText(list.get(position).getDate());
            viewHolder.timeText.setText(list.get(position).getTime());

            viewHolder.img_team.setImageResource(DrawableImages.setImage(list.get(position).getSports_name()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return v;
    }

    class CompleteListViewHolder {
        public MyTextView distance_text, descriptionText, dayText, timeText;
        public ImageView img_team;

        public CompleteListViewHolder(View convertview) {
            distance_text = (MyTextView) convertview
                    .findViewById(R.id.distance_text);
            descriptionText = (MyTextView) convertview
                    .findViewById(R.id.description_text);
            dayText = (MyTextView) convertview
                    .findViewById(R.id.day_text);
            timeText = (MyTextView) convertview
                    .findViewById(R.id.time_text);
            img_team = (ImageView) convertview
                    .findViewById(R.id.img_team);
        }
    }
}
