package com.mygdx.game;

public class SmallXp extends Xp implements Collectible{
    public SmallXp(float x, float y) {
        super(10, x, y);
    }

    @Override
    public void isCollected(Hero theHero) {
        theHero.calculateXp(this.getAmount());
    }
}
