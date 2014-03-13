package com.pewpew.gamestates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
import com.pewpew.components.InventoryBar;
import com.pewpew.entity.EnemyType;
import com.pewpew.entity.Entity;
import com.pewpew.entity.StoryBasedEntity;
import com.pewpew.inventory.Inventory;
import com.pewpew.inventory.Item;
import com.pewpew.inventory.ItemPool;
import com.pewpew.inventory.Loot;
import com.pewpew.startup.Main;
import com.pewpew.tile.*;

public class Level extends BasicGameState{
	Music levelSong;
	ArrayList<String> currentDialogue;
	Dialogue d = new Dialogue(0, "", new Vector2f(0, Main.windowHeight - storyTileHeight), storyTileWidth - 1, storyTileHeight - 1);
	Inventory playerInventory;
	InventoryBar invBar;
	public ArrayList<ItemPool> itemPools;
	public Loot loot;
	public int playerLevel = 0;
	int xpToLevelUp = 5;
	int xpToLevelUpIncreasement = 125;
	float xp;
	int dialogueLineNumber = 0;
	int timer;
	static int tileWidth = 32;
	static int tileHeight = 32;
	int mapNumber;
	int inventoryMaxSpace = 16;
	boolean needRestarting = true;
	boolean startBattle = true;
	public Entity thePlayer;
	static ArrayList<Tile> tiles;
	public ArrayList<Entity> entities;
	public ArrayList<Item> items;
	public File[] maps = Main.filesInFolder;
	public int finishTileId = 0;
	static int windowWidth = Main.windowWidth;
	static int windowHeight = Main.windowHeight;
	public static int storyTileWidth = Main.windowWidth;
	public static int storyTileHeight = 20;
	public static boolean isDialogue = false;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		levelSong = new Music("resources/LevelMusic1.wav");
	}
	
	//This function is rewriting the inner tilesystem: Positions, rewriting IDs. Can also be used as a resetting of tile system
	public void initializeTiles(){
		tiles.clear();
		int x = 0;
		int y = 0;
		int id = 0;
		for(int i = 0;i < getTilesY();i++){
			for(int n = 0;n < getTilesX();n++){
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
		//Rendering ALL the entities
		for(Entity ent : entities){
			ent.render(gc, g);
		}
		//Rendering all the tiles
		for(Tile t : tiles){
			t.render(g);
		}
		
		//Rendering all items
		for(Item i : items){
			i.render(gc, sb, g);
		}
		
		playerInventory.render(gc, sb, g);
		invBar.render(gc, sb, g);
		if(loot != null){
			loot.render(gc, sb, g);
		}
		
		//Setting and rendering dialogue if true
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
		
		//Updating the
		if(isDialogue == true){
			if(gc.getInput().isMousePressed(0) /*&& dialogueLineNumber < currentDialogue.size()*/){
				dialogueLineNumber++;
			}
			if(dialogueLineNumber >= currentDialogue.size()){
				gc.getInput().clearKeyPressedRecord();
				dialogueLineNumber = 0;
				isDialogue = false;
				return;
			}
		}
		
		else if(loot != null){
			loot.update(gc, sbg, arg2);
		}
		
		else{
			try {
				playerInventory.update(gc, sbg, arg2);
				invBar.update(gc, sbg, arg2);
				updateItems(gc, sbg, arg2);
				entityUpdate(gc, sbg);
				doMove(gc, sbg);
				checkFinish(gc, sbg);
				checkLevelUp();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Updates all the entities
	public void entityUpdate(GameContainer gc, StateBasedGame sbg) throws SlickException, IOException{
		for(Entity ent : entities){
			ent.update();
		}
	}
	
	//Checking if the players tile matches the finish tile of the level
	public void checkFinish(GameContainer gc, StateBasedGame sbg) throws IOException, SlickException{
		if(thePlayer.getTileId() == finishTileId){
			if(mapNumber <= maps.length - 1){
				initializeTiles();
				entities.clear();
				loadMap(maps[mapNumber]);
				mapNumber++;
			}
			else{
				gc.getInput().resume();
				levelSong.stop();
				sbg.enterState(Main.winState);
			}
		}
	}
	
	public void checkLevelUp(){
		if(xp >= xpToLevelUp){
			xp -= xpToLevelUp;
			playerLevel++;
			xpToLevelUp += xpToLevelUp / 100 * xpToLevelUpIncreasement;
		}
	}
	
	public void startBattle(Entity player, Entity opponent, StateBasedGame sbg){
		Battle b = (Battle)sbg.getState(Main.battleState);
		b.setMatch(thePlayer, opponent);
		b.setInventory(playerInventory);
		sbg.enterState(1);
		thePlayer.setTileId(thePlayer.getTileId());
		startBattle = false;
		setUpInventoryBar();
	}
	
	public void updateItems(GameContainer gc, StateBasedGame sbg, int delta){
		for(Item i : items){
			i.update(gc, sbg, delta);
		}
	}
	
	public void doMove(GameContainer gc, StateBasedGame sbg){
		Input in = gc.getInput();
		if(in.isKeyPressed(Input.KEY_UP)){
			if(thePlayer.position.y != 0 && getTiles().get(thePlayer.getTileId() - getTilesX()).isBlocked == false && thePlayer.position.y != 0 && getTiles().get(thePlayer.getTileId() - getTilesX()).isOccupied == false){
				thePlayer.getTile().isOccupied = false;
				checkMovingWithEntities(-getTilesX(), gc, sbg);
				thePlayer.setTileId(thePlayer.getTileId() - getTilesX());
				moveAIs(sbg);
			}
			else{
				checkMovingWithEntities(-getTilesX(), gc, sbg);
			}
		}
		if(in.isKeyPressed(Input.KEY_DOWN)){
			if(thePlayer.position.y != windowHeight - tileHeight && getTiles().get(thePlayer.getTileId() + getTilesX()).isBlocked == false && thePlayer.position.y != windowHeight - tileHeight && getTiles().get(thePlayer.getTileId() + getTilesX()).isOccupied == false){
				thePlayer.getTile().isOccupied = false;
				checkMovingWithEntities(getTilesX(), gc, sbg);
				thePlayer.setTileId(thePlayer.getTileId() + getTilesX());
				moveAIs(sbg);
			}
			else{
				checkMovingWithEntities(getTilesX(), gc, sbg);
			}
		}
		if(in.isKeyPressed(Input.KEY_LEFT)){
			if(thePlayer.position.x != 0 && getTiles().get(thePlayer.getTileId() -1).isBlocked == false && thePlayer.position.x != 0 && getTiles().get(thePlayer.getTileId() -1).isOccupied == false){
				thePlayer.getTile().isOccupied = false;
				checkMovingWithEntities(-1, gc, sbg);
				thePlayer.setTileId(thePlayer.getTileId() - 1);
				moveAIs(sbg);
			}
			else{
				checkMovingWithEntities(-1, gc, sbg);
			}
		}
		if(in.isKeyPressed(Input.KEY_RIGHT)){
			if(thePlayer.position.x != windowWidth - tileWidth && getTiles().get(thePlayer.getTileId() + 1).isBlocked == false && thePlayer.position.x != windowWidth - tileWidth && getTiles().get(thePlayer.getTileId() + 1).isOccupied == false){
				thePlayer.getTile().isOccupied = false;
				checkMovingWithEntities(1, gc, sbg);
				thePlayer.setTileId(thePlayer.getTileId() + 1);
				moveAIs(sbg);
			}
			else{
				checkMovingWithEntities(1, gc, sbg);
			}
		}
		if(in.isKeyPressed(Input.KEY_UP)){
			if(thePlayer.position.x != windowWidth - tileWidth){
				thePlayer.setTileId(thePlayer.getTileId() + 1);
				moveAIs(sbg);
			}
		}
		thePlayer.getTile().isOccupied = true;
		checkMovingWithEntities(0, gc, sbg);
	}
	
	public void checkMovingWithEntities(Integer movement, GameContainer gc, StateBasedGame sbg){
		for(Entity ent : entities){
			if(ent.isAI == true){
				if(thePlayer.getTileId() + movement == ent.getTileId()){
					if(ent instanceof StoryBasedEntity){
						interactWith(gc, (StoryBasedEntity)ent);
					}
					else{
						interactWith(ent, sbg);
						startBattle(thePlayer, ent, sbg);
					}
				}
			}
		}
		
		for(Item i : items){
			if(i.getTileId() != null){
				if(thePlayer.getTileId() + movement == i.getTileId()){
					i.setTileId(null);
					if(i instanceof Weapon){
						thePlayer.setTileId(thePlayer.getTileId());
						interactWith((Weapon)i);
					}
					else{
						interactWith(i);
					}
				}
			}
		}
	}
	
	private void interactWith(Item i) {
		playerInventory.addItem(i);
		i.setInventory(playerInventory);
		setUpInventoryBar();
	}
	
	private void interactWith(Weapon w) {
		playerInventory.addItem(w);
		if(thePlayer.defaultWeapon == null){
			thePlayer.defaultWeapon = w;
		}
		w.setInventory(playerInventory);
	}

	public void moveAIs(StateBasedGame sbg){
		for(Entity ent : entities){
			if(ent.isAI == true && ent.needMoving == true){
				if(ent.getTile().y > thePlayer.getTile().y){
					if(ent.getTile().y != 0 && getTiles().get(ent.getTileId() - 20).isBlocked == false && ent.getTile().y != 0 && getTiles().get(ent.getTileId() - 20).isOccupied == false){
						ent.getTile().isOccupied = false;
						ent.setTileId(ent.getTileId() - getTilesX());
					}
				}
				if(ent.getTile().y < thePlayer.getTile().y){
					if(ent.getTile().y != windowHeight - tileHeight && getTiles().get(ent.getTileId() + 20).isBlocked == false && ent.getTile().y != windowHeight - tileHeight && getTiles().get(ent.getTileId() + 20).isOccupied == false){
						ent.getTile().isOccupied = false;
						ent.setTileId(ent.getTileId() + getTilesX());
					}
				}
				if(ent.getTile().x > thePlayer.getTile().x){
					if(ent.getTile().x != 0 && getTiles().get(ent.getTileId() - 1).isBlocked == false && ent.getTile().x != 0 && getTiles().get(ent.getTileId() - 1).isOccupied == false){
						ent.getTile().isOccupied = false;
						ent.setTileId(ent.getTileId() - 1);
					}
				}
				if(ent.getTile().x < thePlayer.getTile().x){
					if(ent.getTile().x != windowWidth - tileWidth && getTiles().get(ent.getTileId() + 1).isBlocked == false && ent.getTile().x != windowWidth - tileWidth && getTiles().get(ent.getTileId() + 1).isOccupied == false){
						ent.getTile().isOccupied = false;
						ent.setTileId(ent.getTileId() + 1);
					}
				}
				ent.getTile().isOccupied = true;
			}
		}
	}
	
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException{
		super.enter(gc, sbg);
		if(needRestarting == true){
			currentDialogue = new ArrayList<String>();
			tiles = new ArrayList<Tile>();
			items = new ArrayList<Item>();
			entities = new ArrayList<Entity>();
			itemPools = new ArrayList<ItemPool>();
			initializeTiles();
			mapNumber = 0;
			
			try {
				loadMap(maps[mapNumber]);
				mapNumber++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				getItemPools(new File("Item Pools"));
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
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
	
	public void getItemPools(File directory) throws IOException, NumberFormatException, SlickException{
		for(File f : directory.listFiles()){
			System.out.println("Making a new item pool out of file: " + f.getName());
			ItemPool ip = new ItemPool(f);
			itemPools.add(ip);
		}
	}
	
	public void loadMap(File f) throws IOException, SlickException{
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		int tile = 0;
		int id = 0;
		while((line = br.readLine()) != null){
			char[] letters = line.toCharArray();
			final String[] args = line.split(" ");
			if(line.startsWith("[HealItem]")){
				Item i = new Item(items.size(), Boolean.parseBoolean(args[4])){
					@Override
					public void interactWith(Entity target) {
						target.restore(Integer.parseInt(args[3]));
						if(target.getRemainingLife() > 5){
							target.restore();
						}
					}
				};
				i.setTexture(new Image(args[1]));
				i.setTileId(Integer.parseInt(args[2]));
				i.enableRendering();
				items.add(i);
			}
			else if(line.startsWith("[SBC]")){
				StoryBasedEntity sbe = new StoryBasedEntity(entities.size(), args[1], Integer.parseInt(args[3]), new Image(args[2]), true);
				sbe.readDialogueFile(new File("resources/dialogues/" + args[4]));
				currentDialogue = sbe.getDialogue();
				entities.add(sbe);
			}
			else if(line.startsWith("[Weapon]")){
				System.out.println("Making weapon");
				Weapon w = new Weapon(items.size(), WeaponType.valueOf(args[3]));
				w.setTexture(new Image(args[1]));
				w.setTileId(Integer.parseInt(args[2]));
				w.enableRendering();
				items.add(w);
			}
			else{
				for(char l : letters){
					System.out.println("Current char is: " + "'" + l + "'");
					if(l == 'P'){
						thePlayer = new Entity(id, tile, new Image("resources/RollingBall.png"), false, true);
						entities.add(thePlayer);
						if(playerInventory == null){
							playerInventory = new Inventory(thePlayer, inventoryMaxSpace);
						}
						else{
							for(Item i : playerInventory.itemsInInventory){
								if(i != null && i instanceof Weapon){
									thePlayer.defaultWeapon = (Weapon)i;
								}
							}
						}
						invBar = new InventoryBar(playerInventory, new Vector2f(0, Main.windowHeight - playerInventory.slotHeight - 1));
						invBar.setUpSlots();
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
						Item i = new Item(items.size(), true){
							@Override
							public void interactWith(Entity target) {
								target.restore(3);
								if(target.getRemainingLife() > 5){
									target.restore();
								}
							}
						};
						Entity enemy;
						if(playerLevel <= 4){
							enemy = new Entity(id, tile, new Image("resources/RollingEnemy.png"), true, true, EnemyType.EASY);
							enemy.setExperienceDrop(3);
						}
						else if(playerLevel > 4 && playerLevel < 8){
							enemy = new Entity(id, tile, new Image("resources/RollingEnemy.png"), true, true, EnemyType.MEDIUM);
							enemy.setExperienceDrop(8);
						}
						else{
							enemy = new Entity(id, tile, new Image("resources/RollingEnemy.png"), true, true, EnemyType.HARD);
							enemy.setExperienceDrop(16);
						}
						entities.add(enemy);
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
		}
		System.out.println("Map loaded");
		br.close();
	}
	
	public void destroy(Entity ent){
		ent.getTile().isOccupied = false;
		
		if(ent.drop != null){
			interactWith(ent.drop);
		}
		
		if(ent.experienceDrop != null){
			xp += ent.experienceDrop;
		}
		
		entities.remove(ent);
		ent = null;
	}
	
	public int getTilesX(){
		return windowWidth/tileWidth;
	}
	
	public void setUpInventoryBar(){
		System.out.println("Setting up inventory bar");
		invBar.setUpSlots();
		for(Item i : playerInventory.itemsInInventory){
			if(i != null){
				i.enableRendering();
			}
		}
	}
	
	public void resetLoot(){
		loot = null;
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
	
	public static int getTileWidth(){
		return tileWidth;
	}
	
	public static int getTileHeight(){
		return tileHeight;
	}
}