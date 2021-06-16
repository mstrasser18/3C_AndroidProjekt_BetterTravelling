package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import java.io.Serializable;

public class Sight implements Serializable {
    private String name;
    private double lon;
    private double lat;
    private String address;
    private double rating;
    private boolean userNotified;

    public Sight (String name, String address, double lat, double lon, double rating) {
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        this.rating = rating;
        this.address = address;
        userNotified = false;
    }

    public double getLon(){
        return lon;
    }

    public double getLat(){
        return lat;
    }

    public String getAddress(){
        return address;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public boolean getUserNotified(){return userNotified;}

    public void setUserNotified(){
        userNotified = true;
    }

    @Override
    public String toString(){
        return name;
    }
}