package com.admiralsbs.RPG;

import java.io.Serializable;
import java.util.ArrayList;

public class Chest extends Stationary implements Serializable {
    private ArrayList<Handheld> contents = new ArrayList<>();
    private static final long serialVersionUID = -9L;

    public Chest() {
        chestConstructor("Chest", 0, 0);
    }

    public Chest(int x, int y) {
        chestConstructor("Chest", x, y);
    }

    public Chest(String n) {
        chestConstructor(n, 0, 0);
    }

    public Chest(String n, int x, int y) {
        chestConstructor(n, x, y);
    }

    private void chestConstructor(String n, int x, int y) {
        assignCoords(x, y);
        letter = 'C';
        name = n;
    }

    public void addContent(Handheld h) {
        contents.add(h);
    }

    public void removeContent(Handheld h) {
        contents.remove(h);
    }

    public Handheld getContent(int i) {
        return contents.get(i);
    }

    public int getContentID(Handheld h) {
        if (contents.contains(h))
            return contents.indexOf(h);
        else
            return -1;
    }

    public int getContentsSize() {
        return contents.size();
    }

    public void printContents(SuperOutput out) {
        if (contents.size() == 0) {
            out.println(name + " is empty");
        } else {
            out.println("Inventory of " + name+ ":"); // Print chest inventory
            for (int i = 0; i < contents.size(); i++) {
                out.print((i + 1) + ") " + contents.get(i).getName());
                out.println();
            }
        }
    }
}