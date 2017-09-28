package com.admiralsbs.RPG;

import java.io.Serializable;

public class Enemy extends Entity implements Serializable {
    private static final long serialVersionUID = -5L;
    protected int XPValue;

    public Enemy() {
        super("Enemy");
        enemyConstructor();
    }

    public Enemy(String nm) {
        super(nm);
        enemyConstructor();
    }

    public Enemy(int x, int y) {
        super("Enemy", x, y);
        enemyConstructor();
    }

    public Enemy(String nm, int x, int y) {
        super(nm, x, y);
        enemyConstructor();
    }

    private void enemyConstructor() {
        letter = 'E';
        calcXP();
    }

    public void takeTurn() {
        int choice;

        int attackChances = 30;

        int fireballChances = getMP() - 6;
        if (getMP() < 6)
            fireballChances = 0;

        int hChance = (int) Math.pow(((getMaxHPTotal() - getHP()) / 4), 2); //(Healthdown/4)^2
        if (getMP() < 5)
            hChance = 0;

        int random = (int) (Math.random() * (attackChances + fireballChances + hChance)); //AI system
        //System.out.println(attackChances);
        //System.out.println(fireballChances);
        //System.out.println(hChance);
        //System.out.println(random);
        if (random < attackChances)
            choice = 2;
        else if (random < (fireballChances + attackChances))
            choice = 4;
        else
            choice = 3;


        switch (choice) { //Pick thing to do
            case 2:
                attack(getTarget());
                break;
            case 3:
                heal();
                break;
            case 4:
                fireball(getTarget());
                break;
        }

    }

    public void calcXP() { //Gives higher weight to atk/def
        double total = 0;
        total += ((getMaxHPTotal() - 35) / 10);
        total += (Math.pow(getMaxMPTotal() - 9, 1.1) / 5);
        total += (Math.pow(getAttackTotal() - 4, 1.3) / 3);
        total += (Math.pow(getDefenseTotal() - 4, 1.3) / 3);
        XPValue = (int) total;
    }

    public int getXPValue() {
        return XPValue;
    }


}