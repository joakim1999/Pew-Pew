package com.pewpew.battle;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class Weapon{
	int damage;
	double missChance;
	Image weaponTexture;
	boolean isRendered;
	
	public Weapon(WeaponType type){
		this.missChance = type.getChanceForMiss();
		this.damage = type.getDefaultDamage();
	}
	
	public void render(Graphics gr, Vector2f position){
		gr.drawImage(weaponTexture, position.x, position.y);
	}
	
	public void setTexture(Image texture){
		this.weaponTexture = texture;
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
	
	public void setChanceForMiss(int chanceForMiss){
		this.missChance = chanceForMiss;
	}
}
