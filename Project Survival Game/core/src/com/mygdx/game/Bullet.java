package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Bullet {
    Vector2 bulletDirection;

    public Bullet(Vector2 bulletDirection) {
        this.bulletDirection = bulletDirection;
    }

    public Vector2 getBulletDirection() {
        return bulletDirection;
    }

    public void setBulletDirection(Vector2 bulletDirection) {
        this.bulletDirection = bulletDirection;
    }
}
