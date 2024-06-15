package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class SkillUpgradeATK extends Skill{
    public SkillUpgradeATK() {
        super.setName("Training Camp");
        super.setDescription("Increase Attack by 10%");
        super.setValue(0.1f);
    }

    @Override
    public void skillEffect(Pair<Rectangle, Hero> mc, SpriteBatch batch) {
        mc.getValue().setAtk((int)(mc.getValue().getAtk() + mc.getValue().getAtk()*getValue()));
    }
}
