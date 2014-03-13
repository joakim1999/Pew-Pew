package com.pewpew.inventory;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.entity.Entity;
import com.pewpew.gamestates.Level;

public abstract class Item{
	Image texture;
	Inventory inventory;
	boolean isRendered = false;
	Integer tileId;
	public int slotId;
	public int id;
	public boolean isDestroyedAfterUse;
	public Vector2f position;
	
	public Item(int id, boolean destroyedAfterUse){
		this.id = id;
		this.isDestroyedAfterUse = destroyedAfterUse;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta){
		if(inventory != null){
			position = new Vector2f(inventory.slots[slotId].x, inventory.slots[slotId].y);
		}
		else{
			position = new Vector2f(Level.getTiles().get(tileId).x, Level.getTiles().get(tileId).y);
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr){
		if(isRendered == true){
			if(inventory != null){
				gr.drawImage(texture, inventory.slots[slotId].x, inventory.slots[slotId].y);
			}
			else{
				gr.drawImage(texture, Level.getTiles().get(tileId).x, Level.getTiles().get(tileId).y);
			}
		}
	}
	
	public void setInventory(Inventory i){
		this.inventory = i;
	}
	
	public void setTexture(Image texture){
		this.texture = texture;
	}
	
	public Image getTexture(){
		return texture;
	}
	
	public abstract void interactWith(Entity target);
	
	public void setTileId(Integer tileId){
		this.tileId = tileId;
	}
	
	public Integer getTileId(){
		return tileId;
	}
	
	public void disableRendering(){
		isRendered = false;
	}
	
	public void enableRendering(){
		isRendered = true;
	}
}
