package com.tanzil.sportspal.view.adapters;

/**
 * Created by Arun on 29/07/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pkmmte.view.CircularImageView;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.bean.Users;

import java.util.ArrayList;


public class TeamMembersAdapter extends BaseAdapter {
    private ArrayList<Users> list;
    private Activity activity;

    public TeamMembersAdapter(final Activity context,
                              ArrayList<Users> list) {
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
            v = li.inflate(R.layout.row_team_member, null);

            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        try {
            SPLog.e("Name :", "" + list.get(position).getFirst_name());
            viewHolder.userNameText.setText(list.get(position).getFirst_name());

            if (android.os.Build.VERSION.SDK_INT < 23) {
                viewHolder.userPic.setBorderColor(activity.getResources().getColor(R.color.white));
                viewHolder.userPic.setSelectorColor(activity.getResources().getColor(R.color.circular_image_border_color));
            } else {
                viewHolder.userPic.setBorderColor(ContextCompat.getColor(activity, R.color.white));
                viewHolder.userPic.setSelectorStrokeColor(ContextCompat.getColor(activity, R.color.circular_image_border_color));
            }
            viewHolder.userPic.setBorderWidth(5);
            viewHolder.userPic.setSelectorStrokeWidth(5);
            viewHolder.userPic.addShadow();

        } catch (Exception ex) {
        }

        return v;
    }

    class CompleteListViewHolder {
        public MyTextView userNameText;
        public CircularImageView userPic;
        public ImageView sportsType;

        public CompleteListViewHolder(View convertview) {
            userNameText = (MyTextView) convertview
                    .findViewById(R.id.txt_user_name);
            userPic = (CircularImageView) convertview
                    .findViewById(R.id.img_user_pic);
            sportsType = (ImageView) convertview
                    .findViewById(R.id.img_sport_type);
        }
    }
}
