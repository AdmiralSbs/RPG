package io.github.AdmiralSbs.RPG;
import java.util.*;
import java.io.*;
import javax.swing.*;

public class Quarter_3_Project { //Main method class

   private static JFrame game;
   private static SuperScanner k;
   private static SuperOutput out;
   private static ArrayList<Map> maps = new ArrayList<Map>(); //Holds all the maps
   private static Map currentMap; //Allows program to know where the player is
   private static Player player; //Interact with player throughout program

public static void main(String[] args) throws IOException{
   
      game = new JFrame("RPG");
      game.setSize(1300, 691);
      game.setLocation(100, 100);
      RPGui gui = new RPGui();
      game.setContentPane(gui);
      game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      k = new SuperScanner(gui.getInput());
      out = new SuperOutput(gui.getPrintOut());
      @SuppressWarnings("unused")
      Item setUp = new Item(k, out);
      @SuppressWarnings("unused")
	  Map mSetUp = new Map(out, new SuperOutput(gui.getDisplayOut()));
      setUp = null;
      mSetUp = null;
   	
      game.setVisible(true);
      int choice = -1;
      out.println("1) New game");
      out.println("2) Load game");
      do {
         try {
            choice = k.nextInt();
         }
         catch (Exception e) {
            choice = 0;
         }
         if (choice != 1 && choice != 2) {
            out.println("Invalid input");
         }
      } while (choice != 1 && choice != 2);
      if (choice == 2) {
         loadGame();
         out.clear();
      }
      else {//Choice == 1
         loadMaps(); //Sets up the maps
         out.println("Enter player name: ");
         String name = "";
         name = k.nextLine();
         player = new Player(name);      
         weaponChoice(); //Gives player a weapon
         currentMap.addEntity(player,2,2);
      }
      currentMap.display();
   
      movingSystem(); //The loop of the program
   
      out.println("On my honor as a South Lakes High School student,");
      out.println("I, Jason Watkins, have neither given nor received");
      out.println("unauthorized assistance on this work.");
      out.println("K cool");
      if (player.getHP() > 0)
         saveGame();
      System.exit(0);
   }

//Other methods
   private static void movingSystem() { //Allows player to move within map
      int choice = -1;
      do {
         choice = -1;
         String choiceS = null;       
         out.println("1) Move up");
         out.println("2) Move right");	
         out.println("3) Move down");
         out.println("4) Move left");
         out.println("5) Check stats");
         out.println("6) Check inventory");
         if (currentMap.getPlayerOn() == 1) {
            out.println("11) Enter doorway");
         }
         else if (currentMap.getPlayerOn() == 2) {
            out.println("11) Heal");
         }
         else if (currentMap.getPlayerOn() == 3) {
            out.println("11) Use chest");
         }
         out.println("99) End game");
      
         boolean good = false;
         do {  //Takes in input, forces an int to continue (unless cheatcode42)
            try {
               good = false;
               choiceS = k.nextLine();
            //out.println(choiceS);
               if (choiceS.equals("cheatcode42")) {
                  cheating(); //Send to debugging system
                  choice = 0;
                  good = true;
               }
               else {
                  choice = Integer.parseInt(choiceS);
                  if (choice != -1)
                     good = true;
               }
            }
            catch (Exception e) {
               if (!choiceS.equals("cheatcode42"))
                  out.println("Invalid Input\n");
            }
         } while (!good);
      
         out.clear();
         switch (choice) {
            case 1:
               if (player.getY() > 1) {
                  currentMap.moveUp(player);
               }
               else {
                  out.println("Error: can't move in that direction");
               }
               break;
            case 2:
               if (player.getX() < currentMap.getWidth()) {
                  currentMap.moveRight(player);
               }
               else {
                  out.println("Error: can't move in that direction");
               }
               break;
            case 3: 
               if (player.getY() < currentMap.getHeight()) {
                  currentMap.moveDown(player);
               }
               else {
                  out.println("Error: can't move in that direction");
               }
               break;
            case 4:
               if (player.getX() > 1) {
                  currentMap.moveLeft(player);
               }
               else {
                  out.println("Error: can't move in that direction");
               }
               break;
            case 5:
               player.printStats();
               break;
            case 6:
               inventoryManagement(); //In a method to make things simpler
               break;
            case 11:
               if (currentMap.getPlayerOn() == 1) { //Transfer player from one map to another
                  currentMap.removeEntity(player);
                  Doorway d = (Doorway) currentMap.getCurrentOverlap();
                  player.assignCoords(d.getPartner().getX(),d.getPartner().getY());
                  currentMap = d.getPartner().getMapLoc();
                  currentMap.addEntity(player);
                  currentMap.display();
               
               }
               else if (currentMap.getPlayerOn() == 2) {
                  player.restore();
               }
               else if (currentMap.getPlayerOn() == 3) {
                  Chest c = (Chest) currentMap.getCurrentOverlap();
                  chestInteraction(c);
               }
               else 
                  out.println("Invalid input");
               break;
            case 99:
               break;
            default:
               out.println("Invalid input");
               break;
         }
         out.println();
      } while(player.getHP() > 0 && (choice != 99));
   } 	

