package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class F35Sim extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    GameScreen gamescreen;

    public void create(){
        batch = new SpriteBatch();
        font = new BitmapFont();

        this.setScreen(new MainMenuScreen(this));
    }

    public void render(){
        super.render();
    }

    public void dispose(){
        System.out.println("Assets disposed"); // debugging code
        this.gamescreen.dispose();
        this.font.dispose();
        this.batch.dispose();
    }
    
}
