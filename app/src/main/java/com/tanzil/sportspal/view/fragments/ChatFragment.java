package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.Utils;

/**
 * Created by arun.sharma on 5/19/2016.
 */
public class ChatFragment extends Fragment {

    private Activity activity;
    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Utils.setHeader(activity, "4-" + activity.getString(R.string.title_chat));
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_team_details, container, false);
        } catch (InflateException e) {
        /* just return view as it is */
        }

        ImageView imageView = (ImageView) activity.findViewById(R.id.img_right);
        imageView.setVisibility(View.INVISIBLE);
        return rootView;
    }
}
