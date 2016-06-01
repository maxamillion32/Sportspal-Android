package com.tanzil.sportspal.model;


/**
 * Created by arun on 16/12/15.
 */
public class ModelManager {

    public static ModelManager modelMgr = null;

    private AuthManager authMgr;
    private SportsManager sportsManager;
    private UsersManager usersManager;
    private TeamMembersManager teamMembersManager;
    private TeamsManager teamsManager;
    private UserPreferredSportsManager userPreferredSportsManager;
    private AddressManager addressManager;
    private NotificationsManager notificationsManager;

    private ModelManager() {

        authMgr = new AuthManager();
        sportsManager = new SportsManager();
        usersManager = new UsersManager();
        teamsManager = new TeamsManager();
        userPreferredSportsManager = new UserPreferredSportsManager();
        addressManager = new AddressManager();
        teamMembersManager = new TeamMembersManager();
        notificationsManager = new NotificationsManager();

    }

    public void clearManagerInstance() {

        this.authMgr = null;
        this.sportsManager = null;
        this.usersManager = null;
        this.teamsManager = null;
        this.userPreferredSportsManager = null;
        this.addressManager = null;
        this.teamMembersManager = null;
        this.notificationsManager = null;
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

    public UsersManager getUsersManager() {
        return usersManager;
    }

    public TeamsManager getTeamsManager() {
        return teamsManager;
    }

    public UserPreferredSportsManager getUserPreferredSportsManager() {
        return userPreferredSportsManager;
    }

    public AddressManager getAddressManager() {
        return addressManager;
    }

    public TeamMembersManager getTeamMembersManager() {
        return teamMembersManager;
    }

    public void setTeamMembersManager(TeamMembersManager teamMembersManager) {
        this.teamMembersManager = teamMembersManager;
    }

    public NotificationsManager getNotificationsManager() {
        return notificationsManager;
    }
}
