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
import com.tanzil.sportspal.customUi.MyTextView;

import java.util.ArrayList;


public class NewsFeedAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private Activity activity;
    private int[] images = {R.drawable.patanjali, R.drawable.decathlon, R.drawable.nike};

    public NewsFeedAdapter(final Activity context,
                           ArrayList<String> list) {
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
            v = li.inflate(R.layout.news_feed_row, null);

            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        try {

            viewHolder.descriptionText.setText(list.get(position));
            viewHolder.userPic.setImageResource(images[position]);
//            viewHolder.dayText.setText("NA");

//            if (android.os.Build.VERSION.SDK_INT < 23) {
//                viewHolder.userPic.setBorderColor(activity.getResources().getColor(R.color.white));
//                viewHolder.userPic.setSelectorColor(activity.getResources().getColor(R.color.circular_image_border_color));
//            } else {
//                viewHolder.userPic.setBorderColor(ContextCompat.getColor(activity, R.color.white));
//                viewHolder.userPic.setSelectorStrokeColor(ContextCompat.getColor(activity, R.color.circular_image_border_color));
//            }
//            viewHolder.userPic.setBorderWidth(5);
//            viewHolder.userPic.setSelectorStrokeWidth(5);
//            viewHolder.userPic.addShadow();

        } catch (Exception ex) {
        }

        return v;
    }

    class CompleteListViewHolder {
        public MyTextView descriptionText/*, dayText, timeText*/;
        //        public CircularImageView userPic;
        public ImageView userPic;

        public CompleteListViewHolder(View convertview) {
            descriptionText = (MyTextView) convertview
                    .findViewById(R.id.description_text);
//            dayText = (MyTextView) convertview
//                    .findViewById(R.id.day_text);
//            timeText = (MyTextView) convertview
//                    .findViewById(R.id.time_text);
//            userPic = (CircularImageView) convertview
//                    .findViewById(R.id.img_profile);
            userPic = (ImageView) convertview
                    .findViewById(R.id.img_profile);
        }
    }
}
