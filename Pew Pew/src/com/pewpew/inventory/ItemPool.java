package com.pewpew.inventory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.pewpew.battle.Weapon;
import com.pewpew.battle.WeaponType;
import com.pewpew.entity.Entity;

public class ItemPool{
	public Item[] items = new Item[5];
	int usedItemSpaces = 0;
	File itemPoolFile;

	public ItemPool(File itemPoolFile) throws NumberFormatException, SlickException, IOException {
		this.itemPoolFile = itemPoolFile;
		loadItemPoolFile();
	}
	
	public void loadItemPoolFile() throws SlickException, NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(itemPoolFile));
		String line;
		while((line = br.readLine()) != null){
			final String[] args = line.split(" ");
			if(line.startsWith("[HealItem]")){
				Item i = new Item(usedItemSpaces, Boolean.parseBoolean(args[3])){
					@Override
					public void interactWith(Entity target) {
						target.restore(Integer.parseInt(args[2]));
						if(target.getRemainingLife() > 5){
							target.restore();
						}
					}
				};
				i.setTexture(new Image(args[1]));
				items[usedItemSpaces] = i;
				usedItemSpaces++;
			}
			else if(line.startsWith("[Weapon]")){
				System.out.println("Making weapon");
				Weapon w = new Weapon(usedItemSpaces, WeaponType.valueOf(args[2]));
				w.setTexture(new Image(args[1]));
				w.enableRendering();
				items[usedItemSpaces] = w;
				usedItemSpaces++;
			}
		}
	}
}
