package com.admiralsbs.RPG;

import java.io.Serializable;

public class Weapon extends Handheld implements Serializable {
    private static final long serialVersionUID = -12L;
    protected int HPChange;
    protected int MPChange;
    protected int attackChange;
    protected int defenseChange;

    public Weapon(String n, int H, int M, int a, int d, String desc) {
        name = n;
        HPChange = H;
        MPChange = M;
        attackChange = a;
        defenseChange = d;
        description = desc;
    }

    public int getHPChange() {
        return HPChange;
    }

    public int getMPChange() {
        return MPChange;
    }

    public int getAttackChange() {
        return attackChange;
    }

    public int getDefenseChange() {
        return defenseChange;
    }

    public void printChanges(SuperOutput out) {
        if (HPChange > 0) {
            out.println("HP Increase: " + HPChange);
        }
        if (MPChange> 0) {
            out.println("MP Increase: " + MPChange);
        }
        if (attackChange> 0) {
            out.println("Attack Increase: " + attackChange);
        }
        if (defenseChange> 0) {
            out.println("Defense Increase: " + defenseChange);
        }
    }
}