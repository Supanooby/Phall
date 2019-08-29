package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.Scenes.Hud;
import com.mygdx.Sprites.Mario;
import com.mygdx.game.MyGdxGame;

public class PlayScreen implements Screen{
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	private MyGdxGame game;
	Texture texture;
	private Hud hud;
	
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	//Box2d variables
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private Mario mario;
	
	
	public PlayScreen(MyGdxGame game) {
		this.game = game;
		
		//camera for following mario
		gamecam = new OrthographicCamera();
		
		//Maintains the display ratio
		gamePort = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, gamecam);
		
		//create our game HUD
		hud = new Hud(game.batch);
		
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("untitled.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		
		gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		
		world = new World(new Vector2(0 , -10), true);
		b2dr = new Box2DDebugRenderer();
		
		BodyDef bdef = new BodyDef(); 					/* For creating		*/
		PolygonShape shape = new PolygonShape();		/* Bounding Physics	*/
		FixtureDef fdef = new FixtureDef();				/* For Mario        */
		Body body;										/* In World       	*/
		
		
		//For ground, copy and do for other object layers
		for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight() / 2);
			
			body = world.createBody(bdef);
			
			shape.setAsBox(rect.getWidth()/2 , rect.getHeight()/2);// reference starts in the center, and expands both sides outwrds;
			fdef.shape = shape;
			body.createFixture(fdef);
		}
		
		
		mario = new Mario(world);
		
	}
	
	public void update(float deltaTime) {
		handleInput(deltaTime);
		
		world.step(1/60f, 10, 1);
		
		gamecam.update();
		renderer.setView(gamecam);
	}
	
	public void handleInput(float deltaTime) {
		if(Gdx.input.isTouched()) {
			gamecam.position.x += 100 * deltaTime;
		}
		
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.render();
		
		b2dr.render(world, gamecam.combined);
		
		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
