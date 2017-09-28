package com.admiralsbs.RPG;

import java.io.Serializable;

public class Wall extends Stationary implements Serializable {
    private static final long serialVersionUID = -10L;

    public Wall() {
        assignCoords(0, 0);
        letter = ' ';
        name = "Wall";
    }

    public Wall(int x, int y) {
        assignCoords(x, y);
        letter = ' ';
        name = "Wall";
    }

}