package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class EndScreen implements Screen {
    
    final F35Sim game;

    final int score;

    OrthographicCamera camera;

    public EndScreen(F35Sim game, int score){

        this.game = game;
        this.score = score;

        // creates and configures camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        
    }
    @Override
    public void render(float delta){

        ScreenUtils.clear(0, 0, 10, 1);
        game.batch.setProjectionMatrix(camera.combined);

        camera.update();

        game.batch.begin();
        game.font.draw(game.batch, "GAME OVER", 100, 150);
        game.font.draw(game.batch, "Final Score: " + score, 100, 100);
		game.font.draw(game.batch, "Tap anywhere to restart", 100, 50);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }
    
}
