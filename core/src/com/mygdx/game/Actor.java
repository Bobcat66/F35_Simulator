package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class Actor extends Rectangle {
    // representation of projectiles
    public int health;
    public String team; // team, to prevent friendly fire

    public Actor(int healthArg, String teamArg){
        super(); // calls constructor of Rectangle class
        health = healthArg;
        team = teamArg;
    }

    public Actor(){
        super();
    }

    public void hit(Projectile projectile){
        if (!projectile.team.equals(this.team)){
            health -= projectile.damage;
        }
    }
}