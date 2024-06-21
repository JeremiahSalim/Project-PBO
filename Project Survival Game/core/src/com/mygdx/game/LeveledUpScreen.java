package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class LeveledUpScreen implements Screen {

    MyGdxGame game;
    SpriteBatch batch;
    OrthographicCamera camera;
    ScreenViewport viewport;
    Stage stage;
    Table skillTable;
    Skin skin;

    ArrayList<Skill> list3Skill;

    Pair<Rectangle, Hero> mc;

    public LeveledUpScreen(final MyGdxGame game, SpriteBatch batch, ArrayList<Skill> list3Skill, Pair<Rectangle, Hero> mc) {
        this.game = game;
        this.batch = batch;
        this.list3Skill = list3Skill;
        this.mc = mc;
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        skillTable = new Table();
        skillTable.setFillParent(true);

        addButtonBasedOnSkill();

        stage.addActor(skillTable);
    }

    private void addActiveSkillButton(String name, Skill s){
        TextButton.TextButtonStyle button = skin.get(TextButton.TextButtonStyle.class);
        TextButton textButton = new TextButton(name, button);
        textButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mc.getValue().addSkill(s);
                GameScreen.leveledUp = false;
            }
        });
        textButton.getLabel().setFontScale(0.5f,0.5f);
        skillTable.add(textButton).pad(10).size(750, 130);
        skillTable.row();
    }

    private void addInstantSkillButton(String name, Skill s){
        TextButton.TextButtonStyle button = skin.get(TextButton.TextButtonStyle.class);
        TextButton textButton = new TextButton(name, button);
        textButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                s.skillEffect(mc, batch);
                GameScreen.leveledUp = false;
            }
        });
        textButton.getLabel().setFontScale(0.5f,0.5f);
        skillTable.add(textButton).pad(10).size(750, 130);
        skillTable.row();
    }

    private void addButtonBasedOnSkill(){
        for (Skill s:list3Skill) {
            if(s instanceof SkillElectricField || s instanceof SkillSpirit || s instanceof  SkillRegenHP){
                addActiveSkillButton(s.getName()+'\n'+s.getDescription(), s);
            }
            else{
                addInstantSkillButton(s.getName()+'\n'+s.getDescription(), s);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
