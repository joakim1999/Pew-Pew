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
import com.pewpew.startup.Main;

public class Menu extends BasicGameState{
	Button startButton;
	Button aboutButton;
	Button exitButton;
	int buttonWidth;
	int buttonHeight;
	JFrame frame;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		frame = new JFrame();
		buttonWidth = 250;
		buttonHeight = 50;
		startButton = new Button(0, "Start Game", new Vector2f(320 - (buttonWidth/2), 200), buttonWidth, buttonHeight);
		buttonWidth = 135;
		aboutButton = new Button(0, "About", new Vector2f(320 - (buttonWidth/2), 300), buttonWidth, buttonHeight);
		buttonWidth = 90;
		exitButton = new Button(0, "Exit", new Vector2f(320 - (buttonWidth/2), 400), buttonWidth, buttonHeight);
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		startButton.render(arg2);
		aboutButton.render(arg2);
		exitButton.render(arg2);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		Input in = arg0.getInput();
		int mouseX = in.getMouseX();
		int mouseY = in.getMouseY();
		if(in.isMouseButtonDown(0)){
			if(mouseX >= startButton.position.x && mouseX <= startButton.position.x + startButton.width && mouseY > startButton.position.y && mouseY < startButton.position.y + startButton.height){
				arg0.getInput().resume();
				arg1.enterState(Main.levelState);
			}
		}
		if(in.isMouseButtonDown(0)){
			if(mouseX >= aboutButton.position.x && mouseX <= aboutButton.position.x + startButton.width && mouseY > aboutButton.position.y && mouseY < aboutButton.position.y + aboutButton.height){
				JOptionPane.showMessageDialog(frame, "PewPew is a science fiction role-playing game");
			}
		}
		if(in.isMouseButtonDown(0)){
			if(mouseX >= exitButton.position.x && mouseX <= exitButton.position.x + exitButton.width && mouseY > exitButton.position.y && mouseY < exitButton.position.y + exitButton.height){
				System.exit(0);
			}
		}
	}

	@Override
	public int getID() {
		return Main.menuState;
	}
}
