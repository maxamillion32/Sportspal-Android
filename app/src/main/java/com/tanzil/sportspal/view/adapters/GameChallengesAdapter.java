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

import com.pkmmte.view.CircularImageView;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.bean.GameNotifications;

import java.util.ArrayList;


public class GameChallengesAdapter extends BaseAdapter {
    private ArrayList<GameNotifications> list;
    private Activity activity;

    public GameChallengesAdapter(final Activity context,
                                 ArrayList<GameNotifications> list) {
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
            v = li.inflate(R.layout.row_game_challenge, null);

            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        try {

            viewHolder.descriptionText.setText(list.get(position).getName());
//            viewHolder.userPic.setImageResource(images[position]);

            viewHolder.userPic.setBorderColor(activity.getResources().getColor(R.color.white));
            viewHolder.userPic.setBorderWidth(5);
            viewHolder.userPic.setSelectorColor(activity.getResources().getColor(R.color.transparent_white));
            viewHolder.userPic.setSelectorStrokeColor(activity.getResources().getColor(R.color.black));
            viewHolder.userPic.setSelectorStrokeWidth(5);
            viewHolder.userPic.addShadow();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return v;
    }

    class CompleteListViewHolder {
        public MyTextView descriptionText, dateText;
        public CircularImageView userPic;

        public CompleteListViewHolder(View convertview) {
            descriptionText = (MyTextView) convertview
                    .findViewById(R.id.txt_description);
            dateText = (MyTextView) convertview
                    .findViewById(R.id.txt_date);
            userPic = (CircularImageView) convertview
                    .findViewById(R.id.img_sender);
        }
    }
}
