package com.mygdx.game;

public class SkillRegenHP extends Skill{
    public SkillRegenHP() {
        super.setName("Jett Heal Me Jett");
        super.setDescription("Regen HP for 1 Every Second");
        super.setValue(1);
    }
    @Override
    public void skillEffect(Hero theHero) {
        theHero.setHp((int)(theHero.getHp() + getValue()));
    }
}
