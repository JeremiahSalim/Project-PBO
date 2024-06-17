package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Bullet {
    Vector2 bulletDirection;
    private float stateTime;

    public Bullet(Vector2 bulletDirection) {
        this.bulletDirection = bulletDirection;
        this.bulletDirection = bulletDirection.nor();
    }

    public Vector2 getBulletDirection() {
        return bulletDirection;
    }

    public void setBulletDirection(Vector2 bulletDirection) {
        this.bulletDirection = bulletDirection;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }
}
