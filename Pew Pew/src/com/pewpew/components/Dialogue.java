package com.pewpew.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Dialogue extends Component{
	String dialogueText;
	
	public Dialogue(int id, String dialogueText, Vector2f position, int width, int height) {
		super(id, position, width, height);
		this.dialogueText = dialogueText;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr) {
		gr.drawRect(position.x, position.y, width, height);
		gr.setColor(Color.black);
		gr.fillRect(position.x + 1, position.y + 1, width - 1, height - 1);
		gr.setColor(Color.white);
		gr.drawString(dialogueText, position.x, position.y);
	}
	
	public void setDialogueText(String text){
		this.dialogueText = text;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
	}
}
