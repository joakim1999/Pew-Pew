package com.pewpew.tile;
import org.newdawn.slick.Graphics;

public class Tile {
	public int x;
	public int y;
	public int width;
	public int height;
	public int id;
	
	public Tile(int x, int y, int width, int height, int id){
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void render(Graphics gr){
		gr.drawRect(x, y, width, height);
	}
	
	public int getId(){
		return id;
	}
}
