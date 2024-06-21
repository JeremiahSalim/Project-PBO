package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Chest {
    private float x;
    private float y;
    LeveledUpScreen screen;
    ArrayList<Skill> skills = new ArrayList<Skill>(){{
        add(new SkillRegenHP());
        add(new SkillUpgradeHP());
        add(new SkillUpgradeATK());
        add(new SkillElectricField());
        add(new SkillSpirit());
    }};

    ArrayList<Skill> list3Skill = new ArrayList<>();

    public Chest(float x, float y) {
        this.x = x;
        this.y = y;
        list3Skill = randomizer(skills, 3);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private ArrayList<Skill> randomizer (ArrayList<Skill> skills, int number){
        ArrayList<Skill> random = new ArrayList<>();
        ArrayList<Skill> copySkills = new ArrayList<>(skills);
        Collections.shuffle(copySkills);
        for (int i = 0; i < number; i++) {
            random.add(copySkills.get(i));
        }

        return random;
    }

    public ArrayList<Skill> getList3Skill() {
        return list3Skill;
    }
}
