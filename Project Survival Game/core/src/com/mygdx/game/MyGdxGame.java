package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, monsImage, mcImage, mcAtkImage, bgImage, blank, xpImage;
	Sound killSound;
	Music bgMusic;
	OrthographicCamera camera;
	Rectangle hero;
//	Array<Rectangle> monsters;
//	Array<Rectangle> heroAttacks;
	private long lastSpawnTime;
	private long lastAttackTime;
//	Array<Monster> monsterObjects;
	Array<Pair<Rectangle, Bullet>> bulletArray;
	Hero heroObject = new Hero();
	int screenWidth;
	float timeDelay = 0.2f;
	float timeSeconds = 0f;
	static boolean leveledUp = false;
	Array<Pair<Rectangle, Xp>> xpArray;
	Array<Pair<Rectangle, Monster>> monsArray;
	private Viewport viewport;

	@Override
	public void create () {
		//Generate Image, Sound, Music
		batch = new SpriteBatch();
		monsImage = new Texture(Gdx.files.internal("img/mons.png"));
		mcImage = new Texture(Gdx.files.internal("img/mc20px.png"));
		mcAtkImage = new Texture(Gdx.files.internal("hero/atk.png"));
		killSound = Gdx.audio.newSound(Gdx.files.internal("sfx/kill.mp3"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/bgMusic.mp3"));
		bgImage = new Texture(Gdx.files.internal("bg/bg.jpg"));
		//make the bgImage repeated
		bgImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		blank = new Texture(Gdx.files.internal("bg/blank.jpeg"));
		xpImage = new Texture(Gdx.files.internal("img/xp.png"));

		//Generate music
		bgMusic.setLooping(true);
		bgMusic.setVolume(0.5f);
		bgMusic.play();
		//Generate camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

		//set hero rectangle position and size
		hero = new Rectangle();
		hero.x = Gdx.graphics.getWidth()/2 - 20/2;
		hero.y = Gdx.graphics.getHeight()/2 - 30/2;
		hero.width = 20;
		hero.height = 30;

		//generate monster array rectangle and arrayobject
		monsArray = new Array<>();

		//generate  bullet array rectangle and arrayobject
		bulletArray = new Array<>();

		// generate xp array
		xpArray = new Array<>();

		screenWidth = Gdx.graphics.getWidth();
	}

	public void resize(int w, int h){
		viewport.update(w,h);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		camera.position.set(hero.x,hero.y,0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//generate repeated infinite background
		batch.draw(bgImage, camera.position.x - camera.viewportWidth/2, camera.position.y - camera.viewportHeight/2, (int)hero.x-bgImage.getWidth(),bgImage.getHeight() - (int)hero.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(mcImage, hero.x, hero.y);

		//Draw image to each monster or heroAtk (bullet)
		for(Pair<Rectangle, Monster> monster: monsArray) {
			batch.draw(monsImage, monster.getKey().x, monster.getKey().y);
		}
		for(Pair<Rectangle, Bullet> bullet: bulletArray) {
			batch.draw(mcAtkImage, bullet.getKey().x, bullet.getKey().y);
		}
		for(Pair<Rectangle, Xp> xp: xpArray) {
			batch.draw(xpImage, xp.getKey().x, xp.getKey().y);
		}
		//Draw hp bar
		batch.draw(blank,hero.getX()-camera.viewportWidth/2,hero.getY()-camera.viewportHeight/2,screenWidth * ((float) heroObject.getHp()/heroObject.getMaxHp()),5);
		batch.end();

		if(Gdx.input.isTouched()) leveledUp = true;
		if(Gdx.input.isKeyPressed(Input.Keys.P)) leveledUp = false;

		if(!leveledUp){
			//Move Hero using WASD on keyboard
			if (Gdx.input.isKeyPressed(Input.Keys.A)) hero.x -= 150 * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Input.Keys.D)) hero.x += 150 * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Input.Keys.S)) hero.y -= 150 * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Input.Keys.W)) hero.y += 150 * Gdx.graphics.getDeltaTime();

			//Interval time spawn monster
			if (TimeUtils.nanoTime() - lastSpawnTime > 900000000) spawnMonster();

			for (Iterator<Pair<Rectangle, Monster>> monsterIter = monsArray.iterator(); monsterIter.hasNext(); ) {
				Pair<Rectangle, Monster> monster = monsterIter.next();
				//Getting monster and hero position
				//make all the monster follow hero by keep updating the hero position
				Vector2 monsterPos = new Vector2(monster.getKey().getPosition(new Vector2()));
				Vector2 heroPos = new Vector2(hero.getPosition(new Vector2()));
				Vector2 direction = new Vector2();
				direction.x = (heroPos.x + hero.width / 2) - (monsterPos.x + monster.getKey().width / 2);
				direction.y = (heroPos.y + hero.height / 2) - (monsterPos.y + monster.getKey().height / 2);
				direction.nor();
				//speed of monster following hero
				monster.getKey().x += direction.x * 1;
				monster.getKey().y += direction.y * 1;

				if (monster.getKey().overlaps(hero)) {
					//delay the overlaps
					timeSeconds += Gdx.graphics.getDeltaTime();
					if (timeSeconds > timeDelay) {
						timeSeconds -= timeDelay;
						heroObject.isAttacked(monster.getValue().getAtk());
						if (!heroObject.isLive()) {
						}
					}
				}
			}

			for (Iterator<Pair<Rectangle, Bullet>> bulletIter = bulletArray.iterator(); bulletIter.hasNext(); ) {
				Pair<Rectangle, Bullet> bullet = bulletIter.next();
				bullet.getKey().x += bullet.getValue().getBulletDirection().x * 10;
				bullet.getKey().y += bullet.getValue().getBulletDirection().y * 10;
			}


			for (Iterator<Pair<Rectangle, Xp>> xpIter = xpArray.iterator(); xpIter.hasNext(); ) {
				Pair<Rectangle, Xp> exp = xpIter.next();
				exp.getKey().x = exp.getValue().getX();
				exp.getKey().y = exp.getValue().getY();
			}

			// Check for collisions
			Set<Pair<Pair<Rectangle, Bullet>, Pair<Rectangle, Monster>>> collisions = new HashSet<>();
			for (Pair<Rectangle, Bullet> bullet : bulletArray) {
				for (Pair<Rectangle, Monster> monster : monsArray) {
					if (bullet.getKey().overlaps(monster.getKey())) {
						collisions.add(new Pair<>(bullet, monster));
					}
				}
			}

			// Process collisions
			for (Pair<Pair<Rectangle, Bullet>, Pair<Rectangle, Monster>> collision : collisions) {
				Rectangle bullet = collision.getKey().getKey();
				Rectangle monster = collision.getValue().getKey();
				Monster monsterObject = collision.getValue().getValue();
				Bullet bulletObject = collision.getKey().getValue();

				monsterObject.isAttacked(heroObject.getAtk());
				long id = killSound.play();
				killSound.setVolume(id, 0.2f);
				bulletArray.removeValue(collision.getKey(), false);

				if (!monsterObject.isLive()) {
					spawnXp(monster);
					monsArray.removeValue(collision.getValue(), false);
				}
			}
			for (Pair<Rectangle, Xp> exp: xpArray) {
				if(hero.overlaps(exp.getKey())){
					heroObject.calculateXp(exp.getValue().getAmount());
					System.out.println(heroObject.getXp());
					System.out.println(heroObject.getLevel());
					xpArray.removeValue(exp,false);
				}
			}

			//Interval time for hero to shoot magic bullet
			if (TimeUtils.nanoTime() - lastAttackTime > 900000000) spawnHeroAtk();

		}
		else {
			//Do anything when leveledUp
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
			monster.x = camera.position.x - camera.viewportWidth/2;
			monster.y = MathUtils.random(camera.position.y - camera.viewportHeight/2, camera.position.y + camera.viewportHeight/2);
		}
		else if(randomize == 1){
			monster.x = camera.position.x + camera.viewportWidth/2;
			monster.y = MathUtils.random(camera.position.y - camera.viewportHeight/2, camera.position.y + camera.viewportHeight/2);
		}
		else if(randomize == 2){
			monster.x = MathUtils.random(camera.position.x - camera.viewportWidth/2, camera.position.x + camera.viewportWidth/2);
			monster.y = camera.position.y - camera.viewportHeight/2;
		}
		else{
			monster.x = MathUtils.random(camera.position.x - camera.viewportWidth/2, camera.position.x + camera.viewportWidth/2);
			monster.y = camera.position.y - camera.viewportHeight/2;
		}
		monster.width = 64;
		monster.height = 64;

		monsArray.add(new Pair<>(monster, new Monster()));
		lastSpawnTime = TimeUtils.nanoTime();
	}

	private void spawnHeroAtk() {
		Rectangle heroAtk = new Rectangle();
		heroAtk.x = hero.x;
		heroAtk.y = hero.y;
		heroAtk.width = 1;
		heroAtk.height = 1;

		Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
		Vector2 heroPos = new Vector2(hero.getPosition(new Vector2()));
		camera.unproject(touchPos);
		Vector2 bulletDirection = new Vector2((touchPos.x) - (heroPos.x + hero.width), (touchPos.y) - (heroPos.y + hero.height));
		bulletArray.add(new Pair<>(heroAtk, new Bullet(bulletDirection)));
		lastAttackTime = TimeUtils.nanoTime();
	}

	private void spawnXp(Rectangle _monsters){
		Rectangle xp = new Rectangle();
		xp.width = 25;
		xp.height = 25;
		int randomize = MathUtils.random(0,2);
		if(randomize == 0){
			xpArray.add(new Pair<>(xp, new SmallXp(_monsters.x+64/2, _monsters.y+64/2)));
		}
		else if(randomize == 1) {
			xpArray.add(new Pair<>(xp, new MediumXp(_monsters.x+64/2, _monsters.y+64/2)));
		}
		else if(randomize == 2){
			xpArray.add(new Pair<>(xp, new LargeXp(_monsters.x+64/2, _monsters.y+64/2)));
		}
	}
}