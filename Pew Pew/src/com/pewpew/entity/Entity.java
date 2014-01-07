package com.pewpew.entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.pewpew.battle.Weapon;
import com.pewpew.levels.Level1;
import com.pewpew.tile.Tile;

public class Entity {
	int id;
	public Weapon defaultWeapon;
	int currentTileId;
	float remainingLife = 5;
	Image image;
	ArrayList<Tile> tiles;
	
	public Entity(int id, int tileId, Image spriteImage){
		this.id = id;
		this.currentTileId = tileId;
		this.image = spriteImage;
	}
	
	public void render(GameContainer gc, Graphics gr){
		tiles = Level1.getTiles();
		for(Tile t : tiles){
			if(t.getId() == currentTileId){
				gr.drawImage(image, t.x, t.y);
			}
		}
	}
	
	public void setTileId(int tileId){
		this.currentTileId = tileId;
	}
	
	public int getTileId(){
		return this.currentTileId;
	}
	
	public void giveDamageTo(Entity target, float damage){
		target.remainingLife -= damage;
	}
	
	public Tile getTile(){
		for(Tile t : tiles){
			if(t.getId() == currentTileId){
				return t;
			}
		}
		return null;
	}
	
	public Image getSpriteImage(){
		return image;
	}
	
	public float getRemainingLife(){
		return remainingLife;
	}
	
	public void restore(){
		remainingLife = 5;
	}
}
