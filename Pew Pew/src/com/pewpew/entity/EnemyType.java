package com.pewpew.entity;

public enum EnemyType {
	EASY(1),
	MEDIUM(2),
	HARD(5);
	
	int maxNumberOfLootItems;
	EnemyType(int maxLoot){
		this.maxNumberOfLootItems = maxLoot;
	}
	
	public int getMaxNumberOfLootItems(){
		return maxNumberOfLootItems;
	}
}
