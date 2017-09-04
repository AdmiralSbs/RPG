package io.github.AdmiralSbs.RPG;

import java.io.Serializable;

public class Stationary extends Item implements Serializable {
    private static final long serialVersionUID = -6L;
    protected String name;

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