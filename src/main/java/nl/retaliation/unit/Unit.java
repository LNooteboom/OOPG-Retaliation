package nl.retaliation.unit;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.AnimatedSpriteObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.han.ica.OOPDProcessingEngineHAN.View.Viewport;
import nl.retaliation.Explosion;
import nl.retaliation.IRTSObject;
import nl.retaliation.Retaliation;
import nl.retaliation.logic.Pathfind;
import nl.retaliation.logic.Trigonio;
import nl.retaliation.logic.Vector2;
import nl.retaliation.players.IPlayer;
import nl.retaliation.unit.weapon.MachineGun;
import nl.retaliation.unit.weapon.Weapon;
import processing.core.PGraphics;

/**
 * Unit class for every object that can move across the map
 * 
 * @author Luke Nooteboom
 *
 */
public abstract class Unit extends AnimatedSpriteObject implements IRTSObject{
	protected Vector2 tilePosition;
	protected Vector2 desiredTilePos;
	private float currentDirection = 0; //0 is right, 0,5PI is bottom etc. IN RAD
	private int spriteDirection = 0;
	
	protected ArrayList<Vector2> currentPath;
	
	protected ArrayList<IRTSObject> gameobjects;
	protected TileMap terrain;
	private int tileSize;

	private float maxSpeed;
	
	protected boolean isMoving = false;
	private boolean isIndestructible = false;
	
	private int maxHealth, health;
	private int armor;
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	
	private IPlayer player;
	private GameEngine engine;
	
	public Unit(float x, float y, Sprite sprite, int tileSize, float maxSpeed, int maxHealth, int armor, IPlayer player, GameEngine engine) {
		super(sprite, 8);
		
		this.tileSize = tileSize;
		this.setX(x * tileSize);
		this.setY(y * tileSize);
		tilePosition = new Vector2((int) x, (int) y);
		
		this.setWidth(tileSize);
		this.setHeight(tileSize);
		
		this.maxSpeed = maxSpeed;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.armor = armor;
		this.player = player;
		this.engine = engine;
		
		weapons.add(new MachineGun(this, tileSize));
		engine.addGameObject(weapons.get(0));
	}
	
	/**
	 * Call this method in when the game updates
	 */
	public void update() {
		
		tilePosition.setX((int) Math.floor(x / width));
		tilePosition.setY((int) Math.floor(y / height));
		
		if (health <= 0 && isIndestructible == false) {
			destroy();
		}
		if (isMoving) {
			moveNext();
		}
	}
	
	@Override
	public void destroy() {
		engine.addGameObject(new Explosion(tilePosition, tileSize, engine));
		engine.deleteGameObject(weapons.get(0));
		player.removeIRTSObject(this);
		engine.deleteGameObject(this);
	}
	
	@Override
	public void target(IRTSObject enemy, TileMap terrain, ArrayList<IRTSObject> gameobjects) {
		if (this.getPos().withinRadius(enemy.getPos(), 3f)) {
			for (Weapon currentWeapon : weapons) {
				currentWeapon.setEnemy(enemy);
			}
		} else {
			setPath(enemy.getPos(), terrain, gameobjects, 3f);
		}
	}
	
	/**
	 * Moves this unit to a desired location
	 * 
	 * @param desiredTilePos The desired position
	 * @param terrain The terrain to move around
	 * @param gameobjects The obstacles to avoid
	 * @param targetRadius The radius of the desired position
	 */
	public abstract void setPath(Vector2 desiredTilePos, TileMap terrain, ArrayList<IRTSObject> gameobjects, float targetRadius);
	
	@Override
	public void drawWithViewport(PGraphics g, Viewport viewport) {
		g.tint(player.getColor());
		super.drawWithViewport(g, viewport);
		g.noTint();
	}
	
