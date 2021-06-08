package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import java.io.Serializable;

public class Sight implements Serializable {
    private String name;
    private double lon;
    private double lat;
    private boolean userNotified;

    public Sight (String name) {
        this.name = name;
        //TODO
        lon = 0.0;
        lat = 0.0;
        userNotified = false;
    }

    public double getLon(){
        return lon;
    }

    public double getLat(){
        return lat;
    }

    public String getName() {
        return name;
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
