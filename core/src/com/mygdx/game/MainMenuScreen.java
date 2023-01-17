package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen {
    
    final F35Sim game; // represents the game
    OrthographicCamera camera;

    public MainMenuScreen(final F35Sim gameArg){
        
        this.game = gameArg;

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
        game.font.draw(game.batch, "Welcome to the 100% Accurate F35 Combat Simulator, Now with Over 5 Levels!", 100, 250);
        game.font.draw(game.batch, "Instructions: move you mouse around to move the plane",100,200);
        game.font.draw(game.batch, "left click to fire the cannon, right click to fire the missile",100,150);
		game.font.draw(game.batch, "Tap anywhere to begin", 100, 100);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
}
