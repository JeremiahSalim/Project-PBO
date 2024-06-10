package com.mygdx.game;

public class Monster extends Entity{
    public Monster() {
        super(150, 10);
    }

    @Override
    public void isAttacked(Entity entity){
        this.setHp(this.getHp() - entity.getAtk());
    }
}
