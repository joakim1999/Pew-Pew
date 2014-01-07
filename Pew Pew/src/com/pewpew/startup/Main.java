package com.pewpew.startup;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.battle.Battle;
import com.pewpew.levels.Level1;


public class Main extends StateBasedGame{
	public static int windowHeight = 640;
	public static int windowWidth = 640;
	public static int levelState = 0;
	public static int battleState = 1;
	
	public Main(String name){
		super(name);
		this.addState(new Level1());
		this.addState(new Battle(windowWidth, windowHeight));
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(levelState).init(gc, this);
    	this.enterState(levelState);
	}
	
	public static void main(String args[])
            throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new Main("Pew Pew - 0.0.2 Alpha"));
         app.setDisplayMode(windowWidth, windowHeight, false);
         app.start();
    }
}
