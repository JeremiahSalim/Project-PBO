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
    Texture img, monsImage, mcAtkImage, bgImage, blank, xpImage, xpBar, chestImage;
    Sound killSound;
    Music bgMusic;
    OrthographicCamera camera;
    private long lastSpawnTime;
    private long lastAttackTime;
    Array<Pair<Rectangle, Bullet>> bulletArray;
    float timeDelay = 0.2f;
    float timeSeconds = 0f;
    float skillDelay = 0.25f;
    float skillSeconds = 0f;
    static boolean leveledUp = false;
    Array<Pair<Rectangle, Xp>> xpArray;
    Array<Pair<Rectangle, Chest>> chestArray;
    Array<Pair<Rectangle, Monster>> monsArray;

    Pair<Rectangle, Hero> mc;
    private ScreenViewport viewport;




    public GameScreen(MyGdxGame game) {
        this.game = game;
        batch = new SpriteBatch();
        monsImage = new Texture(Gdx.files.internal("monster/mons.png"));
        mcAtkImage = new Texture(Gdx.files.internal("hero/atk.png"));
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

        //rectangle hero
        //set hero rectangle position and size
        mc = new Pair<>(new Rectangle(), new Hero());
        mc.getKey().width = 64;
        mc.getKey().height = (mc.getKey().width * 157) / 120;
        mc.getKey().x = Gdx.graphics.getWidth() / 2 - mc.getKey().width / 2;
        mc.getKey().y = Gdx.graphics.getHeight() / 2 - mc.getKey().height / 2;

        //generate monster array rectangle and arrayobject
        monsArray = new Array<>();

        //generate  bullet array rectangle and arrayobject
        bulletArray = new Array<>();

        // generate xp array
        xpArray = new Array<>();

        //generate chest Array
        chestArray = new Array<>();
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
//  batch.draw(mcImage, hero.x, hero.y);
        batch.end();

        //Delay skill per 1 seconds
        skillSeconds += Gdx.graphics.getDeltaTime();
        for (Skill s : mc.getValue().getSkills()){
            if (!(s instanceof SkillElectricField)){
                if (skillSeconds>skillDelay){
                    useSkill(s);
                }
            }else useSkill(s);
        }

        batch.begin();
        //Draw image to each monster or heroAtk (bullet)
        for (Pair<Rectangle, Monster> monster : monsArray) {
            batch.draw(monsImage, monster.getKey().x, monster.getKey().y);
        }
        for (Pair<Rectangle, Bullet> bullet : bulletArray) {
            batch.draw(mcAtkImage, bullet.getKey().x, bullet.getKey().y);
        }
        for (Pair<Rectangle, Xp> xp : xpArray) {
            batch.draw(xpImage, xp.getKey().x, xp.getKey().y);
        }
        for (Pair<Rectangle, Chest> chest : chestArray) {
            batch.draw(chestImage, chest.getKey().x, chest.getKey().y);
        }
        //Draw hp bar
        batch.draw(blank, mc.getKey().getX() + mc.getKey().getWidth()/2 - camera.viewportWidth / 2, mc.getKey().getY() + mc.getKey().getHeight()/2 - camera.viewportHeight / 2, Gdx.graphics.getWidth() * ((float) mc.getValue().getHp() / mc.getValue().getMaxHp()), 5);

        //Draw xp Bar
        batch.draw(xpBar, mc.getKey().getX() + mc.getKey().getWidth()/2 - camera.viewportWidth / 2, mc.getKey().getY() + mc.getKey().getHeight()/2 + camera.viewportHeight / 2 - 10, Gdx.graphics.getWidth() * ((float) mc.getValue().getXp() / mc.getValue().getMaxXp()), 10);
        batch.end();



        if (Gdx.input.isTouched()) leveledUp = true;
        if (Gdx.input.isKeyPressed(Input.Keys.P)) leveledUp = false;

        //Draw hero based on WASD on keyboard
        float stateTime = mc.getValue().getStateTime();
        String mcState = mc.getValue().getMcState();
        batch.begin();
        if (mcState.equals("NW")) {
            TextureRegion currentState = mc.getValue().getMcNorthwest().getKeyFrame(stateTime, true);
            batch.draw(currentState, mc.getKey().x, mc.getKey().y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("SW")) {
            TextureRegion currentState = mc.getValue().getMcSouthWest().getKeyFrame(stateTime, true);
            batch.draw(currentState, mc.getKey().x, mc.getKey().y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("NE")) {
            TextureRegion currentState = mc.getValue().getMcNortheast().getKeyFrame(stateTime, true);
            batch.draw(currentState, mc.getKey().x, mc.getKey().y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("SE")) {
            TextureRegion currentState = mc.getValue().getMcSoutheast().getKeyFrame(stateTime, true);
            batch.draw(currentState, mc.getKey().x, mc.getKey().y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("Left")) {
            TextureRegion currentState = mc.getValue().getMcLeft().getKeyFrame(stateTime, true);
            batch.draw(currentState, mc.getKey().x, mc.getKey().y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("Right")) {
            TextureRegion currentState = mc.getValue().getMcRight().getKeyFrame(stateTime, true);
            batch.draw(currentState, mc.getKey().x, mc.getKey().y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("Down")) {
            TextureRegion currentState = mc.getValue().getMcDown().getKeyFrame(stateTime, true);
            batch.draw(currentState, mc.getKey().x, mc.getKey().y, 64, (float) (64 * 157) / 120);
        } else if (mcState.equals("Up")) {
            TextureRegion currentState = mc.getValue().getMcUp().getKeyFrame(stateTime, true);
            batch.draw(currentState, mc.getKey().x, mc.getKey().y, 64, (float) (64 * 157) / 120);
        } else batch.draw(mc.getValue().getMcImage(), mc.getKey().x, mc.getKey().y, 64, (float) (64 * 157) / 120);
        batch.end();

        if (!leveledUp) {
            //Draw hero based on WASD on keyboard
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

            //Move Hero using WASD on keyboard
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                mc.getKey().x -= 150 * Gdx.graphics.getDeltaTime();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                mc.getKey().x += 150 * Gdx.graphics.getDeltaTime();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                mc.getKey().y -= 150 * Gdx.graphics.getDeltaTime();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                mc.getKey().y += 150 * Gdx.graphics.getDeltaTime();
            }

            //Interval time spawn monster 900000000
            if (TimeUtils.nanoTime() - lastSpawnTime > 900000000) spawnMonster(monsImage);

            for (Iterator<Pair<Rectangle, Monster>> monsterIter = monsArray.iterator(); monsterIter.hasNext(); ) {
                Pair<Rectangle, Monster> monster = monsterIter.next();
                //Getting monster and hero position
                //make all the monster follow hero by keep updating the hero position
                Vector2 monsterPos = new Vector2(monster.getKey().getPosition(new Vector2()));
                Vector2 heroPos = new Vector2(mc.getKey().getPosition(new Vector2()));
                Vector2 direction = new Vector2();
                direction.x = (heroPos.x + mc.getKey().width / 2) - (monsterPos.x + monster.getKey().width / 2);
                direction.y = (heroPos.y + mc.getKey().height / 2) - (monsterPos.y + monster.getKey().height / 2);
                direction.nor();


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

                //check if electric field overlaps monster
                int index = 0;
                for (int i = 0; i < mc.getValue().getSkills().size(); i++) {
                    if (mc.getValue().getSkills().get(i) instanceof SkillElectricField) {
                        index = i;
                    }
                }

                skillSeconds += Gdx.graphics.getDeltaTime();
                if (Intersector.overlaps(((SkillElectricField)(mc.getValue().getSkills().get(index))).getArea(), monster.getKey())){
                    if (skillSeconds>skillDelay) {
                        monster.getValue().isAttacked((int) mc.getValue().getSkills().get(index).getValue());
                        if (!monster.getValue().isLive()) {
                            spawnCollectible(monster.getKey());
                            monsArray.removeValue(monster, false);
                        }
                    }
                }
                //speed of monster following hero
                monster.getKey().x += direction.x * 1;
                monster.getKey().y += direction.y * 1;
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

            for (Iterator<Pair<Rectangle, Chest>> chestIter = chestArray.iterator(); chestIter.hasNext(); ) {
                Pair<Rectangle, Chest> ch = chestIter.next();
                ch.getKey().x = ch.getValue().getX();
                ch.getKey().y = ch.getValue().getY();
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

                monsterObject.isAttacked(mc.getValue().getAtk());
                long id = killSound.play();
                killSound.setVolume(id, 0.2f);
                bulletArray.removeValue(collision.getKey(), false);

                if (!monsterObject.isLive()) {
                    spawnCollectible(monster);
                    monsArray.removeValue(collision.getValue(), false);
                }
            }
            for (Pair<Rectangle, Xp> exp : xpArray) {
                if (mc.getKey().overlaps(exp.getKey())) {
                    mc.getValue().calculateXp(exp.getValue().getAmount());
                    System.out.println(mc.getValue().getXp());
                    System.out.println(mc.getValue().getLevel());
                    xpArray.removeValue(exp, false);
                }
            }

            for (Pair<Rectangle, Chest> ch : chestArray) {
                if (mc.getKey().overlaps(ch.getKey())) {
                    //Code goes here...
                    chestArray.removeValue(ch, false);
                }
            }

            //Interval time for hero to shoot magic bullet
            if (TimeUtils.nanoTime() - lastAttackTime > 900000000) spawnHeroAtk();

            //Refresh skillseconds
            if (skillSeconds>skillDelay){
                skillSeconds -= skillDelay;
            }

        } else {
            //Do anything when leveledUp
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

    private void spawnMonster(Texture monsImage) {
        Rectangle monster = new Rectangle();
        monster.width = monsImage.getWidth();
        monster.height = monsImage.getHeight();
        int randomize = MathUtils.random(0, 4);
        if (randomize == 0) {
            monster.x = camera.position.x - camera.viewportWidth / 2 - monster.width;
            monster.y = MathUtils.random(camera.position.y - camera.viewportHeight / 2, camera.position.y + camera.viewportHeight / 2);
        } else if (randomize == 1) {
            monster.x = camera.position.x + camera.viewportWidth / 2;
            monster.y = MathUtils.random(camera.position.y - camera.viewportHeight / 2, camera.position.y + camera.viewportHeight / 2);
        } else if (randomize == 2) {
            monster.x = MathUtils.random(camera.position.x - camera.viewportWidth / 2, camera.position.x + camera.viewportWidth / 2);
            monster.y = camera.position.y - camera.viewportHeight / 2 - monster.height;
        } else {
            monster.x = MathUtils.random(camera.position.x - camera.viewportWidth / 2, camera.position.x + camera.viewportWidth / 2);
            monster.y = camera.position.y + camera.viewportHeight / 2;
        }

        monsArray.add(new Pair<>(monster, new Monster()));
        lastSpawnTime = TimeUtils.nanoTime();
    }

    private void spawnHeroAtk() {
        Rectangle heroAtk = new Rectangle();
        heroAtk.x = mc.getKey().x + mc.getKey().width / 2;
        heroAtk.y = mc.getKey().y + mc.getKey().height / 3;
        heroAtk.width = 1;
        heroAtk.height = 1;

        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector2 heroPos = new Vector2(mc.getKey().getPosition(new Vector2()));
        camera.unproject(touchPos);
        Vector2 bulletDirection = new Vector2((touchPos.x) - (heroPos.x + mc.getKey().width / 2), (touchPos.y) - (heroPos.y + mc.getKey().height / 2));
        bulletArray.add(new Pair<>(heroAtk, new Bullet(bulletDirection)));
        lastAttackTime = TimeUtils.nanoTime();
    }

    private void spawnCollectible(Rectangle _monsters) {
        Rectangle xp = new Rectangle();
        xp.width = 25;
        xp.height = 25;
        xp.x = _monsters.x + _monsters.getWidth() / 2;
        xp.y = _monsters.y + _monsters.getHeight() / 2;
        int randomize = MathUtils.random(0, 100);
        if (randomize >= 0 && randomize <= 49) {
            xpArray.add(new Pair<>(xp, new SmallXp(_monsters.x + _monsters.getWidth() / 2 - xp.getWidth()/2, _monsters.y + _monsters.getHeight() / 2 - xp.getHeight()/2)));
        } else if (randomize >= 50 && randomize <= 79) {
            xpArray.add(new Pair<>(xp, new MediumXp(_monsters.x + _monsters.getWidth() / 2 - xp.getWidth()/2, _monsters.y + _monsters.getHeight() / 2 - xp.getHeight()/2)));
        } else if (randomize >= 80 && randomize <= 98) {
            xpArray.add(new Pair<>(xp, new LargeXp(_monsters.x +_monsters.getWidth() / 2 - xp.getWidth()/2, _monsters.y + _monsters.getHeight() / 2 - xp.getHeight()/2)));
        }
        else{
            spawnChest(_monsters);
        }
    }

    private void spawnChest(Rectangle _monsters) {
        Rectangle chest = new Rectangle();
        chest.width = 25;
        chest.height = 25;
        chest.x = _monsters.x + (float) monsImage.getWidth() / 2;
        chest.y = _monsters.y + (float) monsImage.getHeight() / 2;
        chestArray.add(new Pair<>(chest, new Chest(_monsters.x + (float) monsImage.getWidth() / 2, _monsters.y + (float) monsImage.getHeight() / 2)));
    }

    //Loop array skill that hero currently have
    private void useSkill(Skill s){
        s.skillEffect(mc, batch);
    }
}
