package com.admiralsbs.RPG;

import java.io.Serializable;

public abstract class Stationary extends Item implements Serializable {
    private static final long serialVersionUID = -6L;
    protected String name;
    public static final int DOORWAY = 1;
    public static final int HEALER = 2;
    public static final int CHEST = 3;

    public Stationary() {
        assignCoords(0, 0);
        letter = '#';
        name = "none";
    }

    public Stationary(int x, int y) {
        assignCoords(x, y);
        letter = '#';
        name = "none";
    }

    public String getName() {
        return name;
    }
}