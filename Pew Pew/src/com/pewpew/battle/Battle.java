package com.pewpew.battle;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.components.Button;
import com.pewpew.components.Dialogue;
import com.pewpew.components.HealthBar;
import com.pewpew.entity.Entity;
import com.pewpew.gamestates.Level;
import com.pewpew.other.Action;
import com.pewpew.startup.Main;

public class Battle extends BasicGameState{
	long timer = 0;
	Image drawedFirst;
	String songPath = "resources/Cool_Things.wav";
	ArrayList<Button> buttons;
	Level level;
	int componentIdCounter = 0;
	Vector2f playerPosition;
	Vector2f opponentPosition;
	Random rand = new Random();
	String lastAction = "";
	int windowWidth;
	int windowHeight;
	int GUIAreaHeight = (Main.windowHeight / 7);
	int GUIAreaWidth = Main.windowWidth - 1;
	Vector2f GUIAreaPosition;
	boolean isCinematicPlaying = false;
	int turn = 0;
	Sound hit;
	Music song;
	Dialogue dialogue;
	HealthBar hbarPlayer;
	HealthBar hbarOpponent;
	public boolean isStillInBattle = true;
	private Entity player;
	private Entity opponent;
	
	public Battle(int windowWidth, int windowHeight){
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException{
		GUIAreaPosition = new Vector2f(0, windowHeight - GUIAreaHeight);
		hit = new Sound("resources/hit.wav");
		song = new Music(songPath);
		buttons = new ArrayList<Button>();
		lastAction = "";
		drawedFirst = new Image("resources/RollingBall.png");
		playerPosition = new Vector2f((windowWidth / 2) / 2, windowHeight / 2);
		opponentPosition = new Vector2f(((windowWidth / 2) / 2) * 3, windowHeight / 2);
		hbarPlayer = new HealthBar(componentIdCounter, new Vector2f(0, 0), 20);
		componentIdCounter++;
		hbarOpponent = new HealthBar(componentIdCounter, new Vector2f(Main.windowWidth - 100, GUIAreaPosition.y - 20), 20);
		componentIdCounter++;
		dialogue = new Dialogue(componentIdCounter, lastAction, new Vector2f(GUIAreaPosition.x, GUIAreaPosition.y), GUIAreaWidth, GUIAreaHeight / 2 - 25);
		componentIdCounter++;
		setUpButtons(arg0, arg1);
		setButtonFonts(new Font("Verdana", Font.BOLD, 15));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		if(drawedFirst.equals(player.getSpriteImage())){
			g.drawImage(opponent.getSpriteImage(), opponentPosition.x, opponentPosition.y);
			g.drawImage(player.getSpriteImage(), playerPosition.x, playerPosition.y);
		}
		else if(drawedFirst.equals(opponent.getSpriteImage())){
			g.drawImage(player.getSpriteImage(), playerPosition.x, playerPosition.y);
			g.drawImage(opponent.getSpriteImage(), opponentPosition.x, opponentPosition.y);
		}
		
		dialogue.render(gc, sb, g);
		hbarPlayer.render(gc, sb, g);
		hbarOpponent.render(gc, sb, g);
		
		renderGUIArea(g);
		renderButtons(g);
	}
	
	public void setButtonFonts(Font f){
		for(Button b : buttons){
			b.setFont(f);
		}
	}
	
	public void setUpButtons(final GameContainer gc, final StateBasedGame sbg){
		int buttonWidth = 128;
		int buttonHeight = (GUIAreaHeight / 2) + 25;
		int buttonX = 0;
		buttons.add(new Button(componentIdCounter, "Meele Attack", new Vector2f(buttonX, dialogue.position.y + dialogue.height), buttonWidth, buttonHeight, new Action(){
			@Override
			public void doAction() {
				System.out.println("Button is clicked");
				doPlayerMove(gc, sbg);
				System.out.println("Player has done its move");
			}
		}));
		componentIdCounter++;
		buttonX += buttonWidth;
	}
	
	public void renderButtons(Graphics gr){
		for(Button b : buttons){
			b.render(gr);
		}
	}
	
	public void renderGUIArea(Graphics gr){
		gr.drawRect(GUIAreaPosition.x, GUIAreaPosition.y, GUIAreaWidth, GUIAreaHeight - 1);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		hbarPlayer.setHealthRemaining(player.getRemainingLife());
		hbarOpponent.setHealthRemaining(opponent.getRemainingLife());
		dialogue.setDialogueText(lastAction);
		player.position = playerPosition;
		opponent.position = opponentPosition;
		if(sbg.getCurrentState().getID() == this.getID() && song.playing() == false){
			song.loop();
		}
		if(isCinematicPlaying == true){
			timer -= delta;
			if(timer <= 0){
				isCinematicPlaying = false;
				playerPosition = new Vector2f((windowWidth / 2) / 2, windowHeight / 2);
				opponentPosition = new Vector2f(((windowWidth / 2) / 2) * 3, windowHeight / 2);
				gc.getInput().resume();
				timer = 0;
			}
		}
		else{
			playerPosition = new Vector2f((windowWidth / 2) / 2, windowHeight / 2);
			opponentPosition = new Vector2f(((windowWidth / 2) / 2) * 3, windowHeight / 2);
			for(Button b : buttons){
				b.update(gc, sbg, delta);
				System.out.println(b.id + " is updated");
			}
			doRound(gc, sbg, delta);
		}
	}
	
	public void doRound(GameContainer gc, StateBasedGame sbg, int delta){
		level = (Level)sbg.getState(0);
		if(turn == 1 && isCinematicPlaying == false){
			doOpponentMove(gc, sbg);
		}
	}
	
	public void doPlayerMove(GameContainer gc, StateBasedGame sbg){
		double d = Math.random();
		if(isCinematicPlaying == false){
			isCinematicPlaying = true;
			gc.getInput().pause();
			timer = 2500;
			drawedFirst = player.getSpriteImage();
			playerPosition = new Vector2f(opponentPosition);
			boolean didMiss = !(d <= 0.75);
			if(didMiss == false){
				hit.play();
				player.giveDamageTo(opponent, player.defaultWeapon.damage);
				lastAction = "You hit the enemy. The enemy losed " + player.defaultWeapon.damage + " hitpoints";
				turn++;
			}
			else{
				lastAction = "You missed. The enemy didn't lose any hitpoints";
				turn = 1;
			}
		}
		if(opponent.getRemainingLife() <= 0){
			isCinematicPlaying = false;
			level.entities.remove(opponent);
			level.destroy(opponent);
			if(player.getRemainingLife() <= 3){
				player.restore(2);
			}
			else if(player.getRemainingLife() == 4){
				player.restore(1);
			}
			turn = 0;
			lastAction = "";
			lastAction = "";
			gc.getInput().resume();
			song.stop();
			sbg.enterState(0);
		}
	}
	
	public void doOpponentMove(GameContainer gc, StateBasedGame sbg){
		isCinematicPlaying = true;
		gc.getInput().pause();
		timer = 2500;
		drawedFirst = opponent.getSpriteImage();
		opponentPosition = new Vector2f(playerPosition);
		double d = Math.random();
		boolean didMiss = !(d <= 0.60);
		if(didMiss == false){
			hit.play();
			float damage = rand.nextInt(3);
			opponent.giveDamageTo(player, damage);
			lastAction = "The enemy hit you. You losed " + damage + " hitpoints";
			if(player.getRemainingLife() <= 0){
				isCinematicPlaying = false;
				opponent.restore();
				player.restore();
				turn = 0;
				lastAction = "";
				lastAction = "";
				gc.getInput().resume();
				song.stop();
				sbg.enterState(Main.gameOverState);
			}
			turn = 0;
		}
		else{
			lastAction = "The enemy missed. You didn't lose any hitpoints";
			turn = 0;
		}
	}
	
	public void setMatch(Entity player, Entity opponent){
		this.player = player;
		this.opponent = opponent;
		drawedFirst = player.getSpriteImage();
	}

	@Override
	public int getID() {
		return Main.battleState;
	}
	
}
