package com.tanzil.sportspal.Utility;

/**
 * Created by arun on 5/3/15.
 */
public class ServiceApi {

    public static final String baseurl = "http://youthtalking.com/sportspal/users"; //staging..

    public static String GCM_PROJECT_NUMBER = "723846163712";

    // Users Family
    public static final String LOGIN = baseurl + "/login";
    public static final String REGISTER = baseurl + "/add";
    public static final String VERIFY_USER = baseurl + "/verifyuser.php";
    public static final String FORGOT_PASSWORD = baseurl + "/forgetpassword.php";
    public static final String CHANGE_PASSWORD = baseurl + "/changepassword.php";
    public static final String GET_USER_SPORTS = baseurl + "/sports/";
    public static final String GET_ALL_SPORTS = baseurl + "/sports";
    public static final String ADD_USER_SPORT = baseurl + "/selectedusers.php";


}
