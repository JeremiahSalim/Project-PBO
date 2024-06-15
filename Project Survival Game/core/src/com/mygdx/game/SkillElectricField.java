package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class SkillElectricField extends Skill{
    private Circle area;
    private Animation<TextureRegion> electricField;
    private float statetime;

    private Texture coba;

    public SkillElectricField(){
        super.setName("Brrt Brrt");
        super.setDescription("Create Electric Field Around Player and Damage Monster per Second");
        super.setValue(100);
        create();
    }

    public Circle getArea() {
        return area;
    }

    public void setArea(Circle area) {
        this.area = area;
    }

    public Animation<TextureRegion> getElectricField() {
        return electricField;
    }

    public void setElectricField(Animation<TextureRegion> electricField) {
        this.electricField = electricField;
    }

    public float getStatetime() {
        return statetime;
    }

    public void setStatetime(float statetime) {
        this.statetime = statetime;
    }

    public void create(){
        area = new Circle();
        area.radius = 200;
        Texture raw = new Texture("skill/electric.png");
        coba = new Texture("skill/electric.png");
        TextureRegion[][] electricFrames = TextureRegion.split(raw, raw.getWidth() / 5, raw.getHeight());
        TextureRegion[] electric = new TextureRegion[5];
        int idx = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 5; j++) {
                electric[idx] = electricFrames[i][j];
                idx++;
            }
        }

        electricField = new Animation<>(0.1f, electric);

        statetime = 0f;

    }

    @Override
    public void skillEffect(Pair<Rectangle, Hero> mc, SpriteBatch batch) {
        area.x = mc.getKey().getX();
        area.y = mc.getKey().getY();
        batch.begin();
        statetime += Gdx.graphics.getDeltaTime();
        TextureRegion currentState = electricField.getKeyFrame(statetime, true);
        batch.draw(currentState,mc.getKey().x - area.radius + mc.getKey().getWidth()/2 , mc.getKey().y - area.radius + mc.getKey().getHeight()/2 , area.radius*2, area.radius*2);
        System.out.println(statetime);
        batch.end();
    }
}
