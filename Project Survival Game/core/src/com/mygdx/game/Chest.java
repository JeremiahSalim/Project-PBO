package com.mygdx.game;

import java.util.ArrayList;

public class Chest {
    private float x;
    private float y;
    ArrayList<Skill> skills = new ArrayList<Skill>(){{
        add(new SkillRegenHP());
        add(new SkillUpgradeHP());
        add(new SkillUpgradeATK());
        add(new SkillElectricField());
        add(new SkillSpirit());
    }};

    public Chest(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
