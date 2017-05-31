package io.github.AdmiralSbs.RPG;
import java.io.Serializable;

public class Entity extends Item implements Serializable {
   private static final long serialVersionUID = -3L;
   protected String name;
   protected int HP;
   protected int maxHP;
   protected int MP;
   protected int maxMP;
   protected int attack;
   protected int defense;
   protected Entity target;
   protected Weapon weapon; //The last few are for equipment
   protected int maxHPTotal;
   protected int maxMPTotal;
   protected int attackTotal;
   protected int defenseTotal;

   public Entity() {
      name = "Entity";      
      maxHP = (int)(Math.random()*21)+40;
      maxMP = (int)(Math.random()*21) + 10;
      attack = (int)(Math.random()*11) + 5;
      defense = (int)(Math.random()*11) + 5;
      target = null;
      weapon = null;
      letter = '\u2022';
      x = 0;
      y = 0;
      updateTotal();
      HP = maxHPTotal;
      MP = maxMPTotal;
   }    

   public Entity(String nm){
      name = nm;
      maxHP = (int)(Math.random()*21)+40;
      maxMP = (int)(Math.random()*21) + 10;
      attack = (int)(Math.random()*11) + 5;
      defense = (int)(Math.random()*11) + 5;
      target = null;
      letter = '\u2022';
      assignCoords(0,0);
      updateTotal();
      HP = maxHPTotal;
      MP = maxMPTotal;
   }

   public Entity(int x, int y) {
      name = "Entity";
      maxHP = (int)(Math.random()*21)+40;
      maxMP = (int)(Math.random()*21) + 10;
      attack = (int)(Math.random()*11) + 5;
      defense = (int)(Math.random()*11) + 5;
      target = null;
      letter = '\u2022';
      assignCoords(x,y);
      updateTotal();
      HP = maxHPTotal;
      MP = maxMPTotal;
   }

   public Entity(String nm, int x, int y) {
      name = nm;
      maxHP = (int)(Math.random()*21)+40;
      maxMP = (int)(Math.random()*21) + 10;
      attack = (int)(Math.random()*11) + 5;
      defense = (int)(Math.random()*11) + 5;
      target = null;
      letter = '\u2022';
      assignCoords(x,y);
      updateTotal();
      HP = maxHPTotal;
      MP = maxMPTotal;
   }

   public void printStats() {
      if (weapon != null) {
         out.println("Name: " + name);
         out.println("HP: " + HP + "/" + maxHP + " (+" + weapon.getHPChange() + ")");
         out.println("MP: " + MP + "/" + maxMP + " (+" + weapon.getMPChange() + ")");
         out.println("Attack: " + attack + " (+" + weapon.getAttackChange() + ")");
         out.println("Defense: " + defense + " (+" + weapon.getDefenseChange() + ")");
      }
      else {
         out.println("Name: " + name);
         out.println("HP: " + HP + "/" + maxHP);
         out.println("MP: " + MP + "/" + maxMP);
         out.println("Attack: " + attack);
         out.println("Defense: " + defense);
      }
   }

   public String getName() {
      return name;
   }

   public int getHP() {
      return HP;
   }

   public int getMaxHP() {
      return maxHP;
   }

   public int getMP() {
      return MP;
   }

   public int getMaxMP() {
      return maxMP;
   }

   public int getAttack() {
      return attack;
   }

   public int getDefense() {
      return defense;
   }
	
   public int getMaxHPTotal() {
      return maxHPTotal;
   }

   public int getMaxMPTotal() {
      return maxMPTotal;
   }

   public int getAttackTotal() {
      return attackTotal;
   }

   public int getDefenseTotal() {
      return defenseTotal;
   }

   public Entity getTarget() {
      return target;
   }
   
   public Weapon getWeapon() {
      return weapon;
   }

   public void subtractHP(int i) {
      HP -= i;
      if (HP<0) 
         HP = 0;
   }

   public void heal() {
      HP += 20;
      if (HP>=maxHPTotal) {
         HP = maxHPTotal;
         out.println(name + " healed to max HP"); 
      }
      else
         out.println(name + " healed 20 HP");
      MP -= 5;
      out.println();
   }

   public void attack(Entity target) {
      double roll = Math.random();
      out.println(name + " attacked " + target.getName()); 
      if (roll < 0.1) {
         out.println("The attack missed!");
      }
      else if (roll > 0.9) {
         int damage = (int) (2 * Math.max(1,attackTotal * ((Math.random()*1.5) + .5) - target.getDefenseTotal()));
         out.println("Critical hit!");
         target.subtractHP(damage);
         out.println(this.name + " did " + damage + " to " + target.getName());
      }
      else {
         int damage = (int) Math.max(1,this.attackTotal * ((Math.random()*1.5) + .5) - target.getDefenseTotal());
         target.subtractHP(damage);		
         out.println(this.name + " did " + damage + " to " + target.getName());      
      }
      out.println();
   }

   public void fireball(Entity target) {
      out.println(this.name + " fireballed " + target.getName()); 
      int damage = (int)(attackTotal * ((Math.random() + 1)));
      target.subtractHP(damage);		
      out.println(this.name + " did " + damage + " to " + target.getName());    
      out.println();
      MP -= 6;
   }

   public void setTarget(Entity t) {
      target = t;
   }

   public void restore() { //Full heal
      HP = maxHPTotal;
      MP = maxMPTotal;
      out.println(name + " is fully restored!");
   }

   public void assignWeapon(Weapon w) { //Equips weapon, deals with the possible situations
      if (weapon == null) {
         weapon = w;
         out.println(name + " equiped " + w.getName());
         updateTotal();
      }
      else if (weapon == w) {
         out.println("You already have this weapon equipped");
      }
      else {
         out.println("Are you sure you want to replace " + weapon.getName() + " with " + w.getName() + "?");
         out.print("Press enter to continue, type anything to cancel: ");
         String in = k.nextLine();
         if (in.equals("")) {
            weapon = w;
            out.println(name + " equiped " + w.getName());
            updateTotal();
         }
         else {
            out.println("Weapon was not changed");
         }
      }
   }
	
   public void updateTotal() { //Updates totals
      if (weapon != null) {
         maxHPTotal = maxHP + weapon.getHPChange();
         maxMPTotal = maxMP + weapon.getMPChange();
         attackTotal = attack + weapon.getAttackChange();
         defenseTotal = defense + weapon.getDefenseChange();
      }
      else {
         maxHPTotal = maxHP;
         maxMPTotal = maxMP;
         attackTotal = attack;
         defenseTotal = defense;
      }
   }
}