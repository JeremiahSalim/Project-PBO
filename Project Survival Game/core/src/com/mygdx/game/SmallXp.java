package com.mygdx.game;

public class SmallXp extends Xp implements Collectible{
    public SmallXp(int amount) {
        super(10);
    }

    @Override
    public void isCollected(Hero theHero) {
        theHero.calculateXp(this.getAmount());
    }
}
