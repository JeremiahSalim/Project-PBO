package com.mygdx.game;

public class MediumXp extends Xp implements Collectible{
    public MediumXp() {
        super(25);
    }

    @Override
    public void isCollected(Hero theHero) {
        theHero.calculateXp(this.getAmount());
    }
}
