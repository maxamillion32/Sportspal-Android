package com.tanzil.sportspal.Utility;

/**
 * Created by arun on 5/3/15.
 */
public class ServiceApi {

    public static final String baseurl = "http://sportspal.in/api/"; //staging..

    public static String GCM_PROJECT_NUMBER = "317594767865";

    public static String API_KEY = "AIzaSyAuXVk2yKgdJ8ix9kRK7nq9CCrUmbX9L0U";

    //AIzaSyBXCTk5VPL1NFwQIxvj2kPJx54L_jb9OdE
    public static String BROWSER_KEY = "AIzaSyBXCTk5VPL1NFwQIxvj2kPJx54L_jb9OdE";

    // Users Family
    public static final String LOGIN = baseurl + "users/login";
    public static final String LOGOUT = baseurl + "users/logout";
    public static final String REGISTER = baseurl + "users/add";
    public static final String VERIFY_USER = baseurl + "verifyuser.php";
    public static final String FORGOT_PASSWORD = baseurl + "forgetpassword.php";
    public static final String CHANGE_PASSWORD = baseurl + "changepassword.php";
    public static final String GET_USER_SPORTS = baseurl + "users/sports/";
    public static final String GET_ALL_SPORTS = baseurl + "sports";
    public static final String ADD_USER_SPORT = baseurl + "selectedusers";
    public static final String GET_ALL_GAMES = baseurl + "games";
    public static final String GET_SEARCH_GAMES = baseurl + "games/search";
    public static final String ADD_USER_GAME = baseurl + "games";
    public static final String GET_ALL_TEAMS = baseurl + "teams";
    public static final String ADD_TEAM = baseurl + "teams";
    public static final String GET_USERS = baseurl + "users/getuser";
    public static final String GET_USER_DETAILS = baseurl + "users/index";
    public static final String GET_USERS_GAMES = baseurl + "games/index/";
    public static final String GET_USER_TEAMS = baseurl + "teams/index/";
    public static final String GET_SEARCH_TEAMS = baseurl + "teams/search";
    public static final String ADD_USER_PREFERENCE = baseurl + "users/sports";
    public static final String GET_GAME_DETAIL = baseurl + "games/singlegame/";
    public static final String GET_TEAM_DETAIL = baseurl + "teams/singleteam/";
    public static final String GET_SEARCH_USERS = baseurl + "users/search";
    public static final String JOIN_TEAM = baseurl + "teams/join";
    public static final String CHALLENGE_TEAM = baseurl + "teams/challenge/";
    public static final String GET_GAME_CHALLENGES = baseurl + "games/users/";
    public static final String GET_TEAM_CHALLENGES = baseurl + "teams/user_team_request/";

    public static final String ACCEPT_TEAM_REQUEST = baseurl + "teams/request/";
    public static final String REJECT_GAME_CHALLENGES = baseurl + "games/challenge";
    public static final String ACCEPT_GAME_CHALLENGES = baseurl + "games/acceptchallenge";
}
