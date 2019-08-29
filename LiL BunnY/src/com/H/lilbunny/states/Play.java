package com.H.lilbunny.states;

import com.H.lilbunny.handlers.B2DVars;
import com.H.lilbunny.handlers.GameStateManager;
import com.H.lilbunny.main.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Play extends GameState{
	
	private World world;
	private Box2DDebugRenderer b2dr;
	private float ppm = B2DVars.PPM;
	
	private OrthographicCamera b2dCam;
	
	public Play(GameStateManager gsm) {
		
		super(gsm);
		
		world = new World(new Vector2(0, -9.81f), true);
		b2dr = new Box2DDebugRenderer();
		
		//create platform
		BodyDef bdef = new BodyDef();
		bdef.position.set(160 / ppm, 120 / ppm);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / ppm, 5 / ppm);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		
		//create falling box
		bdef.position.set(160 / ppm, 200 / ppm);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(5 / ppm, 5 / ppm);
		fdef.shape = shape;
		fdef.restitution = 0.7f;
		body.createFixture(fdef);
		
		//set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / ppm, Game.V_HEIGHT / ppm);
		
		
		
	}

	
	public void handleInput() {}
	
	public void update(float dt) {
		
		world.step(dt, 6, 2);
		
	}
	
	public void render() {
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//draw box2d world
		
		b2dr.render(world, b2dCam.combined);
	}
	
	public void dispose() {}
	

}
