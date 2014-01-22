package com.pewpew.components;
import java.awt.Font;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;

public class Button{
	Font myFont = new Font("Verdana", Font.BOLD, 40);
	TrueTypeFont font;
	public int id;
	private String text;
	public Vector2f position;
	public int width;
	public int height;
	
	public Button(int id, String text, Vector2f position, int width, int height){
		this.id = id;
		this.text = text;
		this.position = position;
		this.width = width;
		this.height = height;
		font = new TrueTypeFont(myFont, false);
	}
	
	public void changeText(String text){
		this.text = text;
	}
	
	public void render(Graphics gr){
		gr.drawRoundRect(position.x, position.y, width, height, 3);
		font.drawString(position.x, position.y, text);
	}
}
