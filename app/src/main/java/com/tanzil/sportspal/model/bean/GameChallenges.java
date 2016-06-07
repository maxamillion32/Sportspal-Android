package com.tanzil.sportspal.model.bean;

import com.tanzil.sportspal.model.SportsManager;

import java.util.ArrayList;

/**
 * Created by arun.sharma on 5/3/2016.
 */
public class GameChallenges {

    private final String TAG = SportsManager.class.getSimpleName();
    String id;
    String status;
    String userId;
    String teamId;
    String gameId;
//    private ArrayList<Users> usersArrayList;
    private Users users;
    private ArrayList<Teams> teamsArrayList;

//    public ArrayList<Users> getUsersArrayList() {
//        return usersArrayList;
//    }
//
//    public void setUsersArrayList(ArrayList<Users> usersArrayList) {
//        this.usersArrayList = usersArrayList;
//    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public ArrayList<Teams> getTeamsArrayList() {
        return teamsArrayList;
    }

    public void setTeamsArrayList(ArrayList<Teams> teamsArrayList) {
        this.teamsArrayList = teamsArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}


