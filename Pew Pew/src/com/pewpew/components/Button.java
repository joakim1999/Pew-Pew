package com.pewpew.components;
import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import com.pewpew.other.Action;

public class Button extends Component{
	Font myFont = new Font("Verdana", Font.BOLD, 40);
	TrueTypeFont font;
	public int id;
	private String text;
	public Vector2f position;
	public int width;
	public int height;
	Action action;
	
	public Button(int id, String text, Vector2f position, int width, int height, Action action){
		super(id, position, width, height);
		this.id = id;
		this.text = text;
		this.position = position;
		this.width = width;
		this.height = height;
		this.action = action;
		font = new TrueTypeFont(myFont, false);
	}
	
	public void changeText(String text){
		this.text = text;
	}
	
	public void setFont(Font f){
		TrueTypeFont font = new TrueTypeFont(f,  false);
		this.font = font;
	}
	
	public void render(Graphics gr){
		gr.drawRoundRect(position.x, position.y, width, height, 3);
		font.drawString(position.x + (width / 2) - (font.getWidth(text) / 2), position.y + (height / 2) - (font.getHeight(text) / 2), text);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr) {
		gr.drawRect(position.x, position.y, width, height);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Input in = gc.getInput();
		int mouseX = in.getMouseX();
		int mouseY = in.getMouseY();
		if(in.isMouseButtonDown(0)){
			if(mouseX >= this.position.x && mouseX <= this.position.x + this.width && mouseY > this.position.y && mouseY < this.position.y + this.height){
				action.doAction();
			}
		}
	}
}
