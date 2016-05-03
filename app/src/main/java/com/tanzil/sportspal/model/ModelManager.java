package com.tanzil.sportspal.model;


/**
 * Created by arun on 16/12/15.
 */
public class ModelManager {

    public static ModelManager modelMgr = null;

    private AuthManager authMgr;
    private SportsManager sportsManager;
    private PlayersManager playersManager;
    private TeamsManager teamsManager;
    private UserPreferredSportsManager userPreferredSportsManager;

    private ModelManager() {

        authMgr = new AuthManager();
        sportsManager = new SportsManager();
        playersManager = new PlayersManager();
        teamsManager = new TeamsManager();
        userPreferredSportsManager = new UserPreferredSportsManager();

    }

    public void clearManagerInstance() {

        this.authMgr = null;
        this.sportsManager = null;
        this.playersManager = null;
        this.teamsManager = null;
        this.userPreferredSportsManager = null;
    }

    public static ModelManager getInstance() {
        if (modelMgr == null) {
            modelMgr = new ModelManager();
        }
        return modelMgr;
    }

    public static void setInstance() {
        modelMgr = new ModelManager();
    }

    public static boolean getInstanceModelManager() {
        return modelMgr != null;
    }

    public AuthManager getAuthManager() {

        return this.authMgr;
    }

    public SportsManager getSportsManager() {
        return sportsManager;
    }

    public PlayersManager getPlayersManager() {
        return playersManager;
    }

    public TeamsManager getTeamsManager() {
        return teamsManager;
    }

    public UserPreferredSportsManager getUserPreferredSportsManager() {
        return userPreferredSportsManager;
    }
}
