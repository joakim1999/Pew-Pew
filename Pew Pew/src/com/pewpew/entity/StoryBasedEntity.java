package com.pewpew.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Timer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import com.pewpew.gamestates.Level;
import com.pewpew.startup.Main;

public class StoryBasedEntity extends Entity{
	public String name;
	public ArrayList<String> dialogue;
	public int dialogueLineNumber = 0;
	public int dialogueNumber = 1;
	Level level;
	long timer = 0;
	int delta = 16;
	public StoryBasedEntity(int id, String name, int tileId, Image spriteImage, boolean isAI) throws IOException {
		super(id, tileId, spriteImage, isAI, false);
		this.name = name;
		dialogue = new ArrayList<String>();
	}
	
	public void render(GameContainer gc, Graphics gr){
		this.currentTileId = defaultTileId;
		if(this.image != null){
			gr.drawImage(image, position.x, position.y);
		}
	}
	
	public void update(GameContainer gc, int delta) throws IOException{
	}
	
	public void readDialogueFile(File f) throws IOException{
		System.out.println("reading dialogue file");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while((line = br.readLine()) != null){
			dialogue.add(line);
		}
		br.close();
		System.out.println("succesfully readed");
	}
	
	public ArrayList<String> getDialogue(){
		return dialogue;
	}
}