   private static void inventoryManagement() {
      if (player.getInventorySize() == 0) {
         out.println("It appears that you don't have any items...");  //Making sure you have items
         return;
      }
   
      int choice = 0;
      do { //Inventory loop
         do { //Force an acceptable input
            out.println("Inventory:");  //Print inventory
            for (int i = 0; i < player.getInventorySize(); i++) {
               out.print((i+1) + ") " + player.getContent(i).getName());
               if (player.getContent(i) == player.getWeapon())
                  out.print(" (Equiped)");
               out.println();
            }
            out.println();
            
            out.println("1) Get information");
            out.println("2) Equip");	
            out.println("3) Use");
            out.println("4) Discard");
            out.println("99) Exit inventory");
            choice = 0;
            try {
               choice = k.nextInt();
            }
            catch (Exception e) {}
            if (!(choice >= 1 && choice <= 4) && (choice != 99) && (choice != -1)) {
               out.println("Invalid input");
            }
         } while (!(choice >= 1 && choice <= 4) && (choice != 99));
      
         int loc = -1;
         if (choice != 99) {     
            do {  //Select item, forces acceptable int
               out.println("Item # (enter 0 to go back): ");
               loc = -1;
               try {
                  loc = k.nextInt();
               }
               catch (Exception e) {}
               if (!(loc >= 0 && loc <= player.getInventorySize()) && loc != -1) {
                  out.println("Invalid input");
               }
            } while (!(loc >= 0 && loc <= player.getInventorySize()));
         }
         if (loc != 0) {
            loc -= 1; //Because arrays start with 0
            out.clear();
            switch (choice) {
               case 1: //Output description and info
                  out.println(player.getContent(loc).getName() + ": " + player.getContent(loc).getDescription());
                  if (player.getContent(loc) instanceof Weapon) {
                     Weapon w = (Weapon) player.getContent(loc);
                     if (w.getHPChange() > 0) {
                        out.println("HP Increase: " + w.getHPChange());
                     }
                     if (w.getMPChange() > 0) {
                        out.println("MP Increase: " + w.getMPChange());
                     }
                     if (w.getAttackChange() > 0) {
                        out.println("Attack Increase: " + w.getAttackChange());
                     }
                     if (w.getDefenseChange() > 0) {
                        out.println("Defense Increase: " + w.getDefenseChange());
                     }
                  }
                  break;
               case 2: //assign weapon
                  if (player.getContent(loc) instanceof Weapon) {
                     Weapon w = (Weapon) player.getContent(loc);
                     player.assignWeapon(w);
                  }
                  break;
               case 3:  //Nothing can be used, so...
                  out.println("That item cannot be used");
                  break;
               case 4:
                  out.println("Are you sure?  Type anything to confirm: ");
                  String y = k.nextLine();
                  if (!y.equals("")) {
                     player.removeContent(player.getContent(loc));
                  }
                  break;
            }
            if (choice != 99)
               out.println();
         }
      } while (choice != 99);
   
   //Finished with inventory
   } 	

