package com.admiralsbs.RPG;

import java.io.Serializable;


public class Item implements Serializable {
    private static final long serialVersionUID = -2L;
    protected transient static SuperOutput out;
    protected transient static SuperScanner k;

    protected int x;
    protected int y;
    protected char letter;

    public Item() {
        x = 0;
        y = 0;
        letter = '%';
    }

    public Item(SuperScanner s1, SuperOutput s2) {
        x = 0;
        y = 0;
        letter = '%';
        k = s1;
        out = s2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void assignCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void changeX(int x) {
        this.x += x;
    }

    public void changeY(int y) {
        this.y += y;
    }

    public char getLetter() {
        return letter;
    }

}