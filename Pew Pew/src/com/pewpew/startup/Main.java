package com.pewpew.startup;
import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.battle.Battle;
import com.pewpew.gamestates.GameOver;
import com.pewpew.gamestates.Level;
import com.pewpew.gamestates.Menu;
import com.pewpew.gamestates.Winning;


public class Main extends StateBasedGame{
	public static int windowHeight = 640;
	public static int windowWidth = 640;
	public static int levelState = 0;
	public static int battleState = 1;
	public static int menuState = 2;
	public static int gameOverState = 3;
	public static int winState = 4;
	public static File[] filesInFolder;
	public static File folder = new File("maps");
	
	public Main(String name){
		super(name);
		this.addState(new Level());
		this.addState(new Battle(windowWidth, windowHeight));
		this.addState(new Menu());
		this.addState(new GameOver());
		this.addState(new Winning());
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		gc.getInput().pause();
		this.getState(menuState).init(gc, this);
    	this.enterState(menuState);
	}
	public static void main(String args[])
            throws SlickException
    {
		if(!folder.exists()){
			folder.mkdir();
		}
		filesInFolder = folder.listFiles();
        AppGameContainer app = new AppGameContainer(new Main("Pew Pew - 0.0.3 Alpha"));
        app.setDisplayMode(windowWidth, windowHeight, false);
        app.start();
    }
}
