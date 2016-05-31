package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.model.bean.Users;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/10/2016.
 */
public class NotificationsFragment extends Fragment  {
    private String TAG = NotificationsFragment.class.getSimpleName();
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();


        Utils.setHeader(activity, "1-" + activity.getString(R.string.title_notifications));


        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        ImageView imageView = (ImageView) activity.findViewById(R.id.img_right);
        imageView.setVisibility(View.INVISIBLE);


        return rootView;
    }

}
