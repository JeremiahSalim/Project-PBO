package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Spawner {

    public Spawner() {
    }

    public void spawnMonster(OrthographicCamera camera, Array<Pair<Rectangle, Monster>> monsArray) {
        Rectangle monster = new Rectangle();
        monster.width = 64;
        monster.height = 64;
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
        //lastSpawnTime = TimeUtils.nanoTime();
    }

    public void spawnHeroAtk(OrthographicCamera camera, Pair<Rectangle, Hero> mc, Array<Pair<Rectangle, Bullet>> bulletArray) {
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
        //lastAttackTime = TimeUtils.nanoTime();
    }

    public void spawnSpiritAtk(Pair<Rectangle,Monster> monster, Pair<Rectangle, Hero> mc, Array<Pair<Rectangle, Bullet>> spiritBulletArray) {
        Rectangle spiritAtk = new Rectangle();
        spiritAtk.x = mc.getKey().getX() - 60;
        spiritAtk.y = mc.getKey().getY() + 60;
        spiritAtk.width = 1;
        spiritAtk.height = 1;

        Vector2 monsPos = new Vector2(monster.getKey().getPosition(new Vector2()));
        Vector2 spiritPos = new Vector2(mc.getKey().getPosition(new Vector2()));
        spiritPos.x -= 60;
        spiritPos.y += 60;
        Vector2 bulletDirection = new Vector2((monsPos .x + monster.getKey().width/2) - (spiritPos.x ), (monsPos .y+ monster.getKey().width/2) - (spiritPos.y ));
        spiritBulletArray.add(new Pair<>(spiritAtk, new Bullet(bulletDirection)));
        //lastSpiritTime = TimeUtils.nanoTime();
    }

    public void spawnCollectible(Rectangle _monsters, Array<Pair<Rectangle, Xp>> xpArray, Array<Pair<Rectangle, Chest>> chestArray) {
        Rectangle xp = new Rectangle();
        xp.width = 25;
        xp.height = 25;
        xp.x = _monsters.x + _monsters.getWidth() / 2;
        xp.y = _monsters.y + _monsters.getHeight() / 2;
        int randomize = MathUtils.random(0, 1000);
        if (randomize >= 0 && randomize <= 499) {
            xpArray.add(new Pair<>(xp, new SmallXp(_monsters.x + _monsters.getWidth() / 2 - xp.getWidth()/2, _monsters.y + _monsters.getHeight() / 2 - xp.getHeight()/2)));
        } else if (randomize >= 50 && randomize <= 799) {
            xpArray.add(new Pair<>(xp, new MediumXp(_monsters.x + _monsters.getWidth() / 2 - xp.getWidth()/2, _monsters.y + _monsters.getHeight() / 2 - xp.getHeight()/2)));
        } else if (randomize >= 80 && randomize <= 998) {
            xpArray.add(new Pair<>(xp, new LargeXp(_monsters.x +_monsters.getWidth() / 2 - xp.getWidth()/2, _monsters.y + _monsters.getHeight() / 2 - xp.getHeight()/2)));
        }
        else{
            spawnChest(_monsters, chestArray);
        }
    }

    private void spawnChest(Rectangle _monsters, Array<Pair<Rectangle, Chest>> chestArray) {
        Rectangle chest = new Rectangle();
        chest.width = 25;
        chest.height = 25;
        chest.x = _monsters.x + _monsters.width/2;
        chest.y = _monsters.y + _monsters.height/2;
        chestArray.add(new Pair<>(chest, new Chest(_monsters.x +  _monsters.width/2, _monsters.y + _monsters.height/2)));
    }

}
