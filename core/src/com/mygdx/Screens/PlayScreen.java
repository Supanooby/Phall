package com.mygdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.Scenes.Hud;
import com.mygdx.Sprites.Coin;
import com.mygdx.Sprites.Mario;
import com.mygdx.game.MyGdxGame;

import Tools.B2WorldCreator;

public class PlayScreen implements Screen, ContactListener{
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	private MyGdxGame game;
	private Hud hud;
	
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	//Box2d variables
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private Mario mario;
	ContactListener listener;
	
	
	public PlayScreen(MyGdxGame game) {
		this.game = game;
		
		
		
		//camera for following mario
		gamecam = new OrthographicCamera();
		
		//Maintains the display ratio
		gamePort = new FitViewport(MyGdxGame.V_WIDTH / 100, MyGdxGame.V_HEIGHT / 100, gamecam);
		
		//create our game HUD
		hud = new Hud(game.batch);
		
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("untitled.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/100f);
		
		gamecam.position.set(gamePort.getWorldWidth() / 2f, gamePort.getWorldHeight() / 2f, 0);
		
		world = new World(new Vector2(0 , 0), true);
		b2dr = new Box2DDebugRenderer();
		
		new B2WorldCreator(world, map);
		
		//creates the bricks fham
		for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			new Coin(world, map, rect);
		}
		
		world.setContactListener(this);
		
		
		

		BodyDef bdef = new BodyDef();
		Body body;
		FixtureDef fdef = new FixtureDef();
		
		for(MapObject object: map.getLayers().get(4).getObjects()) {
			System.out.println(object.getClass());
			Shape shape;
			if (object instanceof EllipseMapObject) {
				shape = getCircle((EllipseMapObject)object);
				System.out.println("lol");
				
			} else shape = null;
			
			bdef.type = BodyDef.BodyType.StaticBody;
			
			
			
			body = world.createBody(bdef);
			
			fdef.isSensor = true;
			fdef.shape = shape;
			
			
			
			body.createFixture(fdef);
			body.setBullet(true);
			
			
		
		}
		
		
		
		
		 
		
		mario = new Mario(world);
		
	}
	
	private Shape getCircle(EllipseMapObject object) {
		// TODO Auto-generated method stub
		
		Ellipse circle = object.getEllipse();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(circle.width /2 / 100);
		circleShape.setPosition(new Vector2((circle.x + circle.width/2) / 100, (circle.y + circle.height/2) / 100));
		return circleShape;
		
	}

	public void update(float deltaTime) {
		handleInput(deltaTime);
		
		gamecam.position.x = mario.b2body.getPosition().x;
		gamecam.position.y = mario.b2body.getPosition().y;
		
		world.step(1/60f, 10, 1);
		
		gamecam.update();
		renderer.setView(gamecam);
	}
	
	public void handleInput(float deltaTime) {
		if(Gdx.input.isKeyJustPressed(Keys.UP)) 
			mario.b2body.applyLinearImpulse(new Vector2(0, 4f),
					mario.b2body.getWorldCenter(), true);
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && mario.b2body.getLinearVelocity().x <= 2)
			mario.b2body.applyLinearImpulse(new Vector2(0.1f, 0), 
					mario.b2body.getWorldCenter(), true);
		if(Gdx.input.isKeyPressed(Keys.LEFT) && mario.b2body.getLinearVelocity().x >= -2)
			mario.b2body.applyLinearImpulse(new Vector2(-0.1f, 0),
					mario.b2body.getWorldCenter(), true);
		if(Gdx.input.isKeyJustPressed(Keys.DOWN)) 
			mario.b2body.applyLinearImpulse(new Vector2(0, -4f),
					mario.b2body.getWorldCenter(), true);
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) mario.b2body.setLinearVelocity(0, 0);
		
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
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
		
	}

	@Override
	public void beginContact(Contact contact) {
		System.out.println(contact.getChildIndexA());
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
