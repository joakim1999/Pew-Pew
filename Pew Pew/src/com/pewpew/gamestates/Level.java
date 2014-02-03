package com.pewpew.gamestates;

import java.io.BufferedReader;
import java.io.File;
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
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.battle.Battle;
import com.pewpew.battle.Weapon;
import com.pewpew.battle.WeaponType;
import com.pewpew.components.Dialogue;
import com.pewpew.entity.Entity;
import com.pewpew.entity.StoryBasedEntity;
import com.pewpew.startup.Main;
import com.pewpew.tile.*;

public class Level extends BasicGameState{
	Music levelSong;
	Graphics currentGraphics;
	ArrayList<String> currentDialogue;
	static ArrayList<Tile> tiles;
	public ArrayList<Entity> entities;
	public File[] maps = Main.filesInFolder;
	static int windowWidth = Main.windowWidth;
	static int windowHeight = Main.windowHeight;
	public static int storyTileWidth = Main.windowWidth;
	public static int storyTileHeight = 20;
	boolean needRestarting = true;
	int dialogueLineNumber = 0;
	int timer;
	public static boolean isDialogue = false;
	int tileWidth = 32;
	int tileHeight = 32;
	int mapNumber;
	Dialogue d = new Dialogue(0, "", new Vector2f(0, Main.windowHeight - storyTileHeight), storyTileWidth - 1, storyTileHeight - 1);
	public Entity thePlayer;
	boolean startBattle = true;
	public int finishTileId = 0;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		levelSong = new Music("resources/LevelMusic1.wav");
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
		
		if(isDialogue == true){
			d.setDialogueText(currentDialogue.get(dialogueLineNumber));
			d.render(gc, sb, g);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2)
			throws SlickException {
		if(sbg.getCurrentState().getID() == this.getID() && levelSong.playing() == false){
			levelSong.loop();
		}
		
		if(isDialogue == true){
			if(gc.getInput().isMousePressed(0) /*&& dialogueLineNumber < currentDialogue.size()*/){
				dialogueLineNumber++;
				System.out.println("size = " + currentDialogue.size());
				System.out.println("dialogueLineNumber = " + dialogueLineNumber);
			}
			else{
				timer -= arg2;
			}
			if(dialogueLineNumber >= currentDialogue.size()){
				gc.getInput().clearKeyPressedRecord();
				dialogueLineNumber = 0;
				isDialogue = false;
				return;
			}
		}
		
		else{
			try {
				entityUpdate(gc, sbg);
				doMove(gc, sbg);
				checkFinish(gc, sbg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void entityUpdate(GameContainer gc, StateBasedGame sbg) throws SlickException, IOException{
		for(Entity ent : entities){
			ent.update();
		}
	}
	
	public void checkFinish(GameContainer gc, StateBasedGame sbg) throws IOException, SlickException{
		if(thePlayer.getTileId() == finishTileId){
			if(mapNumber <= maps.length - 1){
				initializeTiles();
				entities.clear();
				loadMap(maps[mapNumber]);
				mapNumber++;
				StoryBasedEntity sbe = new StoryBasedEntity(0, "tristan", 355, new Image("resources/RollingBallNpc.png"), true);
				entities.add(sbe);
				sbe.readDialogueFile(new File("resources/dialogues/" + sbe.name + "_" + mapNumber + ".dfp"));
				currentDialogue = sbe.getDialogue();
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
	
	public void doMove(GameContainer gc, StateBasedGame sbg){
		Input in = gc.getInput();
		if(in.isKeyPressed(Input.KEY_UP)){
			if(thePlayer.position.y != 0 && getTiles().get(thePlayer.getTileId() - getTilesX()).isBlocked == false){
				thePlayer.setTileId(thePlayer.getTileId() - getTilesX());
				moveAIs(sbg);
			}
		}
		if(in.isKeyPressed(Input.KEY_DOWN)){
			if(thePlayer.position.y != windowHeight - tileHeight && getTiles().get(thePlayer.getTileId() + getTilesX()).isBlocked == false){
				thePlayer.setTileId(thePlayer.getTileId() + getTilesX());
				moveAIs(sbg);
			}
		}
		if(in.isKeyPressed(Input.KEY_LEFT)){
			if(thePlayer.position.x != 0 && getTiles().get(thePlayer.getTileId() -1).isBlocked == false){
				thePlayer.setTileId(thePlayer.getTileId() - 1);
				moveAIs(sbg);
			}
		}
		if(in.isKeyPressed(Input.KEY_RIGHT)){
			if(thePlayer.position.x != windowWidth - tileWidth && getTiles().get(thePlayer.getTileId() + 1).isBlocked == false){
				thePlayer.setTileId(thePlayer.getTileId() + 1);
				moveAIs(sbg);
			}
		}
		if(in.isKeyPressed(Input.KEY_UP)){
			if(thePlayer.position.x != windowWidth - tileWidth){
				thePlayer.setTileId(thePlayer.getTileId() + 1);
				moveAIs(sbg);
			}
		}
		for(Entity ent : entities){
			if(ent.isAI == true){
				if(thePlayer.position.x == ent.position.x && thePlayer.position.y == ent.position.y){
					if(ent instanceof StoryBasedEntity){
						thePlayer.setTileId(thePlayer.getTileId() + 1);
						interactWith(gc, (StoryBasedEntity)ent);
					}
					else{
						interactWith(ent, sbg);
						startBattle(thePlayer, ent, sbg);
					}
				}
			}
		}
	}
	
	public void moveAIs(StateBasedGame sbg){
		for(Entity ent : entities){
			if(ent.isAI == true && ent.needMoving == true){
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
			}
		}
	}
	
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException{
		super.enter(gc, sbg);
		if(needRestarting == true){
			System.out.println("Restarting game thingy");
			currentDialogue = new ArrayList<String>();
			tiles = new ArrayList<Tile>();
			entities = new ArrayList<Entity>();
			initializeTiles();
			mapNumber = 0;
			
			try {
				loadMap(maps[mapNumber]);
				mapNumber++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				StoryBasedEntity sbe = new StoryBasedEntity(0, "tristan", 48, new Image("resources/RollingBallNpc.png"), true);
				entities.add(sbe);
				sbe.readDialogueFile(new File("resources/dialogues/" + sbe.name + "_" + mapNumber + ".dfp"));
				currentDialogue = sbe.getDialogue();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			needRestarting = false;
		}
	}
	
	public void interactWith(GameContainer gc, StoryBasedEntity sEnt){
		//dialogueString = sEnt.dialogue.get(sEnt.dialogueLineNumber);
		d.setDialogueText(currentDialogue.get(dialogueLineNumber));
		isDialogue = true;
		timer = 2500;
	}
	
	public void interactWith(Entity ent, StateBasedGame sbg){
		startBattle(thePlayer, ent, sbg);
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
					thePlayer = new Entity(id, tile, new Image("resources/RollingBall.png"), false, true);
					thePlayer.defaultWeapon = new Weapon(WeaponType.SWORD);
					entities.add(thePlayer);
					System.out.println("Player placed on tile: " + tile);
					tile++;
					id++;
				}
				else if(l == 'B'){
					getTiles().get(tile).isBlocked = true;
					getTiles().get(tile).setTileTexture(new Image("resources/Golmers.png"));
					System.out.println("Tile: " + tile + "is blocked");
					tile++;
				}
				else if(l == 'E'){
					entities.add(new Entity(id, tile, new Image("resources/RollingEnemy.png"), true, true));
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
