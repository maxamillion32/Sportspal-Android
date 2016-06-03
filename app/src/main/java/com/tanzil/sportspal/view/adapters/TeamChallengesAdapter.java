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
import com.squareup.picasso.Picasso;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.ServiceApi;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.bean.TeamNotifications;
import com.tanzil.sportspal.model.bean.Teams;
import com.tanzil.sportspal.model.bean.Users;

import java.util.ArrayList;


public class TeamChallengesAdapter extends BaseAdapter {
    private ArrayList<TeamNotifications> list;
    private Activity activity;

    public TeamChallengesAdapter(final Activity context,
                                 ArrayList<TeamNotifications> list) {
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

            Teams teams = list.get(position).getTeams();
            Users users = teams.getUsers();
            String desc = users.getFirst_name() + " " + users.getLast_name() + " has invited you in " + teams.getTeam_name() + " team";
            viewHolder.descriptionText.setText(desc);

            String[] dt = list.get(position).getCreated_date().split("T");
            viewHolder.dateText.setText(dt[0]);

            viewHolder.userPic.setBorderColor(Utils.setColor(activity, R.color.white));
            viewHolder.userPic.setBorderWidth(5);
            viewHolder.userPic.setSelectorColor(Utils.setColor(activity, R.color.transparent_white));
            viewHolder.userPic.setSelectorStrokeColor(Utils.setColor(activity, R.color.black));
            viewHolder.userPic.setSelectorStrokeWidth(5);
            viewHolder.userPic.addShadow();
            if (!Utils.isEmptyString(users.getImage()))
                Picasso.with(activity)
                        .load(ServiceApi.baseurl + users.getImage())
                        .placeholder(R.drawable.customer_img)
                        .error(R.drawable.customer_img)
                        .into(viewHolder.userPic);

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
