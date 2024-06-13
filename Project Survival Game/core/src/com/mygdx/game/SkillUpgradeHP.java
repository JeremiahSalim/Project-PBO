package com.mygdx.game;

public class SkillUpgradeHP extends Skill{
    public SkillUpgradeHP(){
        super.setName("Gym Bro Pass");
        super.setDescription("Increase HP by 20%");
        super.setValue(0.2f);
    }
    @Override
    public void skillEffect(Hero theHero) {
        theHero.setMaxHp((int)(theHero.getMaxHp() + theHero.getMaxHp()*getValue()));
    }
}
