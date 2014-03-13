package com.pewpew.components;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Component {
	public int id;
	public Vector2f position;
	public int width;
	public int height;
	
	public Component(int id, Vector2f position, int width, int height){
		this.id = id;
		this.position = position;
		this.width = width;
		this.height = height;
	}
	
	public abstract void render(GameContainer gc, StateBasedGame sbg, Graphics gr) throws SlickException;
	
	public abstract void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException;
}
