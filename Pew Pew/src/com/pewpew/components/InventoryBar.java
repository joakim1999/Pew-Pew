package com.pewpew.components;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import com.pewpew.gamestates.Level;
import com.pewpew.inventory.Inventory;
import com.pewpew.inventory.Item;
import com.pewpew.inventory.Slot;

public class InventoryBar {
	Vector2f position;
	Inventory inventory;
	
	public InventoryBar(Inventory i, Vector2f position){
		this.inventory = i;
		this.position = position;
	}
	
	public void setUpSlots(){
		int x = 0;
		int y = 0;
		
		for(int i = 0; i < inventory.maxItemSpace;i++){
			inventory.slots[i] = new Slot((int)position.x + x, (int)position.y + y, inventory.slotWidth, inventory.slotHeight, i);
			x += inventory.slotWidth;
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta){ 
		if(inventory.isOpened == false){
			Input in = gc.getInput();
			int mouseX = in.getMouseX();
			int mouseY = in.getMouseY();
			for(Item i : inventory.itemsInInventory){
				if(i != null){
					i.update(gc, sbg, delta);
				}
			}
			if(in.isMousePressed(0)){
				for(Item i : inventory.itemsInInventory){
					if(i != null){
						if(mouseX > i.position.x && mouseX < i.position.x + Level.getTileWidth() && mouseY > i.position.y && mouseY < i.position.y + Level.getTileWidth()){
							i.interactWith(inventory.target);
							if(i.isDestroyedAfterUse == true){
								System.out.println("destroying item");
								i.disableRendering();
								int slotChange = 0;
								inventory.itemsInInventory[i.slotId] = null;
								inventory.usedSlotSpaces--;
							}
						}
					}
				}
			}
			
			int slotId = 0;
			for(Item i : inventory.itemsInInventory){
				if(i != null){
					i.slotId = slotId;
					i.id = slotId;
					slotId++;
				}
			}
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr){
		if(inventory.isOpened == false){
			for(Item i : inventory.itemsInInventory){
				if(i != null){
					i.render(gc, sbg, gr);
				}
			}
			
			for(Slot s : inventory.slots){
				s.render(gr);
			}
		}
	}
}