   private static void chestInteraction(Chest c) {
      if (c.getContentsSize() == 0) {
         out.println(c.getName() + " is empty");
      }
      else {
         out.println("Inventory:");  //Print inventory
         for (int i = 0; i < c.getContentsSize(); i++) {
            out.print((i+1) + ") " + c.getContent(i).getName());
            out.println();
         }
      }
      out.println();
      int choice = 0;
      do { //Inventory loop
         do { //Force an acceptable input
            out.println("1) Get information");
            out.println("2) Take item");	
            out.println("3) Put item");
            out.println("99) Exit chest");
            choice = 0;
            try {
               choice = k.nextInt();
            }
            catch (Exception e) {}
            if (!(choice >= 1 && choice <= 3) && (choice != 99) && (choice != -1)) {
               out.println("Invalid input");
            }
            if ((choice == 1 || choice == 2) && c.getContentsSize() == 0) {
               if (choice == 2)
                  out.println("There are no items to take");
               if (choice == 1)
                  out.println("There are no items to look at");
            }
            if (choice == 3 && player.getInventorySize() == 0) {
               out.println("You have no items to put in");
            }
         } while (!(choice >= 1 && choice <= 3) && (choice != 99));
      
         int loc = -1;
         if (choice == 2 || choice == 1) {     
            do {  //Select item, forces acceptable int
               out.println("Item # (enter 0 to go back): ");
               loc = -1;
               try {
                  loc = k.nextInt();
               }
               catch (Exception e) {}
               if (!(loc >= 0 && loc <= c.getContentsSize()) && (choice != -1)) {
                  out.println("Invalid input");
               }
            } while (!(loc >= 0 && loc <= c.getContentsSize()));
         }
      
         if (choice == 3) {
            out.println("Your inventory:");  //Print inventory
            for (int i = 0; i < player.getInventorySize(); i++) {
               out.print((i+1) + ") " + player.getContent(i).getName());
               if (player.getContent(i) == player.getWeapon())
                  out.print(" (Equiped)");
               out.println();
            }
         
            do {  //Select item, forces acceptable int
               out.println("Item # (enter 0 to go back): ");
               loc = -1;
               try {
                  loc = k.nextInt();
               }
               catch (Exception e) {}
               if (!(loc >= 0 && loc <= player.getInventorySize()) && (choice != -1)) {
                  out.println("Invalid input");
               }
               if (player.getWeapon() == player.getContent(loc)) {
                  out.println("You can't put that in there!");
               }
            } while (!(loc >= 0 && loc <= player.getInventorySize()));
         
         }
      
         if (loc != 0) {
            loc--; //Because arrays start with 0
            switch (choice) {
               case 1: //Output description and info
                  out.println(c.getContent(loc).getName() + ": " + c.getContent(loc).getDescription());
                  if (c.getContent(loc) instanceof Weapon) {
                     Weapon w = (Weapon) c.getContent(loc);
                     if (w.getHPChange() > 0) {
                        out.println("HP Increase: " + w.getHPChange());
                     }
                     if (w.getMPChange() > 0) {
                        out.println("MP Increase: " + w.getMPChange());
                     }
                     if (w.getAttackChange() > 0) {
                        out.println("Attack Increase: " + w.getAttackChange());
                     }
                     if (w.getDefenseChange() > 0) {
                        out.println("Defense Increase: " + w.getDefenseChange());
                     }
                  }
                  break;
               case 2: //take
                  out.println(player.getName() + " moved " + c.getContent(loc).getName() + " to their inventory");
                  player.addContent(c.getContent(loc));
                  c.removeContent(c.getContent(loc));
                  break;
               case 3:  //put
                  out.println(c.getContent(loc).getName() + " now contains " + c.getContent(loc).getName());
                  c.addContent(player.getContent(loc));
                  player.removeContent(player.getContent(loc));
                  break;
            }
            out.println();
         }
      } while (choice != 99);
   
   //Finished with inventory
   
   }

   private static void cheating() {//For "debugging" (it works as both, why not?)
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
         Healer en = new Healer(Integer.valueOf(vals[0]), Integer.valueOf(vals[1]));
         currentMap.addStationary(en);
         currentMap.display();
      }
   }

   private static void weaponChoice() { //Gives player weapon
      int cho = -1;
      Weapon w = null;
      do {
         out.println("Pick your weapon: ");
         out.println("1) Sword: +2 attack, +2 defense");
         out.println("2) Axe: +4 attack");
         out.println("3) Staff: +8 Max MP");
         out.println("4) Magic Orb: +4 Max HP, +4 Max MP");
         out.println("5) Shield: +4 defense");      
         try {
            cho = k.nextInt();
         }
         catch (Exception e) {
            cho = 0;
         }
         out.println();
         switch (cho) {
            case 1:
               w = new Weapon("Short Sword",0,0,2,2,"Well used, not much use to it.");
               player.addContent(w);            
               break;
            case 2:
               w = new Weapon("Rusty Axe",0,0,4,0,"Better for chopping trees");
               player.addContent(w); 
               break;
            case 3:
               w = new Weapon("Rotten Staff",0,8,0,0,"This looks suspiciously like a branch");
               player.addContent(w); 
               break;
            case 4:
               w = new Weapon("Cracked Magic Orb",4,4,0,0,"Appears kind of glossy");
               player.addContent(w); 
               break;
            case 5:
               w = new Weapon("Small Shield",0,0,0,4,"Wait, is this even a weapon?");
               player.addContent(w); 
               break;
            case -1:
               break;
            default:
               out.println("Invalid input");
         }
      } while (cho < 1 || cho > 5);
      out.clear();
      player.assignWeapon(w);
      out.println();
   }

