package com.admiralsbs.RPG;

import java.io.Serializable;

public class Healer extends Stationary implements Serializable {
    private static final long serialVersionUID = -7L;

    public Healer() {
        healerConstructor("Healer", 0, 0);
    }

    public Healer(int x, int y) {
        healerConstructor("Healer", x, y);
    }

    public Healer(String n) {
        healerConstructor(n, 0, 0);
    }

    public Healer(String n, int x, int y) {
        healerConstructor(n, x, y);
    }

    public void healerConstructor(String n, int x, int y) {
        assignCoords(x, y);
        letter = 'H';
        name = n;
    }

}