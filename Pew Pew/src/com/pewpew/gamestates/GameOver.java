package com.pewpew.gamestates;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.components.Button;
import com.pewpew.other.Action;
import com.pewpew.startup.Main;

public class GameOver extends BasicGameState{
	Font myFont = new Font("Verdana", Font.BOLD, 40);
	TrueTypeFont font;
	Button tryAgain;
	Level level;

	@Override
	public void init(final GameContainer arg0, final StateBasedGame arg1)
			throws SlickException {
		 font = new TrueTypeFont(myFont, false);
		 tryAgain = new Button(0, "Try Again", new Vector2f(Main.windowWidth / 2 - (200 / 2)
				 , (Main.windowHeight / 2) + 200 - (50 / 2)), 210, 50, new Action(){
					@Override
					public void doAction() {
						arg0.getInput().resume();
						level.needRestarting = true;
						arg1.enterState(Main.levelState);
					}
			 
		 });
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		level = (Level)arg1.getState(Main.levelState);
		font.drawString(Main.windowHeight / 2 - (font.getWidth("Game Over") / 2), Main.windowHeight / 2 - (font.getHeight() / 2), "Game Over");
		tryAgain.render(arg2);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		Input in = arg0.getInput();
		tryAgain.update(arg0, arg1, arg2);
	}

	@Override
	public int getID() {
		return Main.gameOverState;
	}
}
