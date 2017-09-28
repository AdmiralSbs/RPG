package com.admiralsbs.RPG;

import java.util.*;  //entities.get(loc).getY() (in case I need it again)
import java.io.Serializable;

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
        entities = new ArrayList<Entity>();
        stationaries = new ArrayList<Stationary>();
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
                    playerOn = 1;
                    allcheck = true;
                }
                if (stationary instanceof Healer) {
                    currentOverlap = stationary;
                    playerOn = 2;
                    allcheck = true;
                }
                if (stationary instanceof Chest) {
                    currentOverlap = stationary;
                    playerOn = 3;
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
        int loc = findEntity(e);                   //spot is empty.  Otherwise, it handles it here.
        if (loc == -1)
            return;
        if (locations[e.getX()][e.getY()] == placeholder) { //It's open, no worries
            display();
            //currentOverlap = null  Shouldn't be needed
        } else if ((e instanceof Player) && (locations[e.getX()][e.getY()] instanceof Enemy)) { //There's an enemy there, battle
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
        } else if ((e instanceof Player) && (locations[e.getX()][e.getY()] instanceof Healer)) { //There's a healer, cool
            Healer h = (Healer) locations[e.getX()][e.getY()];
            out.println(e.getName() + " encountered " + h.getName());
            //Player p = (Player) e;
            //p.restore();
            display();
        } else if ((e instanceof Player) && (locations[e.getX()][e.getY()] instanceof Doorway)) { //You're on a doorway, cool
            //currentOverlap = (Doorway) locations[e.getX()][e.getY()];  Shouldn't be needed
            Doorway d = (Doorway) locations[e.getX()][e.getY()];
            out.println(e.getName() + " encountered " + d.getName());
            display();
        } else if ((e instanceof Player) && (locations[e.getX()][e.getY()] instanceof Wall)) {
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
        } else if ((e instanceof Player) && (locations[e.getX()][e.getY()] instanceof Chest)) { //There's a chest, cool
            Chest c = (Chest) locations[e.getX()][e.getY()];
            out.println(e.getName() + " encountered " + c.getName());
            display();
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
        int loc = findEntity(e);
        if (loc == -1)
            out.println("Error: attempted to remove non-existent entity");
        else {
            locations[e.getX()][e.getY()] = placeholder;
            entities.remove(e);
        }
    }

    private int findEntity(Entity e) { //Add, remove, and find used to be complicated because
        if (entities.contains(e))       //entities was an array, now it's easier, but they stay anyway
            return entities.indexOf(e);
        else
            return -1;
    }

    public void moveRight(Entity e) {
        int loc = findEntity(e);
        if (loc == -1) {
            out.println("Error: this entity is not in the map");
        } else if (e.getX() < width) {
            //locations[e.getX()][e.getY()] = placeholder;
            entities.get(loc).changeX(1);
            checkForEntity(entities.get(loc), 2);
        } else {
            out.println("Error: cannot move in that direction");
        }

    }

    public void moveLeft(Entity e) {
        int loc = findEntity(e);
        if (loc == -1) {
            out.println("Error: this entity is not in the map");
        } else if (e.getX() > 1) {
            //locations[e.getX()][e.getY()] = placeholder;
            entities.get(loc).changeX(-1);
            checkForEntity(entities.get(loc), 4);
        } else {
            out.println("Error: cannot move in that direction");
        }

    }

    public void moveUp(Entity e) {
        int loc = findEntity(e);
        if (loc == -1) {
            out.println("Error: this entity is not in the map");
        } else if (e.getY() > 1) {
            //locations[e.getX()][e.getY()] = placeholder;
            entities.get(loc).changeY(-1);
            checkForEntity(entities.get(loc), 1);
        } else {
            out.println("Error: cannot move in that direction");
        }

    }

    public void moveDown(Entity e) {
        int loc = findEntity(e);
        if (loc == -1) {
            out.println("Error: this entity is not in the map");
        } else if (e.getY() < height) {
            //locations[e.getX()][e.getY()] = placeholder;
            entities.get(loc).changeY(1);
            checkForEntity(entities.get(loc), 3);
        } else {
            out.println("Error: cannot move in that direction");
        }

    }

    public Stationary getCurrentOverlap() {
        return currentOverlap;
    }
}