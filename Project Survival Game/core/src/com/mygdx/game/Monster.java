package com.mygdx.game;

public class Monster extends Entity implements EntitiyAction{
    public Monster() {
        super(1500000, 100);
    }

    @Override
    public void isAttacked(int damage) {
        this.setHp(this.getHp() - damage);
    }
}
