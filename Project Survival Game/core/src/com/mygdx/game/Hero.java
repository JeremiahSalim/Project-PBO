package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Hero extends Entity{
    public Hero() {
        super(1000, 100);
    }


    @Override
    public void isAttacked(Entity entity){
        this.setHp(this.getHp() - entity.getAtk());
    }
}
