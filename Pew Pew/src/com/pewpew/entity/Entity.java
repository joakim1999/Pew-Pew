package com.pewpew.entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.pewpew.battle.Weapon;
import com.pewpew.gamestates.Level;
import com.pewpew.inventory.Item;
import com.pewpew.tile.Tile;

public class Entity {
	int id;
	public EnemyType eType;
	public Item drop = null;
	public Float experienceDrop = null;
	public int defaultTileId;
	public Weapon defaultWeapon;
	int currentTileId;
	public boolean isAI;
	public boolean needMoving;
	float remainingLife = 5;
	float remainingEnergy = 10;
	Image image;
	public Vector2f position;
	ArrayList<Tile> tiles;
	
	public Entity(int id, int tileId, Image spriteImage, boolean isAI, boolean needMoving){
		this.id = id;
		this.currentTileId = tileId;
		this.image = spriteImage;
		this.isAI = isAI;
		this.needMoving = needMoving;
		this.defaultTileId = tileId;
		this.position = new Vector2f(Level.getTiles().get(tileId).x, Level.getTiles().get(tileId).y);
	}
	
	public Entity(int id, int tileId, Image spriteImage, boolean isAI, boolean needMoving, EnemyType enemyType){
		this.id = id;
		this.currentTileId = tileId;
		this.image = spriteImage;
		this.isAI = isAI;
		this.needMoving = needMoving;
		this.defaultTileId = tileId;
		this.eType = enemyType;
		this.position = new Vector2f(Level.getTiles().get(tileId).x, Level.getTiles().get(tileId).y);
	}
	
	public void render(GameContainer gc, Graphics gr){
		tiles = Level.getTiles();
		for(Tile t : tiles){
			if(t.getId() == currentTileId){
				gr.drawImage(image, position.x, position.y);
			}
		}
	}
	
	public void update(){
		tiles = Level.getTiles();
		if(drop != null){
			drop.setTileId(getTileId());
		}
		this.position = new Vector2f(Level.getTiles().get(currentTileId).x, Level.getTiles().get(currentTileId).y);
		if(Level.getTiles() != null){
			getTile().isOccupied = true;
		}
	}
	
	public void setDrop(Item drop){
		this.drop = drop;
	}
	
	public void setExperienceDrop(float experience){
		experienceDrop = experience;
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
	
	public int getId(){
		return id;
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
	
	public float getRemainingEnergy(){
		return remainingEnergy;
	}
	
	public void restore(){
		remainingLife = 5;
	}
	
	public void restore(int health){
		this.remainingLife += health;
	}
}
