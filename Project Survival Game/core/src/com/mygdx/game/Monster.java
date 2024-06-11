package com.mygdx.game;

public class Monster extends Entity implements EntitiyAction{
    public Monster() {
<<<<<<< HEAD
        super(150, 100);
=======
        super(100, 10);
>>>>>>> 5fed4f1193e91c48123fb4fc45d4d0fa52d6a474
    }

    @Override
    public void isAttacked(int damage) {
        this.setHp(this.getHp() - damage);
    }
}
