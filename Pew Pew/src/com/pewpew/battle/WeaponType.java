package com.pewpew.battle;

public enum WeaponType{
	SWORD(2, 25);
	
	int damage;
	int missChance;
	
	WeaponType(int damage, int missChance){
		this.missChance = missChance;
		this.damage = damage;
	}
	
	public int getChanceForMiss(){
		return missChance;
	}
	
	public int getDefaultDamage(){
		return damage;
	}
}
