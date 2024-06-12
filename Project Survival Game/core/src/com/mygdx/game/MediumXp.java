package com.mygdx.game;

public class MediumXp extends Xp implements Collectible{
    public MediumXp(float x, float y) {
        super(25, x, y);
    }
    @Override
    public void isCollected(Hero theHero) {
        theHero.calculateXp(this.getAmount());
    }
}
