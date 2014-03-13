package com.pewpew.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class HealthBar extends Component{
	float healthRemainingP = 100;
	float healthRemaining;
	
	public HealthBar(int id, Vector2f position, int height) {
		super(id, position, 132, height);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr) throws SlickException {
		gr.drawImage(new Image("resources/heart.png"), position.x, position.y);
		gr.setColor(Color.red);
		gr.fillRect(position.x + 32, position.y, healthRemainingP, height);
		gr.setColor(Color.white);
		gr.drawRect(position.x + 32, position.y, 100, height);
		gr.drawString(healthRemaining + "/" + 5.0, position.x + 32 + (100 / 2) - (gr.getFont().getWidth(healthRemaining + "/" + 5.0) / 2), position.y + (32 / 2 - gr.getFont().getHeight(healthRemaining + "/" + 5.0) / 2));
	}
	
	public void setHealthRemaining(float remainingHealth){
		this.healthRemainingP = (remainingHealth / 5) * 100;
		this.healthRemaining = remainingHealth;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
	}
}
