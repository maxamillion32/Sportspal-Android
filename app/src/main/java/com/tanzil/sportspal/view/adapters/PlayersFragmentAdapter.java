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
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Sports;
import com.tanzil.sportspal.model.bean.Users;

import java.util.ArrayList;


public class PlayersFragmentAdapter extends BaseAdapter {
    private ArrayList<Users> list;
    private Activity activity;

    public PlayersFragmentAdapter(final Activity context,
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
            v = li.inflate(R.layout.player_listview_row, null);

            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        String name = list.get(position).getFirst_name() + " " + list.get(position).getLast_name();
        viewHolder.player_text.setText(name.trim());
        String sports_name = "";
        if (list.get(position).getSports_preferences() != null)
            if (list.get(position).getSports_preferences().size() > 0) {
                for (int i = 0; i < list.get(position).getSports_preferences().size(); i++) {
                    ArrayList<Sports> sportsArrayList = ModelManager.getInstance().getSportsManager().getAllSportsList(false);
                    if (sportsArrayList != null) {
                        for (int j = 0; j < sportsArrayList.size(); j++) {
                            if (list.get(position).getSports_preferences().get(i).getId().equalsIgnoreCase(sportsArrayList.get(j).getId())) {
                                sports_name += "#" + sportsArrayList.get(j).getName() + " ";
                                break;
                            }
                        }
                    }
                }
            }
//        SPLog.e("Sports names : ", "" + sports_name.trim());
        viewHolder.game_text.setText(sports_name.trim());

        String sp_name[], nm = "";
        if (sports_name.length() > 0) {
            sp_name = sports_name.split(" ");
            if (sp_name[0] != null)
                if (sp_name[0].length() > 0)
                    nm = sp_name[0].substring(1, sp_name[0].length());
        }
        String userPic;
        if (list.get(position).getImage() == null)
            userPic = "";
        else
        userPic = list.get(position).getImage();

        if (userPic.length() == 0)
        viewHolder.img_user.setImageResource(DrawableImages.setImage(nm));
        else
            viewHolder.img_user.setImageResource(DrawableImages.setImage(nm));

        return v;
    }

    class CompleteListViewHolder {
        public MyTextView player_text, game_text, advanced_text;
        public ImageView img_user;

        public CompleteListViewHolder(View convertview) {
            player_text = (MyTextView) convertview
                    .findViewById(R.id.txt_player_name);
            game_text = (MyTextView) convertview
                    .findViewById(R.id.game_text);
            advanced_text = (MyTextView) convertview
                    .findViewById(R.id.advanced_text);
            img_user = (ImageView) convertview
                    .findViewById(R.id.img_team);
        }
    }
}
