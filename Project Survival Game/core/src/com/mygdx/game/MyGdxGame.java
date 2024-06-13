package com.mygdx.game;
import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {

	@Override
	public void create () {
		setScreen(new MenuScreen(this));
	}

	public void resize(int w, int h){
		super.resize(w,h);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}