package com.mygdx.game;

public class LargeXp extends Xp implements Collectible{
    public LargeXp(float x, float y) {
        super(50, x, y);
    }
    @Override
    public void isCollected(Hero theHero) {
        theHero.calculateXp(this.getAmount());
    }
}
