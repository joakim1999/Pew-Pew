package com.pewpew.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class HealthBar extends Component{
	float healthRemaining = 100;
	
	public HealthBar(int id, Vector2f position, int height) {
		super(id, position, 100, height);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr) {
		gr.setColor(Color.red);
		gr.fillRect(position.x, position.y, healthRemaining, height);
		gr.setColor(Color.white);
		gr.drawRect(position.x, position.y, width, height);
	}
	
	public void setHealthRemaining(float remainingHealth){
		this.healthRemaining = (remainingHealth / 5) * 100;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
	}
}
