package com.H.lilbunny.states;


import com.H.lilbunny.handlers.B2DVars;
import com.H.lilbunny.handlers.GameStateManager;
import com.H.lilbunny.handlers.MyContactListener;
import com.H.lilbunny.handlers.MyInput;
import com.H.lilbunny.main.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Play extends GameState{
	
	private World world;
	private Box2DDebugRenderer b2dr;
	private float ppm = B2DVars.PPM;
	
	private OrthographicCamera b2dCam;
	
	private Body playerBody;
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;
	
	public Play(GameStateManager gsm) {
		
		super(gsm);
		
		world = new World(new Vector2(0, -9.81f), true);
		
		cl = new MyContactListener();
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//create player
		bdef.position.set(160 / ppm, 200 / ppm);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);
		
		shape.setAsBox(5 / ppm, 5 / ppm);
		fdef.shape = shape;
		//fdef.restitution = 0.4f;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_RED;
		playerBody.createFixture(fdef).setUserData("player");
		
		//create foot senser
		shape.setAsBox(2 / ppm, 2 / ppm, new Vector2(0, -5 / ppm), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_RED;
		fdef.isSensor = true;
		playerBody.createFixture(fdef).setUserData("foot");
		
		//set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / ppm, Game.V_HEIGHT / ppm);
		
		///////////////////////////////////////////////////////////////
		
		//load tile map
		tileMap = new TmxMapLoader().load("res/maps/test.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		
		TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
		
		tileSize = layer.getTileWidth();
		
		//go through all the cells in the layer
		
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				
				//get cell
				Cell cell = layer.getCell(col, row);
				
				//check if cell exists
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				//create a body + fixture from the cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * tileSize / ppm,(row + 0.5f) * tileSize / ppm);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[3];
				v[0] = new Vector2(-tileSize / 2 / ppm, -tileSize / 2 / ppm);
				v[1] = new Vector2(-tileSize / 2 / ppm, tileSize / 2 / ppm);
				v[2] = new Vector2(tileSize / 2 / ppm, tileSize / 2 / ppm);
				cs.createChain(v);
				
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = B2DVars.BIT_RED;
				fdef.filter.maskBits = B2DVars.BIT_PLAYER;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);
			}
		}
		
	}

	
	private OrthogonalTiledMapRenderer OrthogonalTiledMapRenderer(TiledMap tileMap2) {
		// TODO Auto-generated method stub
		return null;
	}


	public void handleInput() {
		//player jump
		
		if(MyInput.isPressed(MyInput.BUTTON1)) {
			if(cl.isPlayerOnGround()) {
				playerBody.applyForceToCenter(0, 200, true);
			}
		}
	}
	
	public void update(float dt) {
		
		handleInput();
		
		world.step(dt, 6, 2);
		
	}
	
	public void render() {
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		// draw tile map
		tmr.setView(cam);
		tmr.render();
		
		
		
		//draw box2d world
		
		b2dr.render(world, b2dCam.combined);
	}
	
	public void dispose() {}
	

}
