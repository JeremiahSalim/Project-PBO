package com.mygdx.game;

public class SmallXp extends Xp implements Collectible{
    public SmallXp() {
        super(10);
    }

    @Override
    public void isCollected(Hero theHero) {
        theHero.calculateXp(this.getAmount());
    }
}
