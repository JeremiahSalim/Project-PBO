package com.mygdx.game;

abstract class Entity {
    private int hp;
    private int atk;

    public Entity(int hp, int atk) {
        this.hp = hp;
        this.atk = atk;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public boolean isLive() {
        if(this.hp <= 0){
            return false;
        }
        else{
            return true;
        }
    }
}
