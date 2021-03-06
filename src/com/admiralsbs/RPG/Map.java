package com.admiralsbs.RPG;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Map implements Serializable { //The big one
    private static final long serialVersionUID = -1L;
    private transient static SuperOutput out;
    private transient static SuperOutput outD;
    private int height;
    private int width;
    private Item[][] locations; //Organizes what thing is in each spot (only allows one per spot)
    private ArrayList<Entity> entities; //Holds all the entities
    private ArrayList<Stationary> stationaries; //Holds all the stationaries
    private Entity placeholder; //Empty spot in locations
    private Stationary currentOverlap; //Doorways are complicated
    private int playerOn;

    public Map(int w, int h) {
        height = h;
        width = w;
        locations = new Item[w + 1][h + 1];
        entities = new ArrayList<>();
        stationaries = new ArrayList<>();
        placeholder = new Entity("%$()@&)(*#*@%()$&#()@DFHUVON$r93v80qmwdzl4t3");
        for (int i = 1; i < w + 1; i++) {
            Arrays.fill(locations[i], placeholder);
        }
        locations[0] = null;
        currentOverlap = null;
    }

    public static void setOutputs(SuperOutput s1, SuperOutput s2) {
        out = s1;
        outD = s2;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getPlayerOn() {
        return playerOn;
    }

/*   
public ArrayList<Entity> getEntities() {
   return entities;
}
public ArrayList<Stationary> getStationaries() {
   return stationaries;
}
*/

    public void display() { //Does the display thing
        locateEntities();
        outD.clear();

        outD.print("+");
        for (int i = 0; i < width; i++) {
            outD.print("------");
        }
        outD.println("-+");
        //Top row done

        for (int i = 1; i < height + 1; i++) {
            outD.print("|");
            for (int k = 1; k < width + 1; k++) {
                outD.print("      ");
            }
            outD.println(" |");
            outD.print("|");
            for (int k = 1; k < width + 1; k++) {
                outD.print("   ");
                outD.print(locations[k][i].getLetter()); //Why letter exists
                outD.print("  ");
            }
            outD.println(" |");
            outD.print("|");
            for (int k = 1; k < width + 1; k++) {
                outD.print("      ");
            }
            outD.println(" |");
        }
        //Body complete
        outD.print("+");
        for (int i = 0; i < width; i++) {
            outD.print("------");
        }
        outD.println("-+");
        //Bottom row done
        outD.println();
    }

    private void locateEntities() { //Places all entities and stationaries into locations
        boolean allcheck = false;
        for (int i = 1; i < width + 1; i++) {
            Arrays.fill(locations[i], placeholder);
        }
        for (Entity entity : entities) {
            if (entity.getX() == 0 || entity.getY() == 0) {
                out.println("Error: " + entity.getName() + " does not have any coordinates.");
            } else {
                locations[entity.getX()][entity.getY()] = entity;
            }
        }
        for (Stationary stationary : stationaries) {
            if (stationary.getX() == 0 || stationary.getY() == 0) {
                out.println("Error: " + stationary.getName() + " does not have any coordinates.");
            } else if (locations[stationary.getX()][stationary.getY()] == placeholder) { //Doesn't allow stationaries to override entities
                locations[stationary.getX()][stationary.getY()] = stationary;
            } else if (locations[stationary.getX()][stationary.getY()] instanceof Player) {
                if (stationary instanceof Doorway) {
                    currentOverlap = stationary;
                    playerOn = Stationary.DOORWAY;
                    allcheck = true;
                }
                if (stationary instanceof Healer) {
                    currentOverlap = stationary;
                    playerOn = Stationary.HEALER;
                    allcheck = true;
                }
                if (stationary instanceof Chest) {
                    currentOverlap = stationary;
                    playerOn = Stationary.CHEST;
                    allcheck = true;
                }
            }
        }
        if (!allcheck) {
            playerOn = 0;
            currentOverlap = null;
        }
    }

    private void checkForEntity(Entity e, int dir) { //Before a move finishes, it checks if the
        if (!entities.contains(e))                          //spot is empty.  Otherwise, it handles it here.
            return;
        if (locations[e.getX()][e.getY()] == placeholder) { //It's open, no worries
            display();
            //currentOverlap = null  Shouldn't be needed
        }
        if (e instanceof Player) {
            if (locations[e.getX()][e.getY()] instanceof Enemy) { //There's an enemy there, battle
                Enemy e2 = (Enemy) locations[e.getX()][e.getY()];
                out.println(e.getName() + " encountered " + e2.getName());
                Player p = (Player) e;

                p.battle(e2);
                if (p.getHP() < e2.getHP()) {
                    removeEntity(p);
                } else {
                    removeEntity(e2);
                }
                display();
            } else if (locations[e.getX()][e.getY()] instanceof Healer) { //There's a healer, cool
                Healer h = (Healer) locations[e.getX()][e.getY()];
                out.println(e.getName() + " encountered " + h.getName());
                //Player p = (Player) e;
                //p.restore();
                display();
            } else if (locations[e.getX()][e.getY()] instanceof Doorway) { //You're on a doorway, cool
                //currentOverlap = (Doorway) locations[e.getX()][e.getY()];  Shouldn't be needed
                Doorway d = (Doorway) locations[e.getX()][e.getY()];
                out.println(e.getName() + " encountered " + d.getName());
                display();
            } else if (locations[e.getX()][e.getY()] instanceof Wall) {
                switch (dir) { //Used to send player back to where they came
                    case 1:
                        e.changeY(1);
                        break;
                    case 2:
                        e.changeX(-1);
                        break;
                    case 3:
                        e.changeY(-1);
                        break;
                    case 4:
                        e.changeX(1);
                        break;
                }
                out.println("Error: cannot move in that direction");
                locateEntities();
            } else if (locations[e.getX()][e.getY()] instanceof Chest) { //There's a chest, cool
                Chest c = (Chest) locations[e.getX()][e.getY()];
                out.println(e.getName() + " encountered " + c.getName());
                display();
            }
        }
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void addEntity(Entity e, int x, int y) {
        e.assignCoords(x, y);
        entities.add(e);
    }

    public void addStationary(Stationary e) {
        stationaries.add(e);
    }

    public void addStationary(Stationary e, int x, int y) {
        e.assignCoords(x, y);
        stationaries.add(e);
    }

    public void addDoorway(Doorway e) {
        e.setMap(this);
        stationaries.add(e);
    }

    public void addDoorway(Doorway e, int x, int y) {
        e.assignCoords(x, y);
        e.setMap(this);
        stationaries.add(e);
    }

    public void addHealer(int x, int y) {
        Healer e = new Healer(x, y);
        stationaries.add(e);
    }

    public void addWall(int x, int y) {
        Wall w = new Wall(x, y);
        stationaries.add(w);
    }

    public void removeEntity(Entity e) {
        if (!entities.contains(e))
            out.println("Error: attempted to remove non-existent entity");
        else {
            locations[e.getX()][e.getY()] = placeholder;
            entities.remove(e);
        }
    }

 //Add, remove, and find used to be complicated because 
 //entities was an array, now it's easier, but they stay anyway

    public void moveRight(Entity e) {
        if (!entities.contains(e)) {
            out.println("Error: this entity is not in the map");
        } else if (e.getX() < width) {
            e.changeX(1);
            checkForEntity(e, 2);
        } else {
            out.println("Error: cannot move in that direction");
        }

    }

    public void moveLeft(Entity e) {
        if (!entities.contains(e)) {
            out.println("Error: this entity is not in the map");
        } else if (e.getX() > 1) {
            e.changeX(-1);
            checkForEntity(e, 4);
        } else {
            out.println("Error: cannot move in that direction");
        }

    }

    public void moveUp(Entity e) {
        if (!entities.contains(e)) {
            out.println("Error: this entity is not in the map");
        } else if (e.getY() > 1) {
            e.changeY(-1);
            checkForEntity(e, 1);
        } else {
            out.println("Error: cannot move in that direction");
        }

    }

    public void moveDown(Entity e) {
        if (!entities.contains(e)) {
            out.println("Error: this entity is not in the map");
        } else if (e.getY() < height) {
            e.changeY(1);
            checkForEntity(e, 3);
        } else {
            out.println("Error: cannot move in that direction");
        }

    }

    public Stationary getCurrentOverlap() {
        return currentOverlap;
    }
}