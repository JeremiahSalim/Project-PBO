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

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, monsImage, mcImage, mcAtkImage, bgImage, blank;
	Sound killSound;
	Music bgMusic;
	OrthographicCamera camera;
	Rectangle hero;
	Array<Rectangle> monsters;
	Array<Rectangle> heroAttacks;
	private long lastSpawnTime;
	private long lastAttackTime;

	Array<Monster> monsterObjects;
	Array<Bullet> bulletArray;

	Hero heroObject = new Hero();


	@Override
	public void create () {
		//Generate Image, Sound, Music
		batch = new SpriteBatch();
		monsImage = new Texture(Gdx.files.internal("img/mons.png"));
		mcImage = new Texture(Gdx.files.internal("img/mc20px.png"));
		mcAtkImage = new Texture(Gdx.files.internal("hero/atk.png"));
		killSound = Gdx.audio.newSound(Gdx.files.internal("sfx/kill.mp3"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/bgMusic.mp3"));
		//bgImage = new Texture(Gdx.files.internal("bg/bg.jpg"));
		blank = new Texture(Gdx.files.internal("bg/blank.jpeg"));


		bgMusic.setLooping(true);
		bgMusic.setVolume(0.5f);
		bgMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		//set hero rectangle position and size
		hero = new Rectangle();
		hero.x = 800/2 - 20/2;
		hero.y = 480/2 - 30/2;
		hero.width = 30;
		hero.height = 30;

		//generate new monster array rectangle and arrayobject
		monsters = new Array<Rectangle>();
		monsterObjects = new Array<>();
		spawnMonster();

		//generate new hero magic bullet array rectangle and arrayobject
		heroAttacks = new Array<Rectangle>();
		bulletArray = new Array<>();
		spawnHeroAtk();

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		camera.position.set(hero.x,hero.y,0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(mcImage, hero.x, hero.y);
		for(Rectangle monster: monsters) {
			batch.draw(monsImage, monster.x, monster.y);
		}

		for(Rectangle heroAtk: heroAttacks) {
			batch.draw(mcAtkImage, heroAtk.x, heroAtk.y);
		}

		batch.end();

		//HP BAR (MASIH ERROR)
		batch.begin();
		batch.draw(blank,hero.getX()-500,hero.getY()-200,Gdx.graphics.getWidth()*heroObject.getHp(),5);
		batch.end();

		//Move Hero using WASD on keyboard
		if(Gdx.input.isKeyPressed(Input.Keys.A)) hero.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.D)) hero.x += 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.S)) hero.y -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.W)) hero.y += 200 * Gdx.graphics.getDeltaTime();

		//Interval time spawn monster
		if(TimeUtils.nanoTime() - lastSpawnTime > 900000000) spawnMonster();
		int monsIndex = 0;
		for (Iterator<Rectangle> monsterIter = monsters.iterator(); monsterIter.hasNext(); ) {
			Rectangle monster = monsterIter.next();
			Monster monsterObject = monsterObjects.get(monsIndex);
			//Getting monster and hero position
			//make all the monster follow hero by keep updating the hero position
			Vector2 monsterPos = new Vector2(monster.getPosition(new Vector2()));
			Vector2 heroPos = new Vector2(hero.getPosition(new Vector2()));
			Vector2 direction = new Vector2();
			direction.x = (heroPos.x + 20/2) - (monsterPos.x + 64/2);
			direction.y = (heroPos.y + 30/2) - (monsterPos.y +64/2);
			direction.nor();
			//speed of monster following hero
			monster.x += direction.x * 1;
			monster.y += direction.y * 1;
			float delay = 1; // seconds

			Timer.schedule(new Timer.Task(){
				//hero got attacked by monster if monster overlaps hero
				@Override
				public void run() {
					if(monster.overlaps(hero) && TimeUtils.millis() % 99 == 0) {
						heroObject.isAttacked(monsterObject.getAtk());
						System.out.println(heroObject.getHp());
						//heroObject.setHp(heroObject.getHp() - monsterObject.getAtk());
						if(!heroObject.isLive()){
							System.out.println("GameOver");
						}
					}
				}
			}, delay);

			//Interval time for hero to shoot magic bullet
			if(TimeUtils.nanoTime() - lastAttackTime > 900000000) spawnHeroAtk();
			//bullet targeting
			int bulletIndex = 0;
			for (Iterator<Rectangle> bulletIter = heroAttacks.iterator(); bulletIter.hasNext(); ) {
				Rectangle heroAtk = bulletIter.next();
				Bullet bullet = bulletArray.get(bulletIndex);
				heroAtk.x += bullet.getBulletDirection().x * 0.5f;
				heroAtk.y += bullet.getBulletDirection().y * 0.5f;
				if(heroAtk.overlaps(monster)) {
					monsterObject.isAttacked(heroObject.getAtk());
					long id = killSound.play();
					killSound.setVolume(id, 0.2f);
					bulletIter.remove();
					bulletArray.removeIndex(bulletIndex);
					bulletIndex--;
					if(!monsterObject.isLive()){
						monsterIter.remove();
						monsterObjects.removeIndex(monsIndex);
						monsIndex--;
					}
				}
				bulletIndex++;
			}
			monsIndex++;
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
			monster.x = camera.position.x - 400;
			monster.y = MathUtils.random(camera.position.y - 240, camera.position.y + 240);
		}
		else if(randomize == 1){
			monster.x = camera.position.x + 400;
			monster.y = MathUtils.random(camera.position.y - 240, camera.position.y + 240);
		}
		else if(randomize == 2){
			monster.x = MathUtils.random(camera.position.x - 400, camera.position.x + 400);
			monster.y = camera.position.y - 240;
		}
		else{
			monster.x = MathUtils.random(camera.position.x - 400, camera.position.x + 400);
			monster.y = camera.position.y - 400;
		}
		monster.width = 64;
		monster.height = 64;
		monsters.add(monster);
		monsterObjects.add(new Monster());
		lastSpawnTime = TimeUtils.nanoTime();
	}

	private void spawnHeroAtk() {
		Rectangle heroAtk = new Rectangle();
		heroAtk.x = hero.x;
		heroAtk.y = hero.y;
		heroAtk.width = 1;
		heroAtk.height = 1;
		heroAttacks.add(heroAtk);

		Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
		Vector2 heroPos = new Vector2(hero.getPosition(new Vector2()));
		camera.unproject(touchPos);
		Vector2 bulletDirection = new Vector2();
		bulletDirection.x = (touchPos.x) - (heroPos.x);
		bulletDirection.y = (touchPos.y) - (heroPos.y);
		bulletDirection.nor();
		bulletArray.add(new Bullet(bulletDirection));
		lastAttackTime = TimeUtils.nanoTime();
	}
}