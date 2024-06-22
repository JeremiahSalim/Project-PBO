package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GameScreen implements Screen {
    MyGdxGame game;
    SpriteBatch batch;
    Texture img, monsImage, spiritAtkImage, bgImage, blank, xpImage, xpBar, chestImage;

    Sound killSound;
    Music bgMusic;
    OrthographicCamera camera;
    private long lastSpawnTime;
    private long lastAttackTime;
    private long lastSpiritTime;
    private Array<Pair<Rectangle, Bullet>> bulletArray;
    private Array<Pair<Rectangle, Bullet>> spiritBulletArray;
    float timeDelay = 0.2f;
    float timeSeconds = 0f;
    float skillDelay = 0.25f;
    float skillSeconds = 0f;
    static boolean leveledUp = false;
    private Array<Pair<Rectangle, Xp>> xpArray;
    private Array<Pair<Rectangle, Chest>> chestArray;
    private Array<Pair<Rectangle, Monster>> monsArray;

    private Pair<Rectangle, Hero> mc;
    private ScreenViewport viewport;

    private Animation<TextureRegion> mcAtk;

    private float atkTime = 0f;
    private float atkDelay = 0.9f;
    private float spiritAtkTime = 0f;
    private float spiritAtkDelay = 0.9f;
    private float spawnTime = 0f;
    private float spawnDelay = 0.9f;

    private Spawner spawner;

    LeveledUpScreen levelScreen;




    public GameScreen(MyGdxGame game) {
        this.game = game;
        batch = new SpriteBatch();

        spiritAtkImage = new Texture(Gdx.files.internal("hero/atk.png"));

        Texture rawAtk = new Texture(Gdx.files.internal("hero/basicAtk.png"));
        TextureRegion[][] BAFrames = TextureRegion.split(rawAtk, rawAtk.getWidth()/6, rawAtk.getHeight());
        TextureRegion[] basicAtk = new TextureRegion[6];
        int idx = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 6; j++) {
                basicAtk[idx] = BAFrames[i][j];
                idx++;
            }
        }
        mcAtk = new Animation<>(0.1f, basicAtk);

        killSound = Gdx.audio.newSound(Gdx.files.internal("sfx/kill.mp3"));
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/bgMusic.mp3"));
        bgImage = new Texture(Gdx.files.internal("bg/bg.jpg"));
        //make the bgImage repeated
        bgImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        blank = new Texture(Gdx.files.internal("bar/blank.jpeg"));
        xpImage = new Texture(Gdx.files.internal("hero/xp.png"));
        xpBar = new Texture(Gdx.files.internal("bar/xpBar.jpg"));
        chestImage = new Texture(Gdx.files.internal("hero/chest.png"));

        //Generate music
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f);
        bgMusic.play();
        //Generate camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);


        //Make new screen
        levelScreen = new LeveledUpScreen(game,batch);

        //rectangle hero
        //set hero rectangle position and size
        mc = new Pair<>(new Rectangle(), new Hero(levelScreen, this));
        mc.getKey().width = 64;
        mc.getKey().height = (mc.getKey().width * 157) / 120;
        mc.getKey().x = Gdx.graphics.getWidth() / 2 - mc.getKey().width / 2;
        mc.getKey().y = Gdx.graphics.getHeight() / 2 - mc.getKey().height / 2;

        levelScreen.setMc(mc);

        //generate monster array rectangle and arrayobject
        monsArray = new Array<>();

        //generate  bullet array rectangle and arrayobject
        bulletArray = new Array<>();

        // generate xp array
        xpArray = new Array<>();

        //generate chest Array
        chestArray = new Array<>();

        //generate spirit bullet array
        spiritBulletArray = new Array<>();
        spawner = new Spawner();
        spawner.spawnMonster(camera, monsArray);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);
        camera.position.set(mc.getKey().x + mc.getKey().getWidth()/2, mc.getKey().y + mc.getKey().getHeight()/2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //generate repeated infinite background
        batch.draw(bgImage, camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2, (int) mc.getKey().x - bgImage.getWidth(), bgImage.getHeight() - (int) mc.getKey().y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        //Delay skill per 1 seconds
        skillSeconds += Gdx.graphics.getDeltaTime();
        for (Skill s : mc.getValue().getSkills()){
            if ((s instanceof SkillElectricField || s instanceof SkillSpirit)){
                useSkill(s);
            }
        }


        batch.begin();
        //Draw image to each monster
        for (Pair<Rectangle, Monster> monster : monsArray) {
            if (mc.getKey().x + mc.getKey().width/2 > monster.getKey().x) {
                TextureRegion currentState = monster.getValue().getMonsAnimRight().getKeyFrame(monster.getValue().getStateTime(), true);
                batch.draw(currentState, monster.getKey().x, monster.getKey().y, monster.getKey().getWidth(), monster.getKey().getHeight());
            }else{
                TextureRegion currentState = monster.getValue().getMonsAnimLeft().getKeyFrame(monster.getValue().getStateTime(), true);
                batch.draw(currentState, monster.getKey().x, monster.getKey().y, monster.getKey().getWidth(), monster.getKey().getHeight());
            }

        }

        //Draw image to each heroAtk (bullet)
        for (Pair<Rectangle, Bullet> bullet : bulletArray) {
            TextureRegion currentState = mcAtk.getKeyFrame(bullet.getValue().getStateTime(), true);
            batch.draw(currentState, bullet.getKey().x, bullet.getKey().y, 25, 25);
            //batch.draw(mcAtkImage, bullet.getKey().x, bullet.getKey().y);
        }

        //Draw image to each spiritAtk (bullet)
        for (Pair<Rectangle, Bullet> bullet : spiritBulletArray) {
            batch.draw(spiritAtkImage, bullet.getKey().x, bullet.getKey().y);
        }
        //Draw image to each xp
        for (Pair<Rectangle, Xp> xp : xpArray) {
            batch.draw(xpImage, xp.getKey().x, xp.getKey().y);
        }
        //Draw image to each chest
        for (Pair<Rectangle, Chest> chest : chestArray) {
            batch.draw(chestImage, chest.getKey().x, chest.getKey().y);
        }
        //Draw hp bar
        batch.draw(blank, mc.getKey().getX() + mc.getKey().getWidth()/2 - camera.viewportWidth / 2, mc.getKey().getY() + mc.getKey().getHeight()/2 - camera.viewportHeight / 2, Gdx.graphics.getWidth() * ((float) mc.getValue().getHp() / mc.getValue().getMaxHp()), 5);

        //Draw xp Bar
        batch.draw(xpBar, mc.getKey().getX() + mc.getKey().getWidth()/2 - camera.viewportWidth / 2, mc.getKey().getY() + mc.getKey().getHeight()/2 + camera.viewportHeight / 2 - 10, Gdx.graphics.getWidth() * ((float) mc.getValue().getXp() / mc.getValue().getMaxXp()), 10);
        batch.end();


        //Draw Hero based on mcState
        mc.getValue().drawMove(batch, mc.getKey());

        if (!leveledUp) {
            skillSeconds += Gdx.graphics.getDeltaTime();
            for (Pair<Rectangle, Monster> monster : monsArray) {
                monster.getValue().setStateTime(monster.getValue().getStateTime() + Gdx.graphics.getDeltaTime());
            }

            //Draw image to each heroAtk (bullet)
            for (Pair<Rectangle, Bullet> bullet : bulletArray) {
                bullet.getValue().setStateTime(bullet.getValue().getStateTime() + Gdx.graphics.getDeltaTime());
            }


            spawnTime += Gdx.graphics.getDeltaTime();
            atkTime += Gdx.graphics.getDeltaTime();
            //Change hero moveState based on KeyPressed
            batch.begin();
            if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W)) {
                mc.getValue().setStateTime(mc.getValue().getStateTime()+ Gdx.graphics.getDeltaTime());
                mc.getValue().setMcState("NW");
            } else if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S)) {
                mc.getValue().setStateTime(mc.getValue().getStateTime()+ Gdx.graphics.getDeltaTime());
                mc.getValue().setMcState("SW");
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W)) {
                mc.getValue().setStateTime(mc.getValue().getStateTime()+ Gdx.graphics.getDeltaTime());
                mc.getValue().setMcState("NE");
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.S)) {
                mc.getValue().setStateTime(mc.getValue().getStateTime()+ Gdx.graphics.getDeltaTime());
                mc.getValue().setMcState("SE");
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                mc.getValue().setStateTime(mc.getValue().getStateTime()+ Gdx.graphics.getDeltaTime());
                mc.getValue().setMcState("Left");
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                mc.getValue().setStateTime(mc.getValue().getStateTime()+ Gdx.graphics.getDeltaTime());
                mc.getValue().setMcState("Right");
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                mc.getValue().setStateTime(mc.getValue().getStateTime()+ Gdx.graphics.getDeltaTime());
                mc.getValue().setMcState("Down");
            } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                mc.getValue().setStateTime(mc.getValue().getStateTime()+ Gdx.graphics.getDeltaTime());
                mc.getValue().setMcState("Up");
            } else mc.getValue().setMcState("default");
            batch.end();

            //Move the mc
            mc.getValue().move(mc.getKey());


            float distance = 5000f;

            Pair<Rectangle,Monster> monsterPair = new Pair<>();



            for (Iterator<Pair<Rectangle, Monster>> monsterIter = monsArray.iterator(); monsterIter.hasNext(); ) {
                Pair<Rectangle, Monster> monster = monsterIter.next();
                //Getting monster and hero position
                //make all the monster follow hero by keep updating the hero position
                Vector2 monsterPos = new Vector2(monster.getKey().getPosition(new Vector2()));
                Vector2 heroPos = new Vector2(mc.getKey().getPosition(new Vector2()));
                Vector2 direction = new Vector2();
                float distance2 = Vector2.dst(heroPos.x,heroPos.y,monsterPos.x,monsterPos.y);
                direction.x = (heroPos.x + mc.getKey().width / 2) - (monsterPos.x + monster.getKey().width / 2);
                direction.y = (heroPos.y + mc.getKey().height / 2) - (monsterPos.y + monster.getKey().height / 2);
                direction.nor();
                if(distance2 < distance){
                    distance = distance2;
                    monsterPair = monster;
                }

                if (monster.getKey().overlaps(mc.getKey())) {
                    //delay the overlaps
                    timeSeconds += Gdx.graphics.getDeltaTime();
                    if (timeSeconds > timeDelay) {
                        timeSeconds -= timeDelay;
                        mc.getValue().isAttacked(monster.getValue().getAtk());
                        if (!mc.getValue().isLive()) {
                        }
                    }
                }
                //Check if hero have ElectroFieldSkill
                for (Skill s:mc.getValue().getSkills()) {
                    if(s instanceof SkillElectricField){
                        //check if electric field overlaps monster
                        int index = 0;
                        for (int i = 0; i < mc.getValue().getSkills().size(); i++) {
                            if (mc.getValue().getSkills().get(i) instanceof SkillElectricField) {
                                index = i;
                            }
                        }
                        if (Intersector.overlaps(((SkillElectricField)(mc.getValue().getSkills().get(index))).getArea(), monster.getKey())){
                            if (skillSeconds>skillDelay) {
                                monster.getValue().isAttacked((int) mc.getValue().getSkills().get(index).getValue());
                                if (!monster.getValue().isLive()) {
                                    long id = killSound.play();
                                    killSound.setVolume(id, 0.1f);
                                    spawner.spawnCollectible(monster.getKey(), xpArray, chestArray);
                                    monsArray.removeValue(monster, false);
                                }
                            }
                        }
                    }
                }


                //speed of monster following hero
                monster.getKey().x += direction.x * 1;
                monster.getKey().y += direction.y * 1;
            }

            //Hero attack move logic
            for (Iterator<Pair<Rectangle, Bullet>> bulletIter = bulletArray.iterator(); bulletIter.hasNext(); ) {
                Pair<Rectangle, Bullet> bullet = bulletIter.next();
                bullet.getKey().x += bullet.getValue().getBulletDirection().x * 10;
                bullet.getKey().y += bullet.getValue().getBulletDirection().y * 10;
            }

            //Spiritattack move logic
            for (Iterator<Pair<Rectangle, Bullet>> bulletIter = spiritBulletArray.iterator(); bulletIter.hasNext(); ) {
                Pair<Rectangle, Bullet> bullet = bulletIter.next();
                bullet.getKey().x += bullet.getValue().getBulletDirection().x * 10;
                bullet.getKey().y += bullet.getValue().getBulletDirection().y * 10;
            }

            // Check for collisions between monster and heroatk
            Set<Pair<Pair<Rectangle, Bullet>, Pair<Rectangle, Monster>>> collisions = new HashSet<>();
            for (Pair<Rectangle, Bullet> bullet : bulletArray) {
                for (Pair<Rectangle, Monster> monster : monsArray) {
                    if (bullet.getKey().overlaps(monster.getKey())) {
                        collisions.add(new Pair<>(bullet, monster));
                    }
                }
            }

            // Process collisions between monster and heroatk
            for (Pair<Pair<Rectangle, Bullet>, Pair<Rectangle, Monster>> collision : collisions) {
                Rectangle bullet = collision.getKey().getKey();
                Rectangle monster = collision.getValue().getKey();
                Monster monsterObject = collision.getValue().getValue();
                Bullet bulletObject = collision.getKey().getValue();

                monsterObject.isAttacked(mc.getValue().getAtk());
                bulletArray.removeValue(collision.getKey(), false);

                if (!monsterObject.isLive()) {
                    long id = killSound.play();
                    killSound.setVolume(id, 0.1f);
                    spawner.spawnCollectible(monster, xpArray, chestArray);
                    monsArray.removeValue(collision.getValue(), false);
                }
            }

            // Check for collisions between monster and spiritAtk
            Set<Pair<Pair<Rectangle, Bullet>, Pair<Rectangle, Monster>>> spiritCollisions = new HashSet<>();
            for (Pair<Rectangle, Bullet> bullet : spiritBulletArray) {
                for (Pair<Rectangle, Monster> monster : monsArray) {
                    if (bullet.getKey().overlaps(monster.getKey())) {
                        spiritCollisions.add(new Pair<>(bullet, monster));
                    }
                }
            }

            // Process collisions between monster and spiritAtk
            for (Pair<Pair<Rectangle, Bullet>, Pair<Rectangle, Monster>> collision : spiritCollisions) {
                Rectangle monster = collision.getValue().getKey();
                Monster monsterObject = collision.getValue().getValue();
                Skill skillSpirit = new Skill();
                for (Skill s: mc.getValue().getSkills()){
                    if(s instanceof SkillSpirit){
                        skillSpirit = s;
                    }
                }

                monsterObject.isAttacked((int)skillSpirit.getValue());
                spiritBulletArray.removeValue(collision.getKey(), false);

                if (!monsterObject.isLive()) {
                    long id = killSound.play();
                    killSound.setVolume(id, 0.1f);
                    spawner.spawnCollectible(monster, xpArray, chestArray);
                    monsArray.removeValue(collision.getValue(), false);
                }
            }

            //collision between hero and xp
            for (Pair<Rectangle, Xp> exp : xpArray) {
                if (mc.getKey().overlaps(exp.getKey())) {
                    mc.getValue().calculateXp(exp.getValue().getAmount());
                    xpArray.removeValue(exp, false);
                }
            }

            //collision between hero and chest
            for (Pair<Rectangle, Chest> ch : chestArray) {
                if (mc.getKey().overlaps(ch.getKey())) {
                    //Code goes here...
                    levelScreen.updateTable(ch.getValue().getList3Skill());
                    leveledUp = true;
                    chestArray.removeValue(ch, false);
                }
            }

            //Interval time for hero to shoot magic bullet
            if (atkTime > atkDelay) {
                spawner.spawnHeroAtk(camera, mc, bulletArray);
                atkTime -= atkDelay;
            }

            //Interval time spawn monster 900000000
            if (spawnTime > spawnDelay) {
                spawner.spawnMonster(camera, monsArray);
                spawnTime -= spawnDelay;
            }

            boolean haveSkillSpirit = false;
            for (Skill s:mc.getValue().getSkills()) {
                if(s instanceof SkillSpirit){
                    haveSkillSpirit = true;
                }
            }
            if(haveSkillSpirit){
                spiritAtkTime += Gdx.graphics.getDeltaTime();
                if (spiritAtkTime > spiritAtkDelay) {
                    spawner.spawnSpiritAtk(monsterPair, mc, spiritBulletArray);
                    spiritAtkTime -= spiritAtkDelay;
                }
            }

            for (Skill s:mc.getValue().getSkills()) {
                if (s instanceof SkillRegenHP){
                    if (skillSeconds>skillDelay){
                        useSkill(s);
                    }
                }
            }

            //Refresh skillseconds
            if (skillSeconds>skillDelay){
                skillSeconds -= skillDelay;
            }

        } else {
            //Do anything when leveld up
            levelScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

//    private void spawnMonster(Texture monsImage) {
//        Rectangle monster = new Rectangle();
//        monster.width = monsImage.getWidth();
//        monster.height = monsImage.getHeight();
//        int randomize = MathUtils.random(0, 4);
//        if (randomize == 0) {
//            monster.x = camera.position.x - camera.viewportWidth / 2 - monster.width;
//            monster.y = MathUtils.random(camera.position.y - camera.viewportHeight / 2, camera.position.y + camera.viewportHeight / 2);
//        } else if (randomize == 1) {
//            monster.x = camera.position.x + camera.viewportWidth / 2;
//            monster.y = MathUtils.random(camera.position.y - camera.viewportHeight / 2, camera.position.y + camera.viewportHeight / 2);
//        } else if (randomize == 2) {
//            monster.x = MathUtils.random(camera.position.x - camera.viewportWidth / 2, camera.position.x + camera.viewportWidth / 2);
//            monster.y = camera.position.y - camera.viewportHeight / 2 - monster.height;
//        } else {
//            monster.x = MathUtils.random(camera.position.x - camera.viewportWidth / 2, camera.position.x + camera.viewportWidth / 2);
//            monster.y = camera.position.y + camera.viewportHeight / 2;
//        }
//
//        monsArray.add(new Pair<>(monster, new Monster()));
//        //lastSpawnTime = TimeUtils.nanoTime();
//    }
//
//    private void spawnHeroAtk() {
//        Rectangle heroAtk = new Rectangle();
//        heroAtk.x = mc.getKey().x + mc.getKey().width / 2;
//        heroAtk.y = mc.getKey().y + mc.getKey().height / 3;
//        heroAtk.width = 1;
//        heroAtk.height = 1;
//
//        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
//        Vector2 heroPos = new Vector2(mc.getKey().getPosition(new Vector2()));
//        camera.unproject(touchPos);
//        Vector2 bulletDirection = new Vector2((touchPos.x) - (heroPos.x + mc.getKey().width / 2), (touchPos.y) - (heroPos.y + mc.getKey().height / 2));
//        bulletArray.add(new Pair<>(heroAtk, new Bullet(bulletDirection)));
//        //lastAttackTime = TimeUtils.nanoTime();
//    }
//
//    private void spawnSpiritAtk(Pair<Rectangle,Monster> monster) {
//        Rectangle spiritAtk = new Rectangle();
//        spiritAtk.x = mc.getKey().getX() - 60;
//        spiritAtk.y = mc.getKey().getY() + 60;
//        spiritAtk.width = 1;
//        spiritAtk.height = 1;
//
//        Vector2 monsPos = new Vector2(monster.getKey().getPosition(new Vector2()));
//        Vector2 spiritPos = new Vector2(mc.getKey().getPosition(new Vector2()));
//        spiritPos.x -= 60;
//        spiritPos.y += 60;
//        Vector2 bulletDirection = new Vector2((monsPos .x + monster.getKey().width/2) - (spiritPos.x ), (monsPos .y+ monster.getKey().width/2) - (spiritPos.y ));
//        spiritBulletArray.add(new Pair<>(spiritAtk, new Bullet(bulletDirection)));
//        //lastSpiritTime = TimeUtils.nanoTime();
//    }
//
//    private void spawnCollectible(Rectangle _monsters) {
//        Rectangle xp = new Rectangle();
//        xp.width = 25;
//        xp.height = 25;
//        xp.x = _monsters.x + _monsters.getWidth() / 2;
//        xp.y = _monsters.y + _monsters.getHeight() / 2;
//        int randomize = MathUtils.random(0, 100);
//        if (randomize >= 0 && randomize <= 49) {
//            xpArray.add(new Pair<>(xp, new SmallXp(_monsters.x + _monsters.getWidth() / 2 - xp.getWidth()/2, _monsters.y + _monsters.getHeight() / 2 - xp.getHeight()/2)));
//        } else if (randomize >= 50 && randomize <= 79) {
//            xpArray.add(new Pair<>(xp, new MediumXp(_monsters.x + _monsters.getWidth() / 2 - xp.getWidth()/2, _monsters.y + _monsters.getHeight() / 2 - xp.getHeight()/2)));
//        } else if (randomize >= 80 && randomize <= 98) {
//            xpArray.add(new Pair<>(xp, new LargeXp(_monsters.x +_monsters.getWidth() / 2 - xp.getWidth()/2, _monsters.y + _monsters.getHeight() / 2 - xp.getHeight()/2)));
//        }
//        else{
//            spawnChest(_monsters);
//        }
//    }
//
//    private void spawnChest(Rectangle _monsters) {
//        Rectangle chest = new Rectangle();
//        chest.width = 25;
//        chest.height = 25;
//        chest.x = _monsters.x + (float) monsImage.getWidth() / 2;
//        chest.y = _monsters.y + (float) monsImage.getHeight() / 2;
//        chestArray.add(new Pair<>(chest, new Chest(_monsters.x + (float) monsImage.getWidth() / 2, _monsters.y + (float) monsImage.getHeight() / 2)));
//    }

    //Loop array skill that hero currently have
    private void useSkill(Skill s){
        s.skillEffect(mc, batch);
    }

    public Pair<Rectangle, Hero> getMc() {
        return mc;
    }

    public void setLevelScreen(LeveledUpScreen levelScreen) {
        this.levelScreen = levelScreen;
    }
}
