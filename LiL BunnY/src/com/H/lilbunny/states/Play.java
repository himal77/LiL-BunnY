package com.H.lilbunny.states;

import com.H.lilbunny.handlers.GameStateManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Play extends GameState{
	
	private BitmapFont font = new BitmapFont();
	
	public Play(GameStateManager gsm) {
		super(gsm);
	}

	
	public void handleInput() {}
	
	public void update(float dt) {}
	
	public void render() {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		font.draw(sb, "Play State", 100, 100);
		sb.end();
	}
	
	public void dispose() {}
	

}
