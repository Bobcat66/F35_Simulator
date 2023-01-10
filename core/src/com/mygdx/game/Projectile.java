package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

public class Projectile extends Rectangle {
    // representation of projectiles
    public int damage;
    public String team; // team that fired it, to prevent friendly fire
    public Vector2 velocity; //the vector that describes this object's velocity
    public Texture texture; // projectile texture

    public Projectile(int damageArg, String teamArg){
        super(); // calls constructor of Rectangle class
        damage = damageArg;
        team = teamArg;
    }

    public Projectile(int damage, String team, Vector2 velVector){
        super(); // calls constructor of Rectangle class
        this.damage = damage;
        this.team = team;
        this.velocity = velVector;

    }

    public void move(float deltaTime){
        // handles movement, delta is the time since the last movement
        this.x += velocity.x * deltaTime;
        this.y += velocity.y * deltaTime;

    }

    public Projectile(){
        super();
    }
}

