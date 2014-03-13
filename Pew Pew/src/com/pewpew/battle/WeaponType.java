package com.pewpew.battle;

public enum WeaponType{
	WOODSWORD(2, 0.50),
	IRONSWORD(3, 0.50),
	DIAMONDSWORD(5, 0.50);
	
	int damage;
	double missChance;
	
	WeaponType(int damage, double missChance){
		this.missChance = missChance;
		this.damage = damage;
	}
	
	public double getChanceForMiss(){
		return missChance;
	}
	
	public int getDefaultDamage(){
		return damage;
	}
}
