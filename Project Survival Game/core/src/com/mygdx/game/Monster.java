package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Monster extends Entity implements EntitiyAction{

    private Animation<TextureRegion> monsAnimRight;
    private Animation<TextureRegion> monsAnimLeft;
    private Texture rawMonsRight = new Texture(Gdx.files.internal("monster/FloatingEye.png"));
    private Texture rawMonsLeft = new Texture(Gdx.files.internal("monster/FloatingEyeFlip.png"));
    private float stateTime;

    public void create(){
        TextureRegion[][] monsFrames = TextureRegion.split(rawMonsRight, rawMonsRight.getWidth()/4, rawMonsRight.getHeight());
        TextureRegion[] mons = new TextureRegion[4];
        int idx = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                mons[idx] = monsFrames[i][j];
                idx++;
            }
        }
        monsAnimRight = new Animation<>(0.15f, mons);

        monsFrames = TextureRegion.split(rawMonsLeft, rawMonsLeft.getWidth()/4, rawMonsLeft.getHeight());
        mons = new TextureRegion[4];
        idx = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 3; j >= 0; j--) {
                mons[idx] = monsFrames[i][j];
                idx++;
            }
        }
        monsAnimLeft = new Animation<>(0.15f, mons);
    }

    public Monster() {
        super(100, 10);
        create();
    }

    @Override
    public void isAttacked(int damage) {
        this.setHp(this.getHp() - damage);
    }

    public Animation<TextureRegion> getMonsAnimRight() {
        return monsAnimRight;
    }

    public void setMonsAnimRight(Animation<TextureRegion> monsAnim) {
        this.monsAnimRight = monsAnim;
    }

    public Texture getRawMons() {
        return rawMonsRight;
    }

    public void setRawMons(Texture rawMons) {
        this.rawMonsRight = rawMons;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public Animation<TextureRegion> getMonsAnimLeft() {
        return monsAnimLeft;
    }

    public void setMonsAnimLeft(Animation<TextureRegion> monsAnimLeft) {
        this.monsAnimLeft = monsAnimLeft;
    }
}
