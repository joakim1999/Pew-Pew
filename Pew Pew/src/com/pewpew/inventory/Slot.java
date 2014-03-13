package com.pewpew.inventory;

import org.newdawn.slick.Graphics;
import com.pewpew.tile.Tile;

public class Slot extends Tile{
	
	public Slot(int x, int y, int width, int height, int id) {
		super(x, y, width, height, id);
	}
	
	public void render(Graphics gr){
		gr.drawRect(x, y, width, height);
		if(texture != null){
			gr.drawImage(texture, x, y);
		}
	}
}