/*private static void updateMapIDs() { //Probably does nothing, don't worry about it
   for (int i = 0; i < maps.size(); i++) {
      maps.get(i).setID(i);
   }
}*/
//This is all setup stuff that happens
   private static void loadMaps() {
      loadMap1();
      loadMap2();
   //updateMapIDs();
      doorwaySetup();
      currentMap = maps.get(0);
   }

   private static void loadMap1() {
      currentMap = new Map(5,5);
      maps.add(currentMap);
      currentMap.addEntity(new Enemy("Enemy"),4,2);
      currentMap.addHealer(1,1);
   }

   private static void loadMap2() {
      currentMap = new Map(6,8);
      maps.add(currentMap);
      currentMap.addWall(2,1);
      currentMap.addWall(2,2);
      currentMap.addWall(2,3);
      currentMap.addWall(2,4);
      currentMap.addWall(2,5);
      currentMap.addWall(2,6);
      currentMap.addWall(2,7);
      currentMap.addEntity(new Enemy("Enemy"),1,4);
   }

   private static void doorwaySetup() {
      Doorway d1 = new Doorway(5,5);
      Doorway d2 = new Doorway(1,1);
      maps.get(0).addDoorway(d1);
      maps.get(1).addDoorway(d2);
      d1.setPartner(d2);
      d2.setPartner(d1);
   }

   private static void saveGame() throws IOException{
      File fileMaker = null;
      String nameOfFile = "";
      do {
         try {
            out.println("Name of save file: ");
            nameOfFile = k.next();
            fileMaker = new File(nameOfFile + ".save");
            if (!fileMaker.createNewFile()) {
               out.println("That save already exists, do you want to overwrite it?");
               out.println("Enter anything to confirm: ");
               String in = k.nextLine();
               if (in.equals("")) {
                  nameOfFile = "";
                  out.println("Override canceled");
               }
            }
         }
         catch(Exception e) {
            if (nameOfFile != null) {
               out.println("Invalid Input");
            }
            nameOfFile = "";
         }
      } while(nameOfFile.equals(""));
   
      ObjectOutputStream writer = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileMaker)));
      writer.writeInt(maps.size());
      writer.writeInt(maps.indexOf(currentMap));
      writer.writeObject(player);
      for (int i = 0; i < maps.size(); i++) {
         Map cMap = maps.get(i);
         writer.writeObject(cMap);
      /*ArrayList<Entity> entAL = cMap.getEntities();
      ArrayList<Stationary> statAL = cMap.getStationaries();
      for (int k = 0; k < entAL.size(); k++) {
         writer.writeObject(entAL.get(k));
      }
      for (int k = 0; k < statAL.size(); k++) {
         writer.writeObject(statAL.get(k));
      }*/
      }
   //writer.writeObject(obj);
      writer.close();
   
   }

   private static void loadGame() throws IOException{
      File fileMaker = null;
      String nameOfFile = "";
      do {
         try {
            out.println("Name of load file: ");
            nameOfFile = k.next();
            fileMaker = new File(nameOfFile + ".save");
            if (fileMaker.createNewFile()) {
               out.println("That file does not exist!");
               nameOfFile = "";
               fileMaker.delete();
            }
         }
         catch(Exception e) {
            if (nameOfFile != null) {
               out.println("Invalid Input");
            }
            nameOfFile = "";
         }
      } while(nameOfFile.equals(""));
      ObjectInputStream reader = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileMaker)));
      int num = reader.readInt();
      int cur = reader.readInt();
      try {
         player = (Player) reader.readObject();
         for (int i = 0; i < num; i++) {
            Map m = (Map) reader.readObject();
            maps.add(m);
         }
      }
      catch (Exception e) {
         System.out.println("Load failed");
         System.exit(0);
      }
      reader.close();
      currentMap = maps.get(cur);
   }
}