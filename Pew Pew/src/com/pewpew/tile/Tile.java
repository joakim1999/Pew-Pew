package com.pewpew.tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Tile {
	public int x;
	public int y;
	public int width;
	public int height;
	public int id;
	public Image texture;
	public boolean isBlocked = false;
	
	public Tile(int x, int y, int width, int height, int id){
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void render(Graphics gr){
		//gr.drawRect(x, y, width, height);
		if(texture != null){
			gr.drawImage(texture, x, y);
		}
		//gr.drawString(Integer.toString(id), x, y + 10);
	}
	
	public int getId(){
		return id;
	}
	
	public void setTileTexture(Image texture){
		this.texture = texture;
	}
}
