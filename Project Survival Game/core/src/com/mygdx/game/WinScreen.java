package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class WinScreen implements Screen {
    MyGdxGame game;
    SpriteBatch batch;
    OrthographicCamera camera;
    Texture bgImage;
    ScreenViewport viewport;
    Texture subTitle;
    Music bgMusic;
    private Animation<TextureRegion> title;
    float stateTime;

    public WinScreen(final MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false ,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        batch = new SpriteBatch();
        Texture rawTitle = new Texture("img/youWin.png");
        TextureRegion[][] titleFrames = TextureRegion.split(rawTitle, rawTitle.getWidth(), rawTitle.getHeight()/12);
        TextureRegion[] titles = new TextureRegion[5];
        int idx = 0;
        for (int i = 1; i < 6; i++) {
            titles[idx] = titleFrames[i][0];
            idx++;
        }
        title = new Animation<>(.13f, titles);
        stateTime = 0f;
        bgImage = new Texture("bg/winBg.jpg");
        subTitle = new Texture("img/subtitle.png");
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/winMusic.mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f);
        bgMusic.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(1, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bgImage,0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentState = title.getKeyFrame(stateTime, true);
        batch.draw(currentState, (float) Gdx.graphics.getWidth() /2 - (float) currentState.getRegionWidth()*0.75f, (float) Gdx.graphics.getHeight() /2 - currentState.getRegionHeight()*0.75f, (float) 576*1.5f, 43.4f*1.5f);        batch.draw(subTitle, (float) Gdx.graphics.getWidth() /2 -1097*0.2f , (float) Gdx.graphics.getHeight() /3 - 176*0.2f, 1097*0.4f, 176*0.4f);
        batch.end();

        if (Gdx.input.isTouched()) {
            bgMusic.stop();
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int i, int i1) {

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
