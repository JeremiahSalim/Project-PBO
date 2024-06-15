package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class SkillRegenHP extends Skill{
    public SkillRegenHP() {
        super.setName("Jett Heal Me Jett");
        super.setDescription("Regen HP for 1 Every Second");
        super.setValue(1);
    }
    @Override
    public void skillEffect(Pair<Rectangle, Hero> mc, SpriteBatch batch) {
        if(mc.getValue().getHp() < mc.getValue().getMaxHp()) {
            mc.getValue().setHp((int) (mc.getValue().getHp() + getValue()));
        }
        else{
            mc.getValue().setHp(mc.getValue().getMaxHp());
        }
    }
}
