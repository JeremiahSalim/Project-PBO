package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import com.badlogic.gdx.math.Rectangle;

public class Skill {
    private String name;
    private String description;
    private float value; //this is used for anything

    public Skill(String name, String description, float value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public Skill() {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void skillEffect(Pair<Rectangle, Hero> mc, SpriteBatch batch){

    }
}
