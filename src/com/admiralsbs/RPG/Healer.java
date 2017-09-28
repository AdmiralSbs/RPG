package com.admiralsbs.RPG;

import java.io.Serializable;

public class Healer extends Stationary implements Serializable {
    private static final long serialVersionUID = -7L;

    public Healer() {
        assignCoords(0, 0);
        letter = 'H';
        name = "Healer";
    }

    public Healer(int x, int y) {
        assignCoords(x, y);
        letter = 'H';
        name = "Healer";
    }

    public Healer(String n) {
        assignCoords(0, 0);
        letter = 'H';
        name = n;
    }

    public Healer(String n, int x, int y) {
        assignCoords(x, y);
        letter = 'H';
        name = n;
    }

}