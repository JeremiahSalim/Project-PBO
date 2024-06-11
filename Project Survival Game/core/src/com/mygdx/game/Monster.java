package com.mygdx.game;

public class Monster extends Entity implements EntitiyAction{
    public Monster() {
        super(100, 10);
    }

    @Override
    public void isAttacked(int damage) {
        this.setHp(this.getHp() - damage);
    }
}
