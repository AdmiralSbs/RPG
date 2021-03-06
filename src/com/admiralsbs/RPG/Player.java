package com.admiralsbs.RPG;

import java.io.Serializable;
import java.util.ArrayList;

public class Player extends Entity implements Serializable {
    private static final long serialVersionUID = -4L;
    private int XP;
    private int level;
    private ArrayList<Handheld> inventory = new ArrayList<>(); //Inventory

    public Player() {
        super("Player");
        playerConstructor();
    }

    public Player(String nm) {
        super(nm);
        playerConstructor();
    }

    public Player(int x, int y) {
        super("Player", x, y);
        playerConstructor();
    }

    public Player(String nm, int x, int y) {
        super(nm, x, y);
        playerConstructor();
    }

    private void playerConstructor() {
        letter = 'P';
        XP = 0;
        level = 1;
    }

    public void printStats() {
        if (weapon != null) {
            out.println("Name: " + name + ", Lvl: " + level + ", XP: " + XP);
            out.println("HP: " + HP + "/" + maxHP + " (+" + weapon.getHPChange() + ")");
            out.println("MP: " + MP + "/" + maxMP + " (+" + weapon.getMPChange() + ")");
            out.println("Attack: " + attack + " (+" + weapon.getAttackChange() + ")");
            out.println("Defense: " + defense + " (+" + weapon.getDefenseChange() + ")");
        } else {
            out.println("Name: " + name + ", Lvl: " + level + ", XP: " + XP + "/" + (10 * level * level));
            out.println("HP: " + HP + "/" + maxHP);
            out.println("MP: " + MP + "/" + maxMP);
            out.println("Attack: " + attack);
            out.println("Defense: " + defense);
        }
    }

    private void takeTurn() {
        int choice;
        do {
            do { //Forces a possible move
                out.println(getName() + "'s turn");
                out.println("1) Check stats");
                out.println("2) Attack");
                out.println("3) Heal (5 MP)");
                out.println("4) Fireball (6 MP)");
                choice = Integer.parseInt(k.getCode("1", "2", "3", "4"));
                if (choice == 3 && MP < 5) {
                    out.println("You don't have enough MP for that");
                    choice = 0;
                } else if (choice == 4 && MP < 6) {
                    out.println("You don't have enough MP for that");
                    choice = 0;
                } else if (choice != -1) {
                    out.println("Invalid Input");
                }
            } while (choice < 1 || choice > 4);
            out.clear();
            switch (choice) {
                case 1:
                    printStats();
                    out.println();
                    getTarget().printStats();
                    out.println();
                    break;
                case 2:
                    attack(getTarget());
                    break;
                case 3:
                    heal();

                    break;
                case 4:
                    fireball(getTarget());
                    break;
                default:
                    out.println("Invalid input");
                    break;
            }
        } while (choice == 1);
    }

    public void battle(Enemy e2) { //Runs the battle system here
        out.println(this.getName() + " entered battle with " + e2.getName());
        out.println();
        this.setTarget(e2);
        e2.setTarget(this);
        while (true) {
            this.takeTurn();
            if (e2.getHP() <= 0) {
                out.println(this.getName() + " wins!");
                XP += e2.getXPValue();
                out.println(name + " earned " + e2.getXPValue() + " XP");
                checkLevel();
                break;
            }
            e2.takeTurn();
            if (this.getHP() <= 0) {
                out.println(e2.getName() + " wins!");
                out.println("\nGame Over");
                break;
            }
        }
        this.setTarget(null);
        e2.setTarget(null);
    }

    private void checkLevel() { //Checks if leveled up
        int base = 10 * level * level;
        if (XP >= base) {
            XP -= base;
            level++;
            out.println(name + " leveled up to level " + level + "!");

            int boost = (int) (Math.random() * 3) + 1;
            out.print("HP: " + maxHP + " -> ");
            HP += boost + 3;
            maxHP += boost + 3;
            out.println(maxHP);

            boost = (int) (Math.random() * 3) + 1;
            out.print("MP: " + maxMP + " -> ");
            MP += boost + 1;
            maxMP += boost + 1;
            out.println(maxMP);

            boost = (int) (Math.random() * 2) + 1;
            out.print("Attack: " + attack + " -> ");
            attack += boost;
            out.println(attack);

            boost = (int) (Math.random() * 2) + 1;
            out.print("Defense: " + defense + " -> ");
            defense += boost;
            out.println(defense);
        }
    }

    public void addContent(Handheld h) {
        inventory.add(h);
    }

    public void removeContent(Handheld h) {
        inventory.remove(h);
    }

    public Handheld getContent(int i) {
        return inventory.get(i);
    }

    public int getContentID(Handheld h) {
        if (inventory.contains(h))
            return inventory.indexOf(h);
        else
            return -1;
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public void printInventory(SuperOutput out) {
        out.println("Your inventory:"); // Print inventory
        for (int i = 0; i < inventory.size(); i++) {
            out.print((i + 1) + ") " + inventory.get(i).getName());
            if (inventory.get(i) == weapon)
                out.print(" (Equipped)");
            out.println();
        }
    }
}