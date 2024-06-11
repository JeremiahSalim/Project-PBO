package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Hero extends Entity implements  EntitiyAction{
    public Hero() {
        super(1000, 100);
    }


    @Override
    public void isAttacked(int damage) {
        this.setHp(this.getHp() - damage);
    }
}
