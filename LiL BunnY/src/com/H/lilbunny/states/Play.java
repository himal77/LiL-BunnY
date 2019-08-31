package com.H.lilbunny.states;


import com.H.lilbunny.handlers.B2DVars;
import com.H.lilbunny.handlers.GameStateManager;
import com.H.lilbunny.handlers.MyContactListener;
import com.H.lilbunny.handlers.MyInput;
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
	
	private Body playerBody;
	private MyContactListener cl;
	
	public Play(GameStateManager gsm) {
		
		super(gsm);
		
		world = new World(new Vector2(0, -9.81f), true);
		
		cl = new MyContactListener();
		world.setContactListener(cl);
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
		fdef.filter.categoryBits = B2DVars.BIT_GROUND;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fdef).setUserData("ground");
		
		//create player
		bdef.position.set(160 / ppm, 200 / ppm);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);
		
		shape.setAsBox(5 / ppm, 5 / ppm);
		fdef.shape = shape;
		//fdef.restitution = 0.4f;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		playerBody.createFixture(fdef).setUserData("player");
		
		//create foot senser
		shape.setAsBox(2 / ppm, 2 / ppm, new Vector2(0, -5 / ppm), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		fdef.isSensor = true;
		playerBody.createFixture(fdef).setUserData("foot");
		
		//set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / ppm, Game.V_HEIGHT / ppm);
		
		
		
		
		
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
		
		//draw box2d world
		
		b2dr.render(world, b2dCam.combined);
	}
	
	public void dispose() {}
	

}
