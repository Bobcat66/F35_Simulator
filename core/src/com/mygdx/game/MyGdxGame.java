package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MyGdxGame extends ApplicationAdapter {

	// Batch
	private SpriteBatch batch;

	// Camera
	private OrthographicCamera camera;

	// Textures
	private Texture img;
	private Texture F35Texture;
	private Texture bulletTexture;
	private Texture AMRAAMTexture;
	private Texture MIG21Texture;

	// Audio
	private Sound cannonSound;
	private Sound cannonEndSound;
	private Music backgroundMusic;

	// Entities
	private Actor F35;
	private Array<Projectile> bullets;
	private Array<Projectile> missiles;
	private Array<Actor> enemies;
	
	// Control Variables;
	private boolean cannonFiring = false;
	private boolean missileCooldown = false; // Whether missile is in cooldown

	// Timed events
	private Array<timedEvent> timedEvents;

	// Fonts
	private BitmapFont font;

	// Other variables
	private int score = 0;
	private int misses;

	@Override
	public void create () {

		// SpriteBatch
		batch = new SpriteBatch();

		// Load textures
		F35Texture = new Texture("F35_Sprite.png");
		bulletTexture = new Texture("bullet.png");
		AMRAAMTexture = new Texture("AMRAAM.png");
		MIG21Texture = new Texture("mig-21.png");
		img = new Texture("F-35A.jpg");

		// Load Audio
		cannonSound = Gdx.audio.newSound(Gdx.files.internal("Weapons/GAU8CannonLow.wav"));
		cannonEndSound = Gdx.audio.newSound(Gdx.files.internal("Weapons/GAU8End.wav"));
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Ride_of_the_valkyries.ogg")); //Ride of the valkyries is placeholder audio

		// Begins playing background music immediately
		//backgroundMusic.setLooping(true);
		//backgroundMusic.play();

		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// Load objects
		F35 = new Actor(60000,"blue");

		// Create arrays
		bullets = new Array<Projectile>(); //Player bullets
		missiles = new Array<Projectile>(); //Player missiles
		timedEvents = new Array<timedEvent>(); //timed events
		enemies = new Array<Actor>();

   		//F35.x = 800 / 2 - 100 / 2;
   		//F35.y = 20;
   		F35.width = 100;
   		F35.height = 100;

		// load font
		font = new BitmapFont(); // uses default font

		// adds mig spawner event
		timedEvents.add(new MiGSpawner());
	}

	public void spawnBullet(Vector3 position){
		// spawns bullet
		Projectile bullet = new Projectile(500,"blue");
		bullet.x = position.x;
		bullet.y = position.y + 40;
		bullet.width=2;
		bullet.height=5;
		bullets.add(bullet);
	}

	public void spawnBullet(Vector3 position, String team, Array<Projectile> projectileArray){
		// spawns bullet, works for all teams
		Projectile bullet = new Projectile(500,team);
		bullet.x = position.x;
		bullet.y = position.y;
		bullet.width=2;
		bullet.height=5;
		projectileArray.add(bullet);
	}

	public void spawnMig(){
		// spawns an enemy Mig-21
		Actor MiG21 = new Actor(10000, "red");
		MiG21.x = MathUtils.random(0,800-56);
		MiG21.y = 400;
		MiG21.width = 56;
		MiG21.height = 79;
		enemies.add(MiG21);
	}
	public void spawnMig(int xArg, int yArg){
		// spawns an enemy Mig-21 at the given coordinates
		Actor MiG21 = new Actor(10000, "red");
		MiG21.x = xArg;
		MiG21.y = yArg;
		MiG21.width = 56;
		MiG21.height = 79;
		enemies.add(MiG21);
	}


	public void spawnMissile(Vector3 position){
		// spawns missile
		Projectile missile = new Projectile(10000,"blue");
		missile.x = position.x;
		missile.y = position.y;
		missile.width=10;
		missile.height=30;
		missiles.add(missile);
	}

	public void spawnMissile(Vector3 position, String team, Array<Projectile> projectileArray){
		// spawns missile, works for all teams
		Projectile missile = new Projectile(10000,team);
		missile.x = position.x;
		missile.y = position.y;
		missile.width=10;
		missile.height=30;
		projectileArray.add(missile);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 10, 1);
		batch.setProjectionMatrix(camera.combined);

		camera.update();

		batch.begin();

		// converts mouse coordinates to camera coordinates
		Vector3 mousePos = new Vector3();
		mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mousePos); // projects mousePos vector to camera's coordinate grid
		
		// handles bullet sprite (DEPRECATED)
		/*
		if (cannonFiring){
			for (int i = (int) mousePos.y; i < 480; i += 98){
				// bullet sprite is 98 pixels tall
				batch.draw(bulletsTexture, mousePos.x-1, i + (bulletAnimationCounter/5) );
			}
			if (bulletAnimationCounter < 15){
				bulletAnimationCounter++;
			} else {
				bulletAnimationCounter = 0;
			}
		}
		*/
		// spawns bullets
		if (cannonFiring){
			spawnBullet(mousePos);
		}

		// draws bullets
		for(Projectile bullet : bullets) {
			//System.out.println(bullet.y); //DEBUGGING
			batch.draw(bulletTexture, bullet.x, bullet.y);
		}

		// draws missiles
		for(Projectile missile : missiles) {
			batch.draw(AMRAAMTexture, missile.x, missile.y);
		}
		
		// draws enemies
		for (Actor enemy : enemies) {
			batch.draw(MIG21Texture, enemy.x, enemy.y);
		}

		// updates sprite position
		F35.setPosition(mousePos.x-50, mousePos.y - 50);
		batch.draw(F35Texture, F35.x, F35.y);

		// draws score
		font.draw(batch, "Score: " + score, 0,480);

		batch.end();

		// moves bullets, and removes them if they are no longer on the screen, and checks if they collide with anything
		
		for (Iterator<Projectile> iter = bullets.iterator(); iter.hasNext(); ){
			Projectile bullet = iter.next();
			bullet.y += 500 * Gdx.graphics.getDeltaTime(); // Bullet speed tied to game timer. Speed = 500 pixels per second
			//bullet.y += 10; // Bullet speed tied to framerate. Speed = 10 pixels per frame
			if (bullet.y >= 480){
				iter.remove();
				continue;
			}
			/*
			if (bullet.overlaps(F35)){
				F35.hit(bullet);
				// Sometimes it throws ArrayIndexOutOfBoundsException, IDK why
				// Maybe it's trying to remove the same bullet twice when the F-35 is outside of the screen
				// Gonna try continuing after bullets leave the screen
				// It works for now
				// Also, this whole thing is just to test collisions, and will probably be commented out in the final version
				// Hopefully this solution doesn't cause any headaches down the line
				iter.remove();
				//System.out.println("Collision successful"); // Debugging code
			}
			*/

			for(Actor enemy : enemies){
				if (bullet.overlaps(enemy)) {
					enemy.hit(bullet);
					iter.remove();
					break;
				}
			}
		}

		// moves missiles, + collision detection
		for (Iterator<Projectile> iter = missiles.iterator(); iter.hasNext(); ){
			Projectile missile = iter.next();
			missile.y += 300 * Gdx.graphics.getDeltaTime(); //Missile speed: 300 px per second
			if (missile.y >= 480){
				iter.remove();
				continue;
			}

			for(Actor enemy : enemies){
				if (missile.overlaps(enemy)) {
					enemy.hit(missile);
					iter.remove();
					break;
				}
			}
		}

		
		// Handles mouseclick
		
		//left click fires cannon
		if (Gdx.input.isButtonPressed(0)){
			if (!cannonFiring){
				long x = cannonSound.play();
				cannonSound.setLooping(x, true);
				
				cannonFiring = true;
			}
			
		}

		// right click fires missile
		if (Gdx.input.isButtonPressed(1)){
			if (!missileCooldown){
				//System.out.println(this.missileCooldown);
				spawnMissile(mousePos);
				timedEvents.add(new missileCooldown());
				//System.out.println(this.missileCooldown);
			}

		}

		// stops cannon if mouse is not left-clicked
		if (!Gdx.input.isButtonPressed(0) & cannonFiring){
			cannonSound.stop();
			cannonEndSound.play();
			cannonFiring = false;
		}

		//Handles timedEvents
		for (Iterator<timedEvent> iter = timedEvents.iterator(); iter.hasNext(); ) {
			timedEvent event = iter.next();

			// removes killed events
			if(!event.getState()){
				iter.remove();
				continue;
			}

			event.counter += Gdx.graphics.getDeltaTime();
			// Debugging code
			//System.out.println(Gdx.graphics.getDeltaTime());
			//System.out.println(event.counter);
			//System.out.println(event.delay);
			event.execute();
		}

		// Handles enemies
		for (Iterator<Actor> iter = enemies.iterator(); iter.hasNext(); ){
			Actor enemy = iter.next();
			//enemy.y -= 200 * Gdx.graphics.getDeltaTime();
			if (enemy.y <= 0){
				iter.remove();
				continue;
			}
			if (enemy.health <= 0){
				iter.remove();
				score++;
			}
		}

	}
	
	@Override
	public void dispose () {
		// Garbage collection
		img.dispose();
		F35Texture.dispose();
		bulletTexture.dispose();
		AMRAAMTexture.dispose();
		cannonSound.dispose();
		cannonEndSound.dispose();
		backgroundMusic.dispose();
		MIG21Texture.dispose();
		batch.dispose();
	}

	// Timed event classes
	private class missileCooldown extends timedEvent {

		void event(){
			missileCooldown = false;
			//System.out.println("MissileCooldown event triggered"); //Debugging code
			kill();
		}

		public missileCooldown(){
			// sets missilecooldown to true when event is created
			missileCooldown = true;
			delay = 1;
		}
	}

	private class MiGSpawner extends timedEvent {
		
		void event(){
			spawnMig1(400);
			counter = 0; // resets counter
		}

		public MiGSpawner(){
			delay = 10;
		}
	}

	// enemy spawning methods

	private void spawnMig1(int yArg){
		// spawns a line of  7 migs at the given y value
		for(int i = 1; i < 8; i++){
			spawnMig(100*i,yArg);
		}
	}
}


abstract class timedEvent {
    // Timed event

    public float counter = 0; // event internal counter
    public float delay; // event delay (in seconds)
	private boolean alive = true; 

	// behavior when delay is reached
    abstract void event();

	// check if the event should fire
	public void execute(){
		if (counter >= delay) event();
	}

	// signals that the timedEvent should be deleted
	public void kill(){
		alive = false;
	}

	public boolean getState(){
		// returns the timed event's state
		return alive;
	}
}

abstract class triggeredEvent {
	// Triggered event

	private boolean alive = true;
	// event trigger condition
	abstract boolean condition();

	// behavior when condition is fulfilled
	abstract void event();

	public void execute(){
		if (condition()) event();
	}

	// signals that the event should be deleted
	public void kill(){
		alive = false;
	}
	
	public boolean getState(){
		// returns the timed event's state
		return alive;
	}
}

