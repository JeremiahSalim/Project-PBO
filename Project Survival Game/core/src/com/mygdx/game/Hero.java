package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Hero extends Entity implements  EntitiyAction{
    int maxHp;
    private int xp;
    int maxXp;
    private int level;
    public Hero() {
        super(1000, 100);
        maxHp = 1000;
        xp = 0;
        maxXp = 1000;
        level = 1;
    }

    @Override
    public void isAttacked(int damage) {
        this.setHp(this.getHp() - damage);
    }

    public void calculateXp(int ammountXp) {
        this.setXp(this.getXp() + ammountXp);
        if(this.getXp() >= this.maxXp){ //leveling up
            level++; //naikin lvlnya
            this.setXp(this.getXp()-maxXp); // buat Xp nya ulang dari 0 atau berapapun kalau ada sisanya
            this.maxXp += 100 * this.level;
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }
}
