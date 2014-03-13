package com.pewpew.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class EnergyBar extends Component{
	float energyRemainingP = 100;
	float energyRemaining;
	
	public EnergyBar(int id, Vector2f position, int height) {
		super(id, position, 132, height);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr) throws SlickException {
		gr.setColor(Color.blue);
		gr.fillRect(position.x, position.y, energyRemainingP, height);
		gr.setColor(Color.white);
		gr.drawRect(position.x, position.y, 100, height);
		gr.drawString(energyRemaining + "/" + 10.0, position.x + (100 / 2) - (gr.getFont().getWidth(energyRemaining + "/" + 10.0) / 2), position.y + (32 / 2 - gr.getFont().getHeight(energyRemaining + "/" + 10.0) / 2));
		gr.drawImage(new Image("resources/energysymbol.png"), position.x + 100, position.y);
	}
	
	public void setEnergyRemaining(float remainingEnergy){
		this.energyRemainingP = (remainingEnergy / 10) * 100;
		this.energyRemaining = remainingEnergy;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
	}
}