package com.admiralsbs.RPG;

import java.io.Serializable;

public class Doorway extends Stationary implements Serializable {
    private static final long serialVersionUID = -8L;
    private Doorway partner;
    //private static int ID; //Probably useless
    //private int IDnum; //Probably useless
    private Map mapLoc; //Map where the doorway is contained

    public Doorway() {
        assignCoords(0, 0);
        letter = '@';
        name = "Doorway";
        partner = null;
        //ID++; //Probably useless
        //IDnum = ID; //Probably useless
    }

    public Doorway(int x, int y) {
        assignCoords(x, y);
        letter = '@';
        name = "Doorway";
        partner = null;
        //ID++; //Probably useless
        //IDnum = ID; //Probably useless
    }

    public Doorway(String n) {
        assignCoords(0, 0);
        letter = '@';
        name = n;
        partner = null;
        //ID++; //Probably useless
        //IDnum = ID; //Probably useless
    }

    public Doorway(String n, int x, int y) {
        assignCoords(x, y);
        letter = '@';
        name = n;
        partner = null;
        //ID++; //Probably useless
        //IDnum = ID; //Probably useless
    }

    public void setPartner(Doorway p) {
        partner = p;
    }

   /*public int getIDnum() { //Probably useless
      return IDnum;
   }*/

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