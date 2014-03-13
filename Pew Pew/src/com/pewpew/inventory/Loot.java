package com.pewpew.inventory;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.entity.Entity;
import com.pewpew.gamestates.Level;
import com.pewpew.startup.Main;

public class Loot extends Inventory{
	ItemPool itemPool;
	Inventory targetInventory;
	Vector2f position = new Vector2f((Main.windowWidth / 2) - (240 / 2), (Main.windowHeight / 2) - (240 / 2));
	
	public Loot(Entity player, ItemPool itemPool, Inventory inventory){
		super(player, 5);
		this.itemPool = itemPool;
		slots = new Slot[5];
		itemsInInventory = itemPool.items;
		targetInventory = inventory;
		weaponSlot = null;
		setUpSlots();
		
		int slotId = 0;
		for(Item i : itemsInInventory){
			if(i != null){
				i.setInventory(this);
				i.slotId = slotId;
				i.id = slotId;
				slotId++;
			}
		}
	}
	
	public void setUpSlots(){
		int x = (Main.windowWidth / 2) - (240 / 2) + 10;
		int y = (Main.windowHeight / 2) - (240 / 2) + 10;
		int counter = 0;
		
		for(int i = 0; i < maxItemSpace;i++){
			slots[i] = new Slot(x, y, slotWidth, slotHeight, i);
			counter++;
			if(counter == 4){
				y += slotHeight * 2;
				x = (Main.windowWidth / 2) - (240 / 2) + 10;
				counter = 0;
			}
			else{
				x += slotWidth * 2;
			}
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta){
		Input in = gc.getInput();
		System.out.println("HAHAH");
		if(isOpened){
			int mouseX = in.getMouseX();
			int mouseY = in.getMouseY();
			for(Item i : itemsInInventory){
				if(i != null){
					i.update(gc, sbg, delta);
				}
			}
			
			if(in.isMousePressed(0)){
				for(Item i : itemsInInventory){
					if(i != null){
						if(mouseX > i.position.x && mouseX < i.position.x + Level.getTileWidth() && mouseY > i.position.y && mouseY < i.position.y + Level.getTileWidth()){
							if(targetInventory.usedSlotSpaces < targetInventory.maxItemSpace && i.inventory == this){
								targetInventory.addItem(i);
								i.setInventory(targetInventory);
								i.disableRendering();
								usedSlotSpaces--;
							}
						}
					}
				}
			}
			
			if(in.isKeyPressed(Input.KEY_ESCAPE)){
				System.out.println("Closing inventory");
				in.clearKeyPressedRecord();
				closeInventory(sbg);
				Level level = (Level)sbg.getState(Main.levelState);
				level.setUpInventoryBar();
				level.resetLoot();
			}
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr){
		if(isOpened){
			gr.drawRect(position.x, position.y, 240, 240);
			gr.setColor(Color.black);
			gr.fillRect(position.x + 1, position.y + 1, 240 - 1, 240 - 1);
			gr.setColor(Color.white);
			
			for(Item i : itemsInInventory){
				if(i != null){
					i.enableRendering();
					i.render(gc, sbg, gr);
				}
			}
			
			for(Slot s : slots){
				s.render(gr);
			}
			
			gr.drawString("Used spaces in inventory:", position.x + (240 / 2) - (gr.getFont().getWidth("Used spaces in inventory:") / 2), position.y + 180);
			gr.drawString(targetInventory.usedSlotSpaces + "/" + targetInventory.maxItemSpace, position.x + (240 / 2) - (gr.getFont().getWidth(targetInventory.usedSlotSpaces + "/" + targetInventory.maxItemSpace) / 2), position.y + 200);
			
		}
	}
}