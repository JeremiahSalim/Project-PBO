package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class SkillSpirit extends Skill{
    private Rectangle spiritRectangle;
    private Texture spirit;
    public SkillSpirit() {
        super.setName("Surprise Surprise Mother Father");
        super.setDescription("Create a companion that will shoot bullet like the hero\nIf already have this skill, increase dmg by 10%");
        super.setValue(50);
        create();
    }

    public void create(){
        spiritRectangle = new Rectangle();
        spirit = new Texture("skill/spirit.png");
    }

    @Override
    public void skillEffect(Pair<Rectangle, Hero> mc, SpriteBatch batch) {
        spiritRectangle.x = mc.getKey().getX() - 60;
        spiritRectangle.y = mc.getKey().getY() + 60;
        batch.begin();
        batch.draw(spirit,spiritRectangle.x, spiritRectangle.y, 40, 30);
        batch.end();
    }

    @Override
    public void upgradeSkill() {
        setValue(getValue()*110/100);
    }
}
