package com.mygdx.game;

public class SkillUpgradeATK extends Skill{
    public SkillUpgradeATK() {
        super.setName("Training Camp");
        super.setDescription("Increase Attack by 10%");
        super.setValue(0.1f);
    }

    @Override
    public void skillEffect(Hero theHero) {
        theHero.setAtk((int)(theHero.getAtk() + theHero.getAtk()*getValue()));
    }
}
