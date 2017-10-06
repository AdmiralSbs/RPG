package com.admiralsbs.RPG;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class RPGMainFrame extends JFrame { // Main method class

    private static final long serialVersionUID = 1L;
    private static RPGMainFrame game = new RPGMainFrame();
    private static SuperScanner k;
    private static SuperOutput out;
    private static ArrayList<Map> maps = new ArrayList<>(); // Holds maps
    private static Map currentMap; // Allows program to know where the player is
    private static Player player; // Interact with player throughout program

    public RPGMainFrame() { // Test
        setTitle("RPG");
        setSize(1300, 691);
        setLocation(100, 100);
        RPGui gui = new RPGui();
        setContentPane(gui);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        k = new SuperScanner(gui.getInput());
        out = new SuperOutput(gui.getPrintOut());
        Item.setInputOutput(k, out);
        Map.setOutputs(out, new SuperOutput(gui.getDisplayOut()));
        int waofc = JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
        String[] chars = {"W", "S", "A", "D", "Q", "E", "X", "1", "2", "3", "4", "5", "6", "7", "8", "9", "ESCAPE"};
        for (String aChar : chars) {
            gui.getInputMap(waofc).put(KeyStroke.getKeyStroke(aChar), aChar);
            gui.getActionMap().put(aChar, new KeyGetter(aChar));
        }
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        int choice;
        game.getContentPane().requestFocusInWindow();
        out.println("1) New game");
        out.println("2) Load game");
        choice = Integer.parseInt(k.getCode("1", "2"));
        if (choice == 2) {
            loadGame();
            out.clear();
        } else {// Choice == 1
            loadMaps(); // Sets up the maps
            out.println("Enter player name: ");
            String name;
            do {
                name = k.nextLine();
            } while (name.equals(""));
            player = new Player(name);
            weaponChoice(); // Gives player a weapon
            player.addContent(new Potion("Health Elixir", Potion.HP, 15, "Regenerates 15 HP"));
            currentMap.addEntity(player, 2, 2);
        }
        currentMap.display();

        movingSystem(); // The loop of the program

        out.println("On my honor as a South Lakes High School student,");
        out.println("I, Jason Watkins, have neither given nor received");
        out.println("unauthorized assistance on this work.");
        out.println("K cool");
        if (player.getHP() > 0)
            saveGame();
        else {
            JOptionPane.showMessageDialog(null, "You died");
        }
        try {
            Thread.sleep(1000);
        } catch (Exception ignore) {
        }
        System.exit(0);
    }

    private class KeyGetter extends AbstractAction {
        //private static final long serialVersionUID = 9999L;
        private final String code;

        public KeyGetter(String i) {
            code = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            k.sendCode(code);
        }

    }

    // Other methods
    private static void movingSystem() { // Allows player to move within map
        String choiceS;
        do {
            out.println("W) Move up");
            out.println("A) Move left");
            out.println("S) Move down");
            out.println("D) Move right");
            out.println("Q) Check stats");
            out.println("E) Check inventory");
            if (currentMap.getPlayerOn() == 1) {
                out.println("X) Enter doorway");
            } else if (currentMap.getPlayerOn() == 2) {
                out.println("X) Heal");
            } else if (currentMap.getPlayerOn() == 3) {
                out.println("X) Use chest");
            }
            out.println("Esc) End game");

            game.getContentPane().requestFocusInWindow();
            if (currentMap.getPlayerOn() > 0)
                choiceS = k.getCode("W", "S", "A", "D", "Q", "E", "X", "ESCAPE");
            else
                choiceS = k.getCode("W", "S", "A", "D", "Q", "E", "ESCAPE");
            out.clear();
            switch (choiceS) {
                case "W":
                    if (player.getY() > 1) {
                        currentMap.moveUp(player);
                    } else {
                        out.println("Error: can't move in that direction");
                    }
                    break;
                case "D":
                    if (player.getX() < currentMap.getWidth()) {
                        currentMap.moveRight(player);
                    } else {
                        out.println("Error: can't move in that direction");
                    }
                    break;
                case "S":
                    if (player.getY() < currentMap.getHeight()) {
                        currentMap.moveDown(player);
                    } else {
                        out.println("Error: can't move in that direction");
                    }
                    break;
                case "A":
                    if (player.getX() > 1) {
                        currentMap.moveLeft(player);
                    } else {
                        out.println("Error: can't move in that direction");
                    }
                    break;
                case "Q":
                    player.printStats();
                    break;
                case "E":
                    inventoryManagement(); // In a method to make things simpler
                    break;
                case "X":
                    if (currentMap.getPlayerOn() == Stationary.DOORWAY) { // Transfer player from one
                        // map to another
                        currentMap.removeEntity(player);
                        Doorway d = (Doorway) currentMap.getCurrentOverlap();
                        player.assignCoords(d.getPartner().getX(), d.getPartner().getY());
                        currentMap = d.getPartner().getMapLoc();
                        currentMap.addEntity(player);
                        currentMap.display();

                    } else if (currentMap.getPlayerOn() == Stationary.HEALER) {
                        player.restore();
                    } else if (currentMap.getPlayerOn() == Stationary.CHEST) {
                        Chest c = (Chest) currentMap.getCurrentOverlap();
                        chestInteraction(c);
                        out.clear();
                    } else
                        out.println("Invalid input");
                    break;
                case "ESCAPE":
                    break;
                default:
                    out.println("Invalid input");
                    break;
            }
            out.println();
        } while (player.getHP() > 0 && (!choiceS.equals("ESCAPE")));
    }

    private static void inventoryManagement() {
        if (player.getInventorySize() == 0) {
            out.println("It appears that you don't have any items...");
            return; // Making sure you have items
        }
        String choiceS;
        do { // Inventory loop
            player.printInventory(out);
            out.println();
            out.println("1) Get information");
            out.println("2) Equip");
            out.println("3) Use");
            out.println("4) Discard");
            out.println("ESC) Exit inventory");
            choiceS = k.getCode("1", "2", "3", "4", "ESCAPE");

            int loc;
            if (choiceS.equals("ESCAPE")) {
                out.println();
                return;
            }
            // Select item, forces acceptable int
            out.println("Item # (enter 0 to go back): ");
            loc = k.nextInt(0, player.getInventorySize());
            if (loc == 0)
                continue;
            loc -= 1; // Because arrays start with 0
            out.clear();
            switch (choiceS) {
                case "1": // Output description and info
                    out.println(player.getContent(loc).getName() + ": " + player.getContent(loc).getDescription());
                    if (player.getContent(loc) instanceof Weapon) {
                        Weapon w = (Weapon) player.getContent(loc);
                        w.printChanges(out);
                    }
                    break;
                case "2": // Assign weapon
                    if (player.getContent(loc) instanceof Weapon) {
                        Weapon w = (Weapon) player.getContent(loc);
                        player.assignWeapon(w);
                    } else
                        out.println("That item cannot be equipped");
                    break;
                case "3":
                    if (player.getContent(loc) instanceof Usable) {
                        out.println("Are you sure?  Type anything to confirm: ");
                        String y = k.nextLine();
                        if (!y.equals("")) {
                            Usable u = (Usable) player.getContent(loc);
                            u.use(player);
                            player.removeContent(player.getContent(loc));
                        }
                    } else
                        out.println("That item cannot be used");
                    break;
                case "4":
                    out.println("Are you sure?  Type anything to confirm: ");
                    String y = k.nextLine();
                    if (!y.equals("")) {
                        player.removeContent(player.getContent(loc));
                    }
                    break;
            }

        } while (!choiceS.equals("ESCAPE"));
        // Finished with inventory
    }

    private static void chestInteraction(Chest c) {
        String choiceS;
        do { // Inventory loop
            c.printContents(out);
            out.println();
            do { // Force an acceptable input
                out.println("1) Get information");
                out.println("2) Take item");
                out.println("3) Put item");
                out.println("99) Exit chest");
                choiceS = k.getCode("1", "2", "3", "ESC");
                if (choiceS.equals("2") && c.getContentsSize() == 0)
                    out.println("There are no items to take");

                if (choiceS.equals("1") && c.getContentsSize() == 0)
                    out.println("There are no items to look at");

                if (choiceS.equals("3") && player.getInventorySize() == 0)
                    out.println("You have no items to put in");
            } while ((choiceS.equals("2") && c.getContentsSize() == 0) || (choiceS.equals("1") &&
                    c.getContentsSize() == 0) || (choiceS.equals("3") && player.getInventorySize() == 0));
            if (!choiceS.equals("ESCAPE")) {
                out.println();
                return;
            }
            int loc;
            if (!choiceS.equals("3")) {
                out.println("Item # (enter 0 to go back): ");
                loc = k.nextInt(0, c.getContentsSize());
            } else {
                player.printInventory(out);
                out.println();
                // Select item, forces acceptable int
                out.println("Item # (enter 0 to go back): ");

                //loc = -1;

                if (player.getWeapon() != null)
                    loc = k.nextInt(0, player.getInventorySize(), player.getContentID(player.getWeapon()) - 1);
                else
                    loc = k.nextInt(0, player.getInventorySize());
                /*if (player.getWeapon() == player.getContent(loc - 1)) {
                    out.println("You can't put that in there!");
                    loc = -1;
                }*/
            }
            if (loc == 0)
                continue;
            loc--; // Because arrays start with 0
            out.clear();
            switch (choiceS) {
                case "1": // Output description and info
                    out.println(c.getContent(loc).getName() + ": " + c.getContent(loc).getDescription());
                    if (c.getContent(loc) instanceof Weapon) {
                        Weapon w = (Weapon) c.getContent(loc);
                        w.printChanges(out);
                    }
                    break;
                case "2": // take
                    out.println(player.getName() + " moved " + c.getContent(loc).getName() + " to their inventory");
                    player.addContent(c.getContent(loc));
                    c.removeContent(c.getContent(loc));
                    break;
                case "3": // put
                    out.println(c.getContent(loc).getName() + " now contains " + c.getContent(loc).getName());
                    c.addContent(player.getContent(loc));
                    player.removeContent(player.getContent(loc));
                    break;
            }
            out.println();

        } while (!choiceS.equals("ESC"));

        // Finished with inventory

    }

    private static void cheating() {// For "debugging"
        out.println("\nWelcome to the debugging system!  Here, you can");
        out.println("all the cool things I either:");
        out.println("1) Not hardcoded into the game");
        out.println("2) Debugging systems and testing");
        out.println("3) Conveniences I left for myself");
        out.println("Check the code for what you can use");
        String choice = k.nextLine();
        if (choice.equals("Add Enemy")) {
            out.println("Name,X,Y: ");
            String in = k.nextLine();
            String[] vals = in.split(",");
            Enemy en = new Enemy(vals[0], Integer.valueOf(vals[1]), Integer.valueOf(vals[2]));
            currentMap.addEntity(en);
            currentMap.display();
        }
        if (choice.equals("Add Healer")) {
            out.println("X,Y: ");
            String in = k.nextLine();
            String[] vals = in.split(",");
            currentMap.addHealer(Integer.valueOf(vals[0]), Integer.valueOf(vals[1]));
            currentMap.display();
        }
    }

    private static void weaponChoice() { // Gives player weapon
        Weapon w = null;
        out.println("Pick your weapon: ");
        out.println("1) Sword: +2 attack, +2 defense");
        out.println("2) Axe: +4 attack");
        out.println("3) Staff: +8 Max MP");
        out.println("4) Magic Orb: +4 Max HP, +4 Max MP");
        out.println("5) Shield: +4 defense");
        int cho = Integer.parseInt(k.getCode("1", "2", "3", "4", "5"));
        out.println();
        switch (cho) {
            case 1:
                w = new Weapon("Short Sword", 0, 0, 2, 2, "Well used, not much use to it.");
                player.addContent(w);
                break;
            case 2:
                w = new Weapon("Rusty Axe", 0, 0, 4, 0, "Better for chopping trees");
                player.addContent(w);
                break;
            case 3:
                w = new Weapon("Rotten Staff", 0, 8, 0, 0, "This looks suspiciously like a branch");
                player.addContent(w);
                break;
            case 4:
                w = new Weapon("Cracked Magic Orb", 4, 4, 0, 0, "Appears kind of glossy");
                player.addContent(w);
                break;
            case 5:
                w = new Weapon("Small Shield", 0, 0, 0, 4, "Wait, is this even a weapon?");
                player.addContent(w);
                break;
        }
        out.clear();
        player.assignWeapon(w);
        out.println();
    }

    // This is all setup stuff that happens

    private static void loadMaps() {
        loadMap1();
        loadMap2();
        doorwaySetup();
        currentMap = maps.get(0);
    }

    private static void loadMap1() {
        currentMap = new Map(5, 5);
        maps.add(currentMap);
        currentMap.addEntity(new Enemy("Enemy"), 4, 2);
        currentMap.addHealer(1, 1);
    }

    private static void loadMap2() {
        currentMap = new Map(6, 8);
        maps.add(currentMap);
        currentMap.addWall(2, 1);
        currentMap.addWall(2, 2);
        currentMap.addWall(2, 3);
        currentMap.addWall(2, 4);
        currentMap.addWall(2, 5);
        currentMap.addWall(2, 6);
        currentMap.addWall(2, 7);
        currentMap.addEntity(new Enemy("Enemy"), 1, 4);
        Chest c = new Chest("Chest");
        c.addContent(new Potion("Magic Elixir", 2, 15, "Regenerates 15 MP"));
        currentMap.addStationary(c, 5, 5);
    }

    private static void doorwaySetup() {
        Doorway d1 = new Doorway(5, 5);
        Doorway d2 = new Doorway(1, 1);
        maps.get(0).addDoorway(d1);
        maps.get(1).addDoorway(d2);
        d1.setPartner(d2);
        d2.setPartner(d1);
    }

    private static void saveGame() throws IOException {
        File fileMaker = null;
        String nameOfFile = "";
        do {
            try {
                out.println("Name of save file: ");
                nameOfFile = k.next();
                fileMaker = new File("Saves" + File.separator + nameOfFile + ".rpgsave");
                if (!fileMaker.createNewFile()) {
                    out.println("That save already exists, do you want to overwrite it?");
                    out.println("Enter anything to confirm: ");
                    String in = k.nextLine();
                    if (in.equals("")) {
                        nameOfFile = "";
                        out.println("Override canceled");
                    }
                }
            } catch (Exception e) {
                if (nameOfFile != null) {
                    out.println("Invalid Input");
                }
                nameOfFile = "";
            }
        } while (nameOfFile.equals(""));

        ObjectOutputStream writer = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileMaker)));
        writer.writeInt(maps.indexOf(currentMap));
        writer.writeObject(player);
        writer.writeObject(maps);
        writer.close();

    }

    private static void loadGame() throws IOException {
        File fileMaker = null;
        String nameOfFile = "";
        do {
            try {
                out.println("Name of load file: ");
                nameOfFile = k.next();
                fileMaker = new File("Saves" + File.separator + nameOfFile + ".rpgsave");
                if (fileMaker.createNewFile()) {
                    out.println("That file does not exist!");
                    nameOfFile = "";
                    fileMaker.delete();
                }
            } catch (Exception e) {
                if (nameOfFile != null) {
                    out.println("Invalid Input");
                }
                nameOfFile = "";
            }
        } while (nameOfFile.equals(""));
        ObjectInputStream reader = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileMaker)));
        int cur = reader.readInt();
        try {
            player = (Player) reader.readObject();
            maps = (ArrayList<Map>) reader.readObject();
        } catch (Exception e) {
            System.out.println("Load failed");
            System.exit(0);
        }
        reader.close();
        currentMap = maps.get(cur);
    }
}
