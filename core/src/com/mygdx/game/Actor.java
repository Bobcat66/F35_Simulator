package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

public class Actor extends Rectangle {
    // representation of actors
    public int health;
    public String team; // team, to prevent friendly fire
    public String name; // to identify the actor
    public Vector2 velocity = new Vector2(0,0);
    public Texture texture;

    public Actor(int healthArg, String nameArg, String teamArg){
        super(); // calls constructor of Rectangle class
        health = healthArg;
        name = nameArg;
        team = teamArg;
    }

    public Actor(){
        super();
    }

    public void move(float deltaTime){
        // handles movement, delta is the time since the last movement
        this.x += velocity.x * deltaTime;
        this.y += velocity.y * deltaTime;
    }

    public void hit(Projectile projectile){
        if (!projectile.team.equals(this.team)){
            health -= projectile.damage;
        }
    }
}