package com.mygdx.game;

public class LargeXp extends Xp implements Collectible{
    public LargeXp() {
        super(50);
    }

    @Override
    public void isCollected(Hero theHero) {
        theHero.calculateXp(this.getAmount());
    }
}
