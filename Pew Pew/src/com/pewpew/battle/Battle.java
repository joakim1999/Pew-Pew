package com.pewpew.battle;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.entity.Entity;
import com.pewpew.gamestates.Level;
import com.pewpew.startup.Main;

public class Battle extends BasicGameState{
	long timer = 0;
	Image drawedFirst;
	String songPath = "resources/Cool_Things.wav";
	Level level;
	Vector2f playerPosition;
	Vector2f opponentPosition;
	Random rand = new Random();
	String[] lastAction = new String[2];
	int windowWidth;
	int windowHeight;
	boolean isCinematicPlaying = false;
	int turn = 0;
	Sound hit;
	Music song;
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
		hit = new Sound("resources/hit.wav");
		song = new Music(songPath);
		lastAction[0] = "";
		lastAction[1] = "";
		drawedFirst = new Image("resources/RollingBall.png");
		playerPosition = new Vector2f((windowWidth / 2) / 2, windowHeight / 2);
		opponentPosition = new Vector2f(((windowWidth / 2) / 2) * 3, windowHeight / 2);
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
		
		g.drawRoundRect(0, 500, 640, 140, 10);
		g.drawString("1. Meele Attack: 1 MD", 10, 520);
		
		g.drawString(lastAction[0], 0, 25);
		g.drawString(lastAction[1], 0, 40);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
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
			level = (Level)sbg.getState(0);
			Input in = gc.getInput();
			if(turn == 0){
				double d = Math.random();
				if(in.isKeyDown(Input.KEY_1) && isCinematicPlaying == false){
					isCinematicPlaying = true;
					gc.getInput().pause();
					timer = 2500;
					drawedFirst = player.getSpriteImage();
					playerPosition = new Vector2f(opponentPosition);
					boolean didMiss = !(d <= 0.75);
					if(didMiss == false){
						hit.play();
						player.giveDamageTo(opponent, player.defaultWeapon.damage);
						lastAction[0] = "The enemy got " + player.defaultWeapon.damage + " MD";
						turn++;
					}
					else{
						lastAction[0] = "You missed";
						turn++;
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
					lastAction[0] = "";
					lastAction[1] = "";
					gc.getInput().resume();
					song.stop();
					sbg.enterState(0);
				}
			}
			else if(turn == 1){
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
					lastAction[1] = "You got " + damage + " MD";
					if(player.getRemainingLife() <= 0){
						isCinematicPlaying = false;
						opponent.restore();
						player.restore();
						turn = 0;
						lastAction[0] = "";
						lastAction[1] = "";
						gc.getInput().resume();
						song.stop();
						sbg.enterState(Main.gameOverState);
					}
					turn = 0;
				}
				else{
					lastAction[1] = "The enemy missed";
					turn = 0;
				}
			}
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
