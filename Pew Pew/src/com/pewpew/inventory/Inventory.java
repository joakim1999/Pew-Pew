package com.pewpew.inventory;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import com.pewpew.entity.Entity;
import com.pewpew.gamestates.Level;
import com.pewpew.startup.Main;


public class Inventory{
	public Entity target;
	public Slot weaponSlot;
	public int maxItemSpace;
	public int usedSlotSpaces;
	public Item[] itemsInInventory;
	public Slot[] slots;
	public int slotWidth = Level.getTileWidth();
	public int slotHeight = Level.getTileHeight();
	public int itemSlot;
	public boolean isOpened = false;
	
	public Inventory(Entity ent, int maxSpace){
		this.target = ent;
		this.maxItemSpace = maxSpace;
		itemsInInventory = new Item[maxSpace];
		slots = new Slot[maxItemSpace];
		setUpSlots();
		weaponSlot = new Slot((Main.windowWidth / 2) - (slotWidth / 2), 500, slotWidth, slotHeight, slots.length);
		usedSlotSpaces = 0;
	}
	
	public void setUpSlots(){
		int x = 192;
		int y = 100;
		int counter = 0;
		
		for(int i = 0; i < maxItemSpace;i++){
			slots[i] = new Slot(x, y, slotWidth, slotHeight, i);
			counter++;
			if(counter == 4){
				y += slotHeight * 2;
				x = 192;
				counter = 0;
			}
			else{
				x += slotWidth * 2;
			}
		}
	}
	
	public void addItem(Item i){
		if(usedSlotSpaces < maxItemSpace){
			itemsInInventory[usedSlotSpaces] = i;
			i.slotId = usedSlotSpaces;
			usedSlotSpaces++;
		}
	}
	
	public void openInventory(){
		isOpened = true;
	}
	
	public void closeInventory(StateBasedGame sbg){
		isOpened = false;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta){
		Input in = gc.getInput();
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
							i.interactWith(target);
							if(i.isDestroyedAfterUse == true){
								System.out.println("destroying item");
								i.disableRendering();
								int slotId = 0;
								itemsInInventory[i.slotId] = null;
								usedSlotSpaces--;
							}
						}
					}
				}
			}
			if(in.isKeyPressed(Input.KEY_ESCAPE)){
				System.out.println("Closing inventory");
				closeInventory(sbg);
				Level level = (Level)sbg.getState(Main.levelState);
				level.setUpInventoryBar();
			}
		}
		
		if(in.isKeyPressed(Input.KEY_I)){
			if(isOpened == true){
				System.out.println("Closing inventory");
				closeInventory(sbg);
				Level level = (Level)sbg.getState(Main.levelState);
				level.setUpInventoryBar();
			}
			else{
				setUpSlots();
				openInventory();
				for(Item i : itemsInInventory){
					if(i != null){
						i.enableRendering();
					}
				}
			}
		}
		int slotId = 0;
		for(Item i : itemsInInventory){
			if(i != null){
				i.slotId = slotId;
				i.id = slotId;
				slotId++;
			}
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr){
		if(isOpened == true){
			gr.drawRect(0, 0, Main.windowWidth - 1, Main.windowHeight - 1);
			gr.setColor(Color.black);
			gr.fillRect(1, 1, Main.windowWidth - 2, Main.windowHeight - 2);
			gr.setColor(Color.white);
			if(target.defaultWeapon != null){
				gr.drawImage(target.defaultWeapon.getTexture(), weaponSlot.x, weaponSlot.y);
			}
			gr.drawRect(weaponSlot.x, weaponSlot.y, slotWidth, slotHeight);
			
			for(Item i : itemsInInventory){
				if(i != null){
					i.render(gc, sbg, gr);
				}
			}
			
			for(Slot s : slots){
				s.render(gr);
			}
		}
	}
}
