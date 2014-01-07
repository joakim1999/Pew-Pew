package com.pewpew.battle;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.levels.*;
import com.pewpew.startup.Main;

public class Battle extends BasicGameState{
	Image playerImage;
	Image opponentImage;
	Level1 level;
	Random rand = new Random();
	String[] lastAction = new String[2];
	int windowWidth;
	int windowHeight;
	int turn = 0;
	public boolean isStillInBattle = true;
	
	public Battle(int windowWidth, int windowHeight){
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		try {
			lastAction[0] = "";
			lastAction[1] = "";
			this.playerImage = new Image("com/pewpew/image/RollingBall.png");
			this.opponentImage = new Image("com/pewpew/image/RollingEnemy.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g)
			throws SlickException {
		g.drawImage(playerImage, (windowWidth / 2) / 2, windowHeight / 2);
		g.drawImage(opponentImage, ((windowWidth / 2) / 2) * 3, windowHeight / 2);
		
		g.drawRoundRect(0, 500, 640, 140, 10);
		g.drawString("1. Meele Attack: 1 MD", 10, 520);
		
		g.drawString(lastAction[0], 0, 25);
		g.drawString(lastAction[1], 0, 40);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		level = (Level1)sbg.getState(0);
		Input in = gc.getInput();
		if(turn == 0){
			double d = Math.random();
			boolean didMiss = !(d <= 1 - (level.thePlayer.defaultWeapon.missChance / 100));
			if(in.isKeyPressed(Input.KEY_1)){
				if(didMiss == false){
					level.thePlayer.giveDamageTo(level.enemy, level.thePlayer.defaultWeapon.damage);
					lastAction[0] = "The enemy got " + level.thePlayer.defaultWeapon.damage + " MD";
					turn++;
				}
				else{
					lastAction[0] = "You missed";
					turn++;
				}
			}
			if(level.enemy.getRemainingLife() <= 0){
				level.enemy.restore();
				level.thePlayer.restore();
				turn = 0;
				lastAction[0] = "";
				lastAction[1] = "";
				sbg.enterState(0);
			}
		}
		if(turn == 1){
			double d = Math.random();
			boolean didMiss = !(d <= 0.75);
			if(didMiss == false){
				float damage = rand.nextInt(5);
				level.enemy.giveDamageTo(level.thePlayer, damage);
				lastAction[1] = "You got " + damage + " MD";
				if(level.thePlayer.getRemainingLife() <= 0){
					System.exit(0);
				}
				turn = 0;
			}
			else{
				lastAction[1] = "The enemy missed";
				turn = 0;
			}
		}
	}

	@Override
	public int getID() {
		return Main.battleState;
	}
	
}
