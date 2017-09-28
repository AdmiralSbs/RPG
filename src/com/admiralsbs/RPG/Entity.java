package com.admiralsbs.RPG;

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
	protected Weapon weapon; // The last few are for equipment
	/*protected int getMaxHPTotal();
	protected int getMaxMPTotal();
	protected int getAttackTotal();
	protected int getDefenseTotal();*/

	public Entity() {
	    entityConstructor("Entity", 0, 0);
	}

	public Entity(String nm) {
		entityConstructor(nm, 0, 0);
	}

	public Entity(int x, int y) {
		entityConstructor("Entity", x, y);
	}

	public Entity(String nm, int x, int y) {
		entityConstructor(nm, x, y);
	}

	private void entityConstructor(String nm, int x, int y) {
		name = nm;
		initializeStats();
		target = null;
		letter = '\u2022';
		assignCoords(x, y);
	}

	private void initializeStats() {
		maxHP = (int) (Math.random() * 21) + 40;
		maxMP = (int) (Math.random() * 21) + 10;
		attack = (int) (Math.random() * 11) + 5;
		defense = (int) (Math.random() * 11) + 5;
		HP = getMaxHPTotal();
		MP = getMaxMPTotal();
	}

	public void printStats() {
		if (weapon != null) {
			out.println("Name: " + name);
			out.println("HP: " + HP + "/" + maxHP + " (+" + weapon.getHPChange() + ")");
			out.println("MP: " + MP + "/" + maxMP + " (+" + weapon.getMPChange() + ")");
			out.println("Attack: " + attack + " (+" + weapon.getAttackChange() + ")");
			out.println("Defense: " + defense + " (+" + weapon.getDefenseChange() + ")");
		} else {
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

	public int getMaxMP() { return maxMP; }

	public int getAttack() { return attack; }

	public int getDefense() { return defense; }

	public int getMaxHPTotal() {
		if (weapon != null)
		    return maxHP + weapon.getHPChange();
		else
		    return maxHP;
	}

	public int getMaxMPTotal() {
        if (weapon != null)
            return maxMP + weapon.getMPChange();
        else
            return maxMP;
	}

	public int getAttackTotal() {
        if (weapon != null)
            return attack + weapon.getAttackChange();
        else
            return attack;
	}

	public int getDefenseTotal() {
        if (weapon != null)
            return defense + weapon.getDefenseChange();
        else
            return defense;
	}

	public Entity getTarget() {
		return target;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void subtractHP(int i) {
		HP -= i;
		if (HP < 0)
			HP = 0;
	}

	public void heal() {
		HP += 20;
		if (HP >= getMaxHPTotal()) {
			HP = getMaxHPTotal();
			out.println(name + " healed to max HP");
		} else
			out.println(name + " healed 20 HP");
		MP -= 5;
		out.println();
	}

	public void attack(Entity target) {
		double roll = Math.random();
		out.println(name + " attacked " + target.getName());
		if (roll < 0.1)
			out.println("The attack missed!");
		else if (roll > 0.9) {
			int damage = (int) (2 * Math.max(1, getAttackTotal() * ((Math.random() * 1.5) + .5) - target.getDefenseTotal()));
			out.println("Critical hit!");
			target.subtractHP(damage);
			out.println(this.name + " did " + damage + " to " + target.getName());
		} else {
			int damage = (int) Math.max(1, this.getAttackTotal() * ((Math.random() * 1.5) + .5) - target.getDefenseTotal());
			target.subtractHP(damage);
			out.println(this.name + " did " + damage + " to " + target.getName());
		}
		out.println();
	}

	public void fireball(Entity target) {
		out.println(this.name + " fireballed " + target.getName());
		int damage = (int) (getAttackTotal() * ((Math.random() + 1)));
		target.subtractHP(damage);
		out.println(this.name + " did " + damage + " to " + target.getName());
		out.println();
		MP -= 6;
	}

	public void setTarget(Entity t) {
		target = t;
	}

	public void restore() { // Full heal
		HP = getMaxHPTotal();
		MP = getMaxMPTotal();
		out.println(name + " is fully restored!");
	}

	public void assignWeapon(Weapon w) { // Equips weapon, deals with the
											// possible situations
		if (weapon == null) {
			weapon = w;
			out.println(name + " equipped " + w.getName());
		} else if (weapon == w)
			out.println("You already have this weapon equipped");
		else {
			out.println("Are you sure you want to replace " + weapon.getName() + " with " + w.getName() + "?");
			out.print("Press enter to continue, type anything to cancel: ");
			String in = k.nextLine();
			if (in.equals("")) {
				weapon = w;
				out.println(name + " equipped " + w.getName());
			} else
				out.println("Weapon was not changed");
		}
	}

	public void editStat(int i, int p) {
		switch (i) {
		case Potion.HP:
			HP += p;
			if (HP > maxHP)
				HP = maxHP;
			break;
		case Potion.MP:
			MP += p;
			if (MP > maxMP)
				MP = maxMP;
			break;
		case Potion.MAXHP:
			maxHP += p;
			HP += p;
			break;
		case Potion.MAXMP:
			maxMP += p;
			MP += p;
			break;
		case Potion.ATTACK:
			attack += p;
			break;
		case Potion.DEFENSE:
			defense += p;
			break;
		default:
			System.err.println("Attempted to edit unknown stat");
		}
	}
}