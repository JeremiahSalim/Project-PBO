package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {
    MyGdxGame game;
    SpriteBatch batch;
    OrthographicCamera camera;
    BitmapFont font;
    private Animation<TextureRegion> title;
    float stateTime;


    public MenuScreen(final MyGdxGame game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false ,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.font = new BitmapFont();
        batch = new SpriteBatch();
        Texture rawTitle = new Texture("img/GameTitle.png");
        TextureRegion[][] titleFrames = TextureRegion.split(rawTitle, rawTitle.getWidth(), rawTitle.getHeight()/10);
        TextureRegion[] titles = new TextureRegion[5];
        int idx = 0;
        for (int i = 1; i < 6; i++) {
            titles[idx] = titleFrames[i][0];
            idx++;
        }
        title = new Animation<>(.13f, titles);
        stateTime = 0f;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentState = title.getKeyFrame(stateTime, true);
        batch.draw(currentState, (float) Gdx.graphics.getWidth() /2 - (float) 563, (float) Gdx.graphics.getHeight() /2 - 44.3f, (float) 563*2, 44.3f*2);
        font.draw(batch, "Tap anywhere to start", (float) Gdx.graphics.getWidth() /2, (float) Gdx.graphics.getHeight() /3 );
        batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

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
