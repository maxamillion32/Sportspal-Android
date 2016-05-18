package com.tanzil.sportspal.Utility;

import com.tanzil.sportspal.R;

/**
 * Created by arun.sharma on 5/18/2016.
 */
public class DrawableImages {

    public static int setImage(String sportsName) {
        if (sportsName.equalsIgnoreCase("Badminton"))
            return R.drawable.badminton;
        else if (sportsName.equalsIgnoreCase("basketball"))
            return R.drawable.basketball;
        else if (sportsName.equalsIgnoreCase("cricket"))
            return R.drawable.cricket;
        else if (sportsName.equalsIgnoreCase("cycling"))
            return R.drawable.cycling;
        else if (sportsName.equalsIgnoreCase("fishing"))
            return R.drawable.fishing;
        else if (sportsName.equalsIgnoreCase("football"))
            return R.drawable.football;
        else if (sportsName.equalsIgnoreCase("frisbee"))
            return R.drawable.frisbee;
        else if (sportsName.equalsIgnoreCase("golf"))
            return R.drawable.golf;
        else if (sportsName.equalsIgnoreCase("gymming"))
            return R.drawable.gymming;
        else if (sportsName.equalsIgnoreCase("hockey"))
            return R.drawable.hockey;
        else if (sportsName.equalsIgnoreCase("kabaddi"))
            return R.drawable.kabaddi;
        else if (sportsName.equalsIgnoreCase("khokho"))
            return R.drawable.khokho;
        else if (sportsName.equalsIgnoreCase("kiteflying"))
            return R.drawable.kiteflying;
        else if (sportsName.equalsIgnoreCase("rockclimbing"))
            return R.drawable.rockclimbing;
        else if (sportsName.equalsIgnoreCase("skating"))
            return R.drawable.skating;
        else if (sportsName.equalsIgnoreCase("squash"))
            return R.drawable.squash;
        else if (sportsName.equalsIgnoreCase("swimming"))
            return R.drawable.swimming;
        else if (sportsName.equalsIgnoreCase("tabletennis"))
            return R.drawable.tabletennis;
        else if (sportsName.equalsIgnoreCase("tennis"))
            return R.drawable.tennis;
        else if (sportsName.equalsIgnoreCase("volleyball"))
            return R.drawable.volleyball;
        else if (sportsName.equalsIgnoreCase("yoga"))
            return R.drawable.yoga;
        else
            return R.drawable.transparent;
    }
}
