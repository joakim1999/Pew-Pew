package com.pewpew.gamestates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.battle.Battle;
import com.pewpew.battle.Weapon;
import com.pewpew.battle.WeaponType;
import com.pewpew.entity.Entity;
import com.pewpew.startup.Main;
import com.pewpew.tile.*;

public class Level extends BasicGameState{
	Music levelSong;
	Graphics currentGraphics;
	static ArrayList<Tile> tiles;
	public ArrayList<Entity> entities;
	public File[] maps = Main.filesInFolder;
	static int windowWidth = Main.windowWidth;
	static int windowHeight = Main.windowHeight;
	boolean needRestarting = true;
	int tileWidth = 32;
	int tileHeight = 32;
	int mapNumber;
	int turn = 0;
	int AIMove = 0;
	public Entity thePlayer;
	public int playerDefaultTile;
	public int enemyDefaultTile = 20;
	boolean startBattle = true;
	public int finishTileId = 0;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		levelSong = new Music("src/LevelMusic1.wav");
	}
	
	public void initializeTiles(){
		tiles.clear();
		int x = 0;
		int y = 0;
		int id = 0;
		System.out.println("Got " + getTilesX() + " tiles in X");
		System.out.println("Got " + getTilesY() + " tiles in Y");
		for(int i = 0;i < getTilesY();i++){
			System.out.println("Looping on x");
			for(int n = 0;n < getTilesX();n++){
				System.out.println("Made a tile");
				tiles.add(new Tile(x, y, tileWidth, tileHeight, id));
				x += tileWidth;
				id++;
			}
			y += tileHeight;
			x = 0;
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		for(Entity ent : entities){
			System.out.println("Rendered Ents id: " + ent.getId());
			ent.render(gc, g);
		}
		for(Tile t : tiles){
			t.render(g);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2)
			throws SlickException {
		for(Entity ent : entities){
			ent.update();
		}
		Input in = gc.getInput();
		
		if(sbg.getCurrentState().getID() == this.getID() && levelSong.playing() == false){
			levelSong.loop();
		}
		checkMovement(in, sbg);
		
		if(thePlayer.getTileId() == finishTileId){
			if(mapNumber <= maps.length - 1){
				try {
					initializeTiles();
					entities.clear();
					turn = 0;
					loadMap(maps[mapNumber]);
					mapNumber++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				gc.getInput().resume();
				levelSong.stop();
				sbg.enterState(Main.winState);
			}
		}
	}
	
	public void startBattle(Entity player, Entity opponent, StateBasedGame sbg){
		Battle b = (Battle)sbg.getState(Main.battleState);
		b.setMatch(thePlayer, opponent);
		sbg.enterState(1);
		thePlayer.setTileId(thePlayer.getTileId());
		startBattle = false;
	}
	
	public void checkMovement(Input in, StateBasedGame sbg){
		if(turn == 0){
			if(in.isKeyPressed(Input.KEY_UP)){
				if(thePlayer.getTile().y != 0 && getTiles().get(thePlayer.getTileId() - getTilesX()).isBlocked == false){
					thePlayer.setTileId(thePlayer.getTileId() - getTilesX());
					turn++;
				}
			}
			else if(in.isKeyPressed(Input.KEY_DOWN)){
				if(thePlayer.getTile().y != windowHeight - tileHeight && getTiles().get(thePlayer.getTileId() + getTilesX()).isBlocked == false){
					thePlayer.setTileId(thePlayer.getTileId() + getTilesX());
					turn++;
				}
			}
			else if(in.isKeyPressed(Input.KEY_LEFT)){
				if(thePlayer.getTile().x != 0 && getTiles().get(thePlayer.getTileId() -1).isBlocked == false){
					thePlayer.setTileId(thePlayer.getTileId() - 1);
					turn++;
				}
			}
			else if(in.isKeyPressed(Input.KEY_RIGHT)){
				if(thePlayer.getTile().x != windowWidth - tileWidth && getTiles().get(thePlayer.getTileId() + 1).isBlocked == false){
					thePlayer.setTileId(thePlayer.getTileId() + 1);
					turn++;
				}
			}
			else if(in.isKeyPressed(Input.KEY_UP)){
				if(thePlayer.getTile().x != windowWidth - tileWidth){
					thePlayer.setTileId(thePlayer.getTileId() + 1);
					turn++;
				}
			}
		}
		else{
			Random rand = new Random();
			AIMove = rand.nextInt(4);
			for(Entity ent : entities){
				if(ent.isAI == true){
					if(ent.getTile().y > thePlayer.getTile().y){
						if(ent.getTile().y != 0 && getTiles().get(ent.getTileId() - 20).isBlocked == false){
							ent.setTileId(ent.getTileId() - getTilesX());
						}
					}
					if(ent.getTile().y < thePlayer.getTile().y){
						if(ent.getTile().y != windowHeight - tileHeight && getTiles().get(ent.getTileId() + 20).isBlocked == false){
							ent.setTileId(ent.getTileId() + getTilesX());
						}
					}
					if(ent.getTile().x > thePlayer.getTile().x){
						if(ent.getTile().x != 0 && getTiles().get(ent.getTileId() - 1).isBlocked == false){
							ent.setTileId(ent.getTileId() - 1);
						}
					}
					if(ent.getTile().x < thePlayer.getTile().x){
						if(ent.getTile().x != windowWidth - tileWidth && getTiles().get(ent.getTileId() + 1).isBlocked == false){
							ent.setTileId(ent.getTileId() + 1);
						}
					}
					
					if(ent.getTile().x == thePlayer.getTile().x && ent.getTile().y == thePlayer.getTile().y){
						ent.setTileId(ent.getTileId() - getTilesX());
						startBattle(thePlayer, ent, sbg);
					}
				}
			}
			turn = 0;
		}
	}
	
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException{
		super.enter(gc, sbg);
		if(needRestarting == true){
			System.out.println("Restarting game thingy");
			tiles = new ArrayList<Tile>();
			entities = new ArrayList<Entity>();
			initializeTiles();
			turn = 0;
			mapNumber = 0;
			
			try {
				loadMap(maps[mapNumber]);
				mapNumber++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			needRestarting = false;
		}
	}
	
	public void loadMap(File f) throws IOException, SlickException{
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		int tile = 0;
		int id = 0;
		while((line = br.readLine()) != null){
			char[] letters = line.toCharArray();
			for(char l : letters){
				System.out.println("Current char is: " + "'" + l + "'");
				if(l == 'P'){
					playerDefaultTile = tile;
					thePlayer = new Entity(id, tile, new Image("com/pewpew/image/RollingBall.png"), false);
					thePlayer.defaultWeapon = new Weapon(WeaponType.SWORD);
					entities.add(thePlayer);
					System.out.println("Player placed on tile: " + tile);
					tile++;
					id++;
				}
				else if(l == 'B'){
					getTiles().get(tile).isBlocked = true;
					getTiles().get(tile).setTileTexture(new Image("/src/com/pewpew/image/Golmers.png"));
					System.out.println("Tile: " + tile + "is blocked");
					tile++;
				}
				else if(l == 'E'){
					entities.add(new Entity(id, tile, new Image("com/pewpew/image/RollingEnemy.png"), true));
					tile++;
					id++;
				}
				else if(l == 'F'){
					finishTileId = tile;
					tile++;
				}
				else if(l == ' '){
					tile++;
				}
			}
			if(letters.length != getTilesX()){
				tile += (getTilesX()) - letters.length;
				System.out.println("added" + ((getTilesX() - 1) - letters.length));
			}
		}
		System.out.println("Map loaded");
		br.close();
	}
	
	public void destroy(Entity ent){
		ent = null;
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
