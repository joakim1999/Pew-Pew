package com.pewpew.gamestates;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Color;

import com.pewpew.components.Button;
import com.pewpew.other.Action;
import com.pewpew.startup.Main;

public class Menu extends BasicGameState{
	Button startButton;
	Button aboutButton;
	Button exitButton;
	int buttonWidth;
	int buttonHeight;
	JFrame frame;

	@Override
	public void init(final GameContainer arg0, final StateBasedGame arg1)
			throws SlickException {
		frame = new JFrame();
		buttonWidth = 250;
		buttonHeight = 50;
		startButton = new Button(0, "Start Game", new Vector2f(320 - (buttonWidth/2), 200), buttonWidth, buttonHeight, new Action(){
			@Override
			public void doAction() {
				arg0.getInput().resume();
				arg1.enterState(Main.levelState);
			}
		});
		buttonWidth = 135;
		aboutButton = new Button(0, "About", new Vector2f(320 - (buttonWidth/2), 300), buttonWidth, buttonHeight, new Action(){
			@Override
			public void doAction() {
				JOptionPane.showMessageDialog(frame, "PewPew is a science fiction role-playing game");
			}
		});
		buttonWidth = 90;
		exitButton = new Button(0, "Exit", new Vector2f(320 - (buttonWidth/2), 400), buttonWidth, buttonHeight, new Action(){
			@Override
			public void doAction() {
				System.exit(0);
			}
		});
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		startButton.render(arg2);
		aboutButton.render(arg2);
		exitButton.render(arg2);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		startButton.update(gc, sbg, delta);
		aboutButton.update(gc, sbg, delta);
		exitButton.update(gc, sbg, delta);
	}

	@Override
	public int getID() {
		return Main.menuState;
	}
}
