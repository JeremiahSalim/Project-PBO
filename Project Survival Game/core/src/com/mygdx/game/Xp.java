package com.mygdx.game;

public class Xp {
    private int amount;
    private float x;
    private float y;

    public Xp(int amount, float x, float y) {
        this.amount = amount;
        this.x = x;
        this.y = y;
    }

    public int getAmount() {
        return amount;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