	private void moveNext() {
		if (currentPath != null && currentPath.size() > 0) { //checks if path exists
			float nextX = currentPath.get(0).getX() * width;
			float nextY = currentPath.get(0).getY() * height;
			float deltaX = nextX - x;
			float deltaY = nextY - y;
			if (Pathfind.place_free(new Vector2(currentPath.get(0).getX(), currentPath.get(0).getY()), gameobjects, this, terrain) == false) {
				setPath(desiredTilePos, terrain, gameobjects, 3f);
				if (currentPath.size() == 0) {
					currentPath.add(tilePosition);
				}
				return;
			}

			if (maxSpeed >= Trigonio.distance(deltaX, deltaY)) { //To prevent oscillation
				x = nextX;
				y = nextY;

				currentPath.remove(0); //sets target to next tile in path
				if (currentPath.size() == 0) { //reached destination
					isMoving = false;
				}
			} else {
				currentDirection = Trigonio.angle(deltaX, deltaY);
				updateSpriteDirection(currentDirection);
				x = x + Trigonio.xSpeed(currentDirection, maxSpeed);
				y = y + Trigonio.ySpeed(currentDirection, maxSpeed);

				Vector2 newTile = new Vector2((int) Math.floor(x / width), (int) Math.floor(y / height));
				if (currentPath.size() > 0 && newTile.equal(currentPath.get(0))) {
					tilePosition = currentPath.get(0);
				}
			}
		}
	}
	
	private void updateSpriteDirection(double trueDirection){
		double pi8 = Trigonio.PI / 8;
		if (trueDirection >= pi8 && trueDirection < Trigonio.QUARTER_PI + pi8) { //bottom-right
			spriteDirection = 1;
			setCurrentFrameIndex(1);
			return;
		}
		if (trueDirection >= Trigonio.QUARTER_PI + pi8 && trueDirection < Trigonio.HALF_PI + pi8) { //bottom
			spriteDirection = 2;
			setCurrentFrameIndex(2);
			return;
		}
		if (trueDirection >= Trigonio.HALF_PI + pi8 && trueDirection < Trigonio.PI - pi8) { //bottom-left
			spriteDirection = 3;
			setCurrentFrameIndex(3);
			return;
		}
		if ((trueDirection >= Trigonio.PI - pi8 && trueDirection < Trigonio.PI + pi8)|| (trueDirection <= -(Trigonio.PI - pi8) && trueDirection > -(Trigonio.PI + pi8))) { //left
			spriteDirection = 4;
			setCurrentFrameIndex(4);
			return;
		}
		if (trueDirection <= -(Trigonio.HALF_PI + pi8) && trueDirection > -(Trigonio.PI - pi8)) { //top-left
			spriteDirection = 5;
			setCurrentFrameIndex(5);
			return;
		}
		if (trueDirection <= -(Trigonio.QUARTER_PI + pi8) && trueDirection > -(Trigonio.HALF_PI + pi8)) { //top
			spriteDirection = 6;
			setCurrentFrameIndex(6);
			return;
		}
		if (trueDirection <= -pi8 && trueDirection > -(Trigonio.QUARTER_PI + pi8)) { //top-right
			spriteDirection = 7;
			setCurrentFrameIndex(7);
			return;
		}
		spriteDirection = 0;
		setCurrentFrameIndex(0); //right
	}
	
	@Override
	public String serialize() {
		String output = "%";
		output += ("$" + this.getClass());
		output += ("$" + player.getID());
		output += ("$" + (int) getX());
		output += ("$" + (int) getY());
		
		if (spriteDirection != 0) {
			output += ("$" + spriteDirection);
		} else {
			output += "$0";
		}
		return output;
	}
	public void forceSpriteDirection(int direction) {
		spriteDirection = direction;
		setCurrentFrameIndex(direction);
	}
	
	public void damage(int damage) {
		health -= damage;
	}
	public int getHealth() {
		return health;
	}
	
	public float getHealthPercentage(){
		return (float)health / (float)maxHealth;
	}
	
	public int getArmor() {
		return armor;
	}
	
	public Vector2 getPos(){
		return tilePosition;
	}
	@Override
	public IPlayer getOwner() {
		return player;
	}
	@Override
	public void addWeapon(Weapon newWeapon) {
		weapons.add(newWeapon);
	}
	
	@Override
	public void addToEngine(Retaliation engine) {
		engine.addGameObject(this);
	}
}
