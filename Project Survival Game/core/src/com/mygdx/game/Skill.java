package com.mygdx.game;

public class Skill {
    private String name;
    private String description;
    private float value; //this is used for anything
    private float cooldown;
    private float duration;
    private boolean isActive = false;

    public Skill(String name, String description, float value, float cooldown, float duration) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.cooldown = cooldown;
        this.duration = duration;
    }

    public Skill(String name, String description, float value) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.cooldown = cooldown;
        this.duration = duration;
    }

    public Skill() {
        this.name = name;
        this.description = description;
        this.value = value;
        this.cooldown = cooldown;
        this.duration = duration;
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

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float skillEffect(float amount){
        return 0;
    }

    public void skillEffect(Hero theHero){

    }
}
