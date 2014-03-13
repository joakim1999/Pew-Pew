package com.pewpew.battle;

import com.pewpew.entity.Entity;
import com.pewpew.inventory.Item;

public class Weapon extends Item{
	int damage;
	double missChance;
	
	public Weapon(int id, WeaponType type){
		super(id, false);
		this.missChance = type.getChanceForMiss();
		this.damage = type.getDefaultDamage();
	}
	
	public int getDamage(){
		return damage;
	}
	
	public double getChanceMiss(){
		return missChance;
	}
	
	public void setDamage(int damage){
		this.damage = damage;
	}
	
	public void interactWith(Entity target){
		target.defaultWeapon = this;
	}
	
	public void setChanceForMiss(int chanceForMiss){
		this.missChance = chanceForMiss;
	}
}
