package com.tanzil.sportspal.model;

import com.tanzil.sportspal.model.bean.Address;

import java.util.ArrayList;

/**
 * Created by arun.sharma on 17/5/16.
 */
public class AddressManager {
    private ArrayList<Address> addresses = new ArrayList<Address>();

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public void clearData() {
        addresses = new ArrayList<Address>();
    }
}
