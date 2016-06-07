package com.tanzil.sportspal.model.bean;

/**
 * Created by arun.sharma on 5/17/2016.
 */
public class Address {
    String address, sports_name, sports_id, team_type, team_name, date, time, team_id, member_limit, game_status;
    double latitude, longitude;

    public String getAddress() {
        return address;
    }

    public String getMember_limit() {
        return member_limit;
    }

    public void setMember_limit(String member_limit) {
        this.member_limit = member_limit;
    }

    public String getGame_status() {
        return game_status;
    }

    public void setGame_status(String game_status) {
        this.game_status = game_status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSports_name() {
        return sports_name;
    }

    public void setSports_name(String sports_name) {
        this.sports_name = sports_name;
    }

    public String getSports_id() {
        return sports_id;
    }

    public void setSports_id(String sports_id) {
        this.sports_id = sports_id;
    }

    public String getTeam_type() {
        return team_type;
    }

    public void setTeam_type(String team_type) {
        this.team_type = team_type;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }
}
