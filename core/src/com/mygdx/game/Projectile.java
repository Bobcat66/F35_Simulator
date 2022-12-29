package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class Projectile extends Rectangle {
    // representation of projectiles
    public int damage;
    public String team; // team that fired it, to prevent friendly fire

    public Projectile(int damageArg, String teamArg){
        super(); // calls constructor of Rectangle class
        damage = damageArg;
        team = teamArg;
    }

    public Projectile(){
        super();
    }
}

