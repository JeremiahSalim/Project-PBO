package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, monsImage, mcImage, mcAtkImage, bgImage;
	Sound killSound;
	Music bgMusic;
	OrthographicCamera camera;
	Rectangle hero;
	Array<Rectangle> monsters;
	Array<Rectangle> heroAttacks;
	private long lastSpawnTime;
	private long lastAttackTime;


	@Override
	public void create () {
		batch = new SpriteBatch();

		monsImage = new Texture(Gdx.files.internal("img/mons.png"));
		mcImage = new Texture(Gdx.files.internal("img/mc20px.png"));
		mcAtkImage = new Texture(Gdx.files.internal("hero/atk.png"));
		killSound = Gdx.audio.newSound(Gdx.files.internal("sfx/kill.mp3"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/bgMusic.mp3"));
		//bgImage = new Texture(Gdx.files.internal("bg/bg.jpg"));

		bgMusic.setLooping(true);
		bgMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		hero = new Rectangle();
		hero.x = 800/2 - 20/2;
		hero.y = 480/2 - 30/2;
		hero.width = 20;
		hero.height = 30;

		monsters = new Array<Rectangle>();
		spawnMonster();

		heroAttacks = new Array<Rectangle>();
		spawnHeroAtk();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(mcImage, hero.x, hero.y);
		for(Rectangle monster: monsters) {
			batch.draw(monsImage, monster.x, monster.y);
		}
		batch.end();

		batch.begin();
		for(Rectangle heroAtk: heroAttacks) {
			batch.draw(mcAtkImage, heroAtk.x, heroAtk.y);
		}
		batch.end();

		if(Gdx.input.isKeyPressed(Input.Keys.A)) hero.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.D)) hero.x += 200 * Gdx.graphics.getDeltaTime();
		if(hero.x < 0) hero.x = 0;
		if(hero.x > 800 - 64) hero.x = 800 - 64;
		if(Gdx.input.isKeyPressed(Input.Keys.S)) hero.y -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.W)) hero.y += 200 * Gdx.graphics.getDeltaTime();
		if(hero.y < 0) hero.y = 0;
		if(hero.y > 480 - 64) hero.y = 480 - 64;

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			hero.x = touchPos.x - 20 / 2;
			hero.y = touchPos.y - 20 / 2;
		}

		if(TimeUtils.nanoTime() - lastSpawnTime > 900000000) spawnMonster();
		for (Iterator<Rectangle> iter = monsters.iterator(); iter.hasNext(); ) {
			Rectangle monster = iter.next();
			Vector2 zombiePos = new Vector2(monster.getPosition(new Vector2()));
			Vector2 playerPos = new Vector2(hero.getPosition(new Vector2()));
			Vector2 direction = new Vector2();
			direction.x = (playerPos.x) - (zombiePos.x);
			direction.y = (playerPos.y) - (zombiePos.y);
			direction.nor();
			monster.x += direction.x * 1;
			monster.y += direction.y * 1;
			if(monster.y + 64 < 0) iter.remove();
			if(monster.overlaps(hero)) {
				//killSound.play();
				iter.remove();
			}
		}

		if(TimeUtils.nanoTime() - lastAttackTime > 900000000) spawnHeroAtk();
		for (Iterator<Rectangle> iter = heroAttacks.iterator(); iter.hasNext(); ) {
			Rectangle heroAtk = iter.next();
			heroAtk.y -= 200 * Gdx.graphics.getDeltaTime();
			if(heroAtk.y + 64 < 0) iter.remove();
//			if(heroAtk.overlaps(mon)) {
//				killSound.play();
//				iter.remove();
//			}
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	private void spawnMonster() {
		Rectangle monster = new Rectangle();
		int randomize = MathUtils.random(0,3);
		if(randomize == 0){
			monster.x = 0;
			monster.y = MathUtils.random(0, 480-64);
		}
		else if(randomize == 1){
			monster.x = 800-64;
			monster.y = MathUtils.random(0, 480-64);
		}
		else if(randomize == 2){
			monster.x = MathUtils.random(0, 800-64);
			monster.y = 0;
		}
		else{
			monster.x = MathUtils.random(0, 800-64);
			monster.y = 480-64;
		}
		monster.width = 64;
		monster.height = 64;
		monsters.add(monster);
		lastSpawnTime = TimeUtils.nanoTime();
	}

	private void spawnHeroAtk() {
		Rectangle heroAtk = new Rectangle();
		heroAtk.x = hero.x;
		heroAtk.y = hero.y;
		heroAtk.width = 64;
		heroAtk.height = 64;
		heroAttacks.add(heroAtk);
		lastAttackTime = TimeUtils.nanoTime();
	}
}