package com.pewpew.levels;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.battle.Battle;
import com.pewpew.battle.Weapon;
import com.pewpew.battle.WeaponType;
import com.pewpew.entity.Entity;
import com.pewpew.startup.Main;
import com.pewpew.tile.*;

public class Level1 extends BasicGameState{
	Graphics currentGraphics;
	static ArrayList<Tile> tiles;
	static int windowWidth = Main.windowWidth;
	static int windowHeight = Main.windowHeight;
	int tileWidth = 32;
	int tileHeight = 32;
	int turn = 0;
	int AIMove = 0;
	public Entity thePlayer;
	public Entity enemy;
	boolean startBattle = false;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		tiles = new ArrayList<Tile>();
		initializeTiles();
		
		thePlayer = new Entity(0, 39, new Image("com/pewpew/image/RollingBall.png"));
		thePlayer.defaultWeapon = new Weapon(WeaponType.SWORD);
		enemy = new Entity(0, 20, new Image("com/pewpew/image/RollingEnemy.png"));
	}
	
	public void initializeTiles(){
		int x = 0;
		int y = 0;
		int id = 0;
		System.out.println("Got " + getTilesX() + " tiles in X");
		System.out.println("Got " + getTilesY() + " tiles in Y");
		for(int i = 0;i < getTilesX();i++){
			System.out.println("Looping on x");
			for(int n = 0;n < getTilesY();n++){
				System.out.println("Made a tile");
				tiles.add(new Tile(x, y, tileWidth, tileHeight, id));
				y += tileHeight;
				id++;
			}
			y = 0;
			x += tileWidth;
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		for(Tile t : tiles){
			t.render(g);
		}
		thePlayer.render(gc, g);
		enemy.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2)
			throws SlickException {
		Input in = gc.getInput();
		
		if(turn == 0){
			checkMovement(in, thePlayer);
		}
		else{
			checkMovement(in, enemy);
		}
		
		if(startBattle == true){
			sbg.enterState(1);
			thePlayer.setTileId(39);
			enemy.setTileId(20);
			startBattle = false;
		}
	}
	
	public void checkMovement(Input in, Entity affectedEntity){
		if(turn == 0){
			if(in.isKeyPressed(Input.KEY_UP)){
				if(affectedEntity.getTile().y != 0){
					affectedEntity.setTileId(affectedEntity.getTileId() - 1);
					turn++;
				}
			}
			else if(in.isKeyPressed(Input.KEY_DOWN)){
				if(affectedEntity.getTile().y != windowHeight - tileHeight){
					affectedEntity.setTileId(affectedEntity.getTileId() + 1);
					turn++;
				}
			}
			else if(in.isKeyPressed(Input.KEY_LEFT)){
				if(affectedEntity.getTile().x != 0){
					affectedEntity.setTileId(affectedEntity.getTileId() - 20);
					turn++;
				}
			}
			else if(in.isKeyPressed(Input.KEY_RIGHT)){
				if(affectedEntity.getTile().x != windowWidth - tileWidth){
					affectedEntity.setTileId(affectedEntity.getTileId() + 20);
					turn++;
				}
			}
			else if(in.isKeyPressed(Input.KEY_UP)){
				if(affectedEntity.getTile().x != windowWidth - tileWidth){
					affectedEntity.setTileId(affectedEntity.getTileId() + 20);
					turn++;
				}
			}
		}
		else{
			Random rand = new Random();
			AIMove = rand.nextInt(4);
			
			if(affectedEntity.getTile().y > thePlayer.getTile().y){
				if(affectedEntity.getTile().y != 0){
					affectedEntity.setTileId(affectedEntity.getTileId() - 1);
				}
			}
			if(affectedEntity.getTile().y < thePlayer.getTile().y){
				if(affectedEntity.getTile().y != windowHeight - tileHeight){
					affectedEntity.setTileId(affectedEntity.getTileId() + 1);
				}
			}
			if(affectedEntity.getTile().x > thePlayer.getTile().x){
				if(affectedEntity.getTile().x != 0){
					affectedEntity.setTileId(affectedEntity.getTileId() - 20);
				}
			}
			if(affectedEntity.getTile().x < thePlayer.getTile().x){
				if(affectedEntity.getTile().x != windowWidth - tileWidth){
					affectedEntity.setTileId(affectedEntity.getTileId() + 20);
				}
			}
			
			if(affectedEntity.getTile().x == thePlayer.getTile().x && affectedEntity.getTile().y == thePlayer.getTile().y){
				affectedEntity.setTileId(affectedEntity.getTileId() - 1);
				startBattle = true;
			}
			turn = 0;
		}
	}
	
	public int getTilesX(){
		return windowWidth/tileWidth;
	}
	
	public int getTilesY(){
		return windowHeight/tileHeight;
	}

	@Override
	public int getID() {
		return Main.levelState;
	}
	
	public static ArrayList<Tile> getTiles(){
		return tiles;
	}
}
