package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class SkillUpgradeHP extends Skill{
    public SkillUpgradeHP(){
        super.setName("Gym Bro Pass");
        super.setDescription("Increase HP by 20%");
        super.setValue(0.2f);
    }
    @Override
    public void skillEffect(Pair<Rectangle, Hero> mc, SpriteBatch batch) {
        mc.getValue().setMaxHp((int)(mc.getValue().getMaxHp() + mc.getValue().getMaxHp()*getValue()));
    }
}
