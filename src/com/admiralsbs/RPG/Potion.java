package com.admiralsbs.RPG;

public class Potion extends Usable {
    private static final long serialVersionUID = -14L;
    public static final int HP = 1;
    public static final int MP = 2;
    public static final int MAXHP = 3;
    public static final int MAXMP = 4;
    public static final int ATTACK = 5;
    public static final int DEFENSE = 6;
    private int type;
    private int potency;

    public Potion(String n, int t, int p, String d) {
        super(n, d);
        type = t;
        potency = p;
    }

    public void use(Entity e) {
        e.editStat(type, potency);
    }
}
