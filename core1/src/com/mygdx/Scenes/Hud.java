package com.mygdx.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

public class Hud {
	
	public Stage stage;
	private Viewport viewport; //So the HUD stays in the same spot on the screen
	
	private Integer worldTimer;
	private float timeCount;
	private Integer score;
	
	
	//The On Screen Elements
	Label countdownLabel;
	Label scoreLabel;
	Label timeLabel;
	Label levelLabel;
	Label worldLabel;
	Label marioLabel;
	
	public Hud(SpriteBatch sb) {
		worldTimer = 300;
		timeCount = 0;
		score = 0;
		
		viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);
		stage = new Stage(viewport, sb); // Empty Box
		
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		
		countdownLabel = new Label(String.format("%03d", worldTimer),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		scoreLabel = new Label(String.format("%06d", score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		
		//Holds the on screen ui elements in place
		table.add(marioLabel).expandX().padTop(10);
		table.add(worldLabel).expandX().padTop(10);
		table.add(timeLabel).expandX().padTop(10);
		table.row(); //creates a new row for elements to be put on
		table.add(scoreLabel).expandX();
		table.add(levelLabel).expandX();
		table.add(countdownLabel).expandX();
		stage.addActor(table);
	}

}
