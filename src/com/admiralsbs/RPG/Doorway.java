package com.admiralsbs.RPG;

import java.io.Serializable;

public class Doorway extends Stationary implements Serializable {
    private static final long serialVersionUID = -8L;
    private Doorway partner;
    private Map mapLoc; //Map where the doorway is contained

    public Doorway() {
        doorwayConstructor("Doorway", 0, 0);
    }

    public Doorway(int x, int y) {
        doorwayConstructor("Doorway", x, y);
    }

    public Doorway(String n) {
        doorwayConstructor(n, 0, 0);
    }

    public Doorway(String n, int x, int y) {
        doorwayConstructor(n, x, y);
    }

    private void doorwayConstructor(String n, int x, int y) {
        assignCoords(x, y);
        letter = '@';
        name = n;
        partner = null;
    }

    public void setPartner(Doorway p) {
        partner = p;
    }

    public Doorway getPartner() {
        return partner;
    }

    public void setMap(Map m) {
        mapLoc = m;
    }

    public Map getMapLoc() {
        return mapLoc;
    }

}