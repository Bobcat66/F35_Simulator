package com.mygdx.game;

import java.util.Iterator;
import java.util.function.BooleanSupplier;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {

	// Game
	final F35Sim game;

	// Camera
	OrthographicCamera camera;

	// Textures
	Texture img;
	Texture F35Texture;
	Texture bulletTexture;
	Texture AMRAAMTexture;
	Texture MIG21Texture;
	Texture SpreyTexture;
	Texture AngrySpreyTexture;
	Texture BAMRAAMTexture;

	// Audio
	Sound cannonSound;
	Sound cannonEndSound;
	Music backgroundMusic;

	// Entities
	Actor F35;
	Array<Actor> actors;
	Array<Projectile> bullets;
	Array<Projectile> missiles;
	Array<Actor> enemies;
	Array<Projectile> enemyMissiles;
	Array<Projectile> projectiles;

	// Control Variables;
	boolean cannonFiring = false;
	boolean missileCooldown = false; // Whether missile is in cooldown

	// events
	Array<timedEvent> timedEvents;
	Array<triggeredEvent> trigEvents;
	Array<constantEvent> constEvents;


	// Other variables
	int score = 0;
	int misses;
	int MiGCount = 0;

	public GameScreen(final F35Sim game) {

		// configures game
		this.game = game;
        this.game.gamescreen = this; // passes reference of itself to the F35Sim object
        
		// Load textures
		F35Texture = new Texture("F35_Sprite.png");
		bulletTexture = new Texture("bullet.png");
		AMRAAMTexture = new Texture("AMRAAM.png");
		BAMRAAMTexture = new Texture("BAMRAAM.png");
		MIG21Texture = new Texture("mig-21.png");
		img = new Texture("F-35A.jpg");
		SpreyTexture = new Texture("pierreSpret.jpg");
		AngrySpreyTexture = new Texture("pierreSpretAngry.jpg");

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

		// Create F35
		F35 = new Actor();
		F35.health = 60000;
		F35.team = "blue";
		F35.texture = F35Texture;
		F35.width = 76;
		F35.height = 82;

		// Create arrays
		bullets = new Array<Projectile>(); //Player bullets
		missiles = new Array<Projectile>(); //Player missiles
		projectiles = new Array<Projectile>();
		timedEvents = new Array<timedEvent>(); //timed events
		trigEvents = new Array<triggeredEvent>(); //triggered events
		constEvents = new Array<constantEvent>();
		enemies = new Array<Actor>();
		enemyMissiles = new Array<Projectile>();
		actors = new Array<Actor>();

		// adds mig spawner event
		trigEvents.add(new level1());
	}

	// Spawner functions
	public void spawnBullet(Vector3 position){
		// spawns bullet
		Projectile bullet = new Projectile(500,"blue");
		bullet.x = position.x;
		bullet.y = position.y + 40;
		bullet.width=2;
		bullet.height=5;
		bullet.texture = bulletTexture;
		bullet.velocity = new Vector2(0,500);
		projectiles.add(bullet);
	}

	public void spawnMig(){
		// spawns an enemy Mig-21
		MiGCount++;
		String MiGName = "MiG-" + MiGCount;
		Actor MiG21 = new Actor(10000, MiGName, "red");
		MiG21.texture = MIG21Texture;
		MiG21.x = MathUtils.random(0,800-56);
		MiG21.y = 400;
		MiG21.width = 56;
		MiG21.height = 79;
		enemies.add(MiG21);
	}
	public void spawnMig(int xArg, int yArg){
		// spawns an enemy Mig-21 at the given coordinates
		MiGCount++;
		String MiGName = "MiG-" + MiGCount;
		Actor MiG21 = new Actor(10000,MiGName, "red");
		MiG21.texture = MIG21Texture;
		MiG21.x = xArg;
		MiG21.y = yArg;
		MiG21.width = 56;
		MiG21.height = 79;
		enemies.add(MiG21);
		timedEvents.add(new FiringPattern1(MiGName));
	}

	public void spawnTargetMig(int xArg, int yArg){
		// spawns an enemy Mig-21 with targeting missiles at the given coordinates
		MiGCount++;
		String MiGName = "MiG-" + MiGCount;
		Actor MiG21 = new Actor(10000,MiGName, "red");
		MiG21.texture = MIG21Texture;
		MiG21.x = xArg;
		MiG21.y = yArg;
		MiG21.width = 56;
		MiG21.height = 79;
		enemies.add(MiG21);
		timedEvents.add(new FiringPattern2(MiGName));
	}

	public void spawnKingMig(int xArg, int yArg){
		// spawns an enemy MiG that launches missile barrages at the given coordinates
		MiGCount++;
		String MiGName = "MiG-" + MiGCount;
		Actor MiG21 = new Actor(10000,MiGName, "red");
		MiG21.texture = MIG21Texture;
		MiG21.x = xArg;
		MiG21.y = yArg;
		MiG21.width = 56;
		MiG21.height = 79;
		enemies.add(MiG21);
		timedEvents.add(new FiringPattern3(MiGName));
	}

	public void spawnSprey(int xArg, int yArg){
		//TODO: add pierre sprey asset
		MiGCount++;
		String MiGName = "MiG-" + MiGCount;
		Actor MiG21 = new Actor(1000000,MiGName, "red");
		MiG21.texture = SpreyTexture;
		MiG21.x = xArg;
		MiG21.y = yArg;
		MiG21.width = 177;
		MiG21.height = 182;
		MiG21.healthDisplay = true;
		enemies.add(MiG21);
		timedEvents.add(new FiringPattern4(MiGName,0,0,2));
		timedEvents.add(new FiringPattern4(MiGName,177,0,2));
		trigEvents.add(new SpreyAngry(MiGName));
		constEvents.add(new chaseX(MiGName, 150));
	}


	public void spawnMissile(Vector3 position){
		// spawns missile
		Projectile missile = new Projectile(10000,"blue");
		missile.x = position.x;
		missile.y = position.y;
		missile.width=10;
		missile.height=30;
		missile.texture = AMRAAMTexture;
		missile.velocity = new Vector2(0,300);
		projectiles.add(missile);
	}

	public void spawnProjectile(int width, int height, Vector2 position, Vector2 velocity, String team, int damage, Texture texture){
		Projectile projectile = new Projectile();
		projectile.damage = damage;
		projectile.team = team;
		projectile.velocity = velocity;
		projectile.x = position.x;
		projectile.y = position.y;
		projectile.width = width;
		projectile.height = height;
		projectile.texture = texture;
		projectiles.add(projectile);
	}

	public void spawnMissile(float xArg, float yArg, String team, Vector2 velocity){
		// spawns missile, works for all teams
		Projectile missile = new Projectile(10000,team);
		missile.x = xArg;
		missile.y = yArg;
		missile.width=10;
		missile.height=30;
		missile.team = team;
		missile.velocity = velocity;
		projectiles.add(missile);
	}

	public Actor findEnemy(String nameString){
		// finds enemy with the given name
		for (Actor enemy : enemies){
			if(enemy.name.equals(nameString)) return enemy;
		}
		return null;
	}


	public Vector2 locateObject(Rectangle origin, Rectangle target){
		// finds the position of target rectangle relative to origin rectangle, in the form of a vector
		float x = target.x - origin.x;
		float y = target.y - origin.y;
		return new Vector2(x,y);
	}

	public Vector2 locateObject(Vector2 origin, Rectangle target){
		// finds the position of target rectangle relative to origin point, in the form of a vector
		float x = target.x - origin.x;
		float y = target.y - origin.y;
		return new Vector2(x,y);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 10, 1);
		game.batch.setProjectionMatrix(camera.combined);

		camera.update();

		game.batch.begin();

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
			game.batch.draw(bulletTexture, bullet.x, bullet.y);
		}

		// draws missiles
		for(Projectile missile : missiles) {
			game.batch.draw(AMRAAMTexture, missile.x, missile.y);
		}

		// draws enemy missiles
		for(Projectile missile : enemyMissiles) {
			game.batch.draw(AMRAAMTexture, missile.x, missile.y); //TODO: Give proper textures to enemy missiles
		}

		// draws projectiles
		for (Projectile projectile : projectiles){
			game.batch.draw(projectile.texture, projectile.x, projectile.y);
		}
		
		// draws enemies
		for (Actor enemy : enemies) {
			game.batch.draw(enemy.texture, enemy.x, enemy.y);
			if (enemy.healthDisplay){
				game.font.draw(game.batch, "HP: " + enemy.health, enemy.x + enemy.width/2, enemy.y+enemy.height);
			}
		}

		// updates sprite position
		F35.setPosition(mousePos.x-38, mousePos.y - 41);
		game.batch.draw(F35Texture, F35.x, F35.y);

		// draws score
		game.font.draw(game.batch, "Score: " + score + " Health: " + F35.health, 0,480);

		game.batch.end();

		// moves bullets, and removes them if they are no longer on the screen, and checks if they collide with anything
		/* 
		
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

			for(Actor enemy : enemies){
				if (bullet.overlaps(enemy)) {
					enemy.hit(bullet);
					iter.remove();
					break;
				}
			}
		}
		*/

		// moves missiles, + collision detection
		/*
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

		// moves missiles, + collision detection
		for (Iterator<Projectile> iter = enemyMissiles.iterator(); iter.hasNext(); ){
			Projectile missile = iter.next();
			missile.y -= 300 * Gdx.graphics.getDeltaTime(); //Missile speed: 300 px per second
			if (missile.y <= 0){
				iter.remove();
				continue;
			}
		
			if (missile.overlaps(F35)){
				F35.hit(missile);
				iter.remove();
			}
		}
		*/

		// Moves projectiles + collision detection
		for (Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext(); ){
			Projectile projectile = iter.next();
			projectile.move(Gdx.graphics.getDeltaTime());
			// makes sure projectile stays within the game
			if (projectile.y <= 0 || projectile.y >= 480 || projectile.x <= 0 || projectile.x >= 800){
				iter.remove();
				continue;
			}

			for (Actor enemy : enemies){
				if (projectile.overlaps(enemy)){
					enemy.hit(projectile);
					//removes missile if its from blue team after hitting enemy
					if (projectile.team.equals("blue")){
						iter.remove();
						continue;
					}
				}
			}

			if (projectile.overlaps(F35)){
				F35.hit(projectile);
				if (projectile.team.equals("red")){
					iter.remove();
					continue;
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

		//Handles triggeredEvents
		for (Iterator<triggeredEvent> iter = trigEvents.iterator(); iter.hasNext(); ) {
			triggeredEvent event = iter.next();
		
			// removes killed events
			if(!event.getState()){
				iter.remove();
				continue;
			}
		
			// Debugging code
			//System.out.println(Gdx.graphics.getDeltaTime());
			//System.out.println(event.counter);
			//System.out.println(event.delay);
			event.execute();
		}

		//Handles constantEvents
		for (Iterator<constantEvent> iter = constEvents.iterator(); iter.hasNext(); ) {
			constantEvent event = iter.next();
		
			// removes killed events
			if(!event.getState()){
				iter.remove();
				continue;
			}
		
			// Debugging code
			//System.out.println(Gdx.graphics.getDeltaTime());
			//System.out.println(event.counter);
			//System.out.println(event.delay);
			event.event();
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

		//Handles actor movement
		for (Actor enemy : enemies) {
			enemy.move(Gdx.graphics.getDeltaTime());
		}

		//Handles endgame conditions
		if (F35.health <= 0){
			game.setScreen(new EndScreen(game, score));
		}

	}
	
	@Override
	public void dispose () {
		// Garbage collection
		img.dispose();
		F35Texture.dispose();
		bulletTexture.dispose();
		AMRAAMTexture.dispose();
		BAMRAAMTexture.dispose();
		cannonSound.dispose();
		cannonEndSound.dispose();
		backgroundMusic.dispose();
		MIG21Texture.dispose();
		SpreyTexture.dispose();
		AngrySpreyTexture.dispose();
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
    public void show() {
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

	private class missileBarrage extends timedEvent {
		// fires barrageCount missile barrages, each containing missileCount missiles
		int barrageCounter = 0;
		int barrageCount;
		int missileCount;
		String name;
		// Where the missiles should launch, relative to the launcher's origin
		int offsetX;
		int offsetY;
		int degOffset = 1; // how much each missile is rotated compared to the last one, in degrees


		void event(){
			if (barrageCounter >= barrageCount) {
				kill();
				return;
			}
			Actor launcher = findEnemy(name);
			if (launcher == null) {
				kill();
				return;
			}
			Vector2 position = new Vector2(launcher.x + offsetX, launcher.y + offsetY);
			Vector2 velocity = locateObject(position,F35);
			velocity.nor().scl(300);
			velocity.rotateDeg(-(degOffset * missileCount)/2);
			for (int i = 0; i < missileCount; i++){
				velocity.rotateDeg(degOffset);
				// System.out.println(velocity.angleDeg()); //Debugging code
				spawnProjectile(10,30,position, new Vector2(velocity.x,velocity.y),"red",10000,BAMRAAMTexture);
			}
			barrageCounter++;
			counter = 0;

		}

		public missileBarrage(int missileCount, int barrageCount, float delay, String name, int offsetX, int offsetY){
			this.missileCount = missileCount;
			this.barrageCount = barrageCount;
			this.delay = delay;
			this.name = name;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}

		public missileBarrage(int missileCount, int barrageCount, float delay, String name, int degOffset){
			this.missileCount = missileCount;
			this.barrageCount = barrageCount;
			this.delay = delay;
			this.name = name;
			this.degOffset = degOffset;
		}

		public missileBarrage(int missileCount, int barrageCount, float delay, String name, int offsetX, int offsetY, int degOffset){
			this.missileCount = missileCount;
			this.barrageCount = barrageCount;
			this.delay = delay;
			this.name = name;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.degOffset = degOffset;
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

	private class FiringPattern1 extends timedEvent {
		// 1/4 chance of firing an unguided missile every second
		private String MiGName;

		void event(){
			Actor MiG = findEnemy(MiGName);
			if (MiG == null) {
				kill();
				return;
			}
			int RNGesus = MathUtils.random(0,3);
			if (RNGesus == 2){
				spawnProjectile(10,30,new Vector2(MiG.x,MiG.y), new Vector2(0,-300),"red",10000,BAMRAAMTexture);
			}
			counter = 0;

		}

		public FiringPattern1(String nameArg){
			MiGName = nameArg;
			delay = 1;
		}
	}

	private class FiringPattern2 extends timedEvent{
		// 1/4 chance of firing an unguided rocket aimed at the player every second
		private String MiGName;

		void event(){
			Actor MiG = findEnemy(MiGName);
			if (MiG == null) {
				kill();
				return;
			}
			int RNGesus = MathUtils.random(0,3);
			if (RNGesus == 2){
				Vector2 position = new Vector2(MiG.x, MiG.y);
				Vector2 velocity = locateObject(MiG,F35);
				velocity.nor();
				velocity.scl(300);

				spawnProjectile(10,30,position, velocity,"red",10000,BAMRAAMTexture);
			}
			counter = 0;

		}

		public FiringPattern2(String nameArg){
			MiGName = nameArg;
			delay = 1;
		}
	}
	
	private class FiringPattern3 extends timedEvent{
		//Fires a barrage of 10 unguided missiles every second
		private String MiGName;

		void event(){
			Actor MiG = findEnemy(MiGName);
			if (MiG == null) {
				kill();
				return;
			}
			Vector2 position = new Vector2(MiG.x, MiG.y);
			Vector2 velocity = locateObject(MiG,F35);
			velocity.nor().scl(300);
			for (int i = 0; i < 10; i++){
				velocity.rotateDeg(1);
				// System.out.println(velocity.angleDeg()); //Debugging code
				spawnProjectile(10,30,position, new Vector2(velocity.x,velocity.y),"red",10000,BAMRAAMTexture);
			}
			counter = 0;

		}

		public FiringPattern3(String nameArg){
			MiGName = nameArg;
			delay = 1;
		}
	}

	private class FiringPattern4 extends timedEvent {
		private String name;
		// how far from the launcher's origin point should the missiles fire from
		private int offsetX = 0;
		private int offsetY = 0;
		private int degOffset = 1;
		
		void event(){
			Actor launcher = findEnemy(name);
			if (launcher == null) {
				kill();
				return;
			}
			timedEvents.add(new missileBarrage(20,5,(float) 0.1,launcher.name,offsetX,offsetY, degOffset));
			counter = 0;
		}

		public FiringPattern4(String nameArg){
			name = nameArg;
			delay = 3;
		}

		public FiringPattern4(String nameArg, int offsetX, int offsetY){
			name = nameArg;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			delay = 3;
		}
		
		public FiringPattern4(String nameArg, int offsetX, int offsetY, int degOffset){
			name = nameArg;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.degOffset = degOffset;
			delay = 3;
		}
	}
	
	// Triggered events classes


	// levels

	private class level1 extends triggeredEvent {

		void event(){
			spawnMig1(400);
			trigEvents.add(new level2());
			kill();
		}
		boolean condition(){
			return enemies.isEmpty();
		}
	}

	private class level2 extends triggeredEvent {

		void event(){
			spawnMig1(400);
			spawnMig1(300);
			trigEvents.add(new level3());
			kill();
		}

		boolean condition(){
			return enemies.isEmpty();
		}

	}

	private class level3 extends triggeredEvent {

		void event(){
			spawnMig2(400);
			trigEvents.add(new level4());
			kill();
		}
		boolean condition(){
			return enemies.isEmpty();
		}
	}

	private class level4 extends triggeredEvent {

		void event(){
			spawnMig2(400);
			spawnMig2(300);
			trigEvents.add(new level5());
			kill();
		}
		boolean condition(){
			return enemies.isEmpty();
		}
	}

	private class level5 extends triggeredEvent {
		void event(){
			enemySpawn3(400);
			spawnMig2(300);
			trigEvents.add(new level6());
			kill();
		}
		boolean condition(){
			return enemies.isEmpty();
		}

	}

	private class level6 extends triggeredEvent {
		void event(){
			spawnSprey(400, 300);
			trigEvents.add(new winGame());
			kill();
		}
		boolean condition(){
			return enemies.isEmpty();
		}

	}

	private class winGame extends triggeredEvent {
		void event(){
			game.setScreen(new WinScreen(game, score));
		}

		boolean condition(){
			return enemies.isEmpty();
		}
	}


	//Triggered events

	private class SpreyAngry extends triggeredEvent{
		private String name;

		void event(){
			Actor sprey = findEnemy(name);
			sprey.texture = AngrySpreyTexture;
			kill();
		}

		boolean condition(){
			Actor sprey = findEnemy(name);
			return sprey.health <= 750000;
		}

		public SpreyAngry(String name){
			this.name = name;
		}
	}


	// Constant Event Classes

	private class chaseX extends constantEvent {
		// makes enemy chase the player
		private String enemyName; // name of enemy chasing the player
		private int speed; //speed multiplier
		
		void event(){
			Actor enemy = findEnemy(enemyName);
			Vector2 newVelocity = null;
			try{
				newVelocity = locateObject(enemy, F35);
			} catch (NullPointerException e){
				kill();
				return;
			}
			newVelocity.nor();
			newVelocity.y = 0;
			newVelocity.scl(speed);
			enemy.velocity = newVelocity;
		}

		public chaseX(String enemyName, int speed){
			this.enemyName = enemyName;
			this.speed = speed;
		}
	}
	// enemy spawning methods

	private void spawnMig1(int yArg){
		// spawns a line of 8 migs at the given y value
		for(int i = 0; i < 8; i++){
			spawnMig(100*i + 1,yArg);
		}
	}

	private void spawnMig2(int yArg){
		// spawns a line of 8 targeting migs at the given y value
		for(int i = 0; i < 8; i++){
			spawnTargetMig(100*i + 1,yArg);
		}
	}

	private void enemySpawn3(int yArg){
		// spawns 3 king migs at the given y value
		spawnKingMig(200, yArg);
		spawnKingMig(300, yArg);
		spawnKingMig(400, yArg);
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

abstract class constantEvent {
	// Event that executes every single time render() is called
	private boolean alive = true;

	abstract void event();

	public void kill(){
		alive = false;
	}
	
	public boolean getState(){
		return alive;
	}
}