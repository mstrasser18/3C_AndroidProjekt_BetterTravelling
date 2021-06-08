package net.htlgrieskirchen.jthanner18.mstrasser18.bettertravelling;

import java.io.Serializable;

public class Sight implements Serializable {
    private String name;

    public Sight (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
