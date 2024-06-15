package com.mygdx.game;

import java.util.ArrayList;

public class Hero extends Entity implements  EntitiyAction{
    private int maxHp;
    private int xp;
    private int maxXp;
    private int level;
    ArrayList<Skill> skills;
    public Hero() {
        super(1000, 100);
        maxHp = 1000;
        xp = 0;
        maxXp = 1000;
        level = 1;
        skills = new ArrayList<Skill>(){{
            add(new SkillRegenHP());
        }};;
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
            this.maxXp += 100 * this.level-1;
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

    public int getLevel() {
        return level;
    }

    public int getMaxXp() {
        return maxXp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setMaxXp(int maxXp) {
        this.maxXp = maxXp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill _skill){
        skills.add(_skill);
    }
}
