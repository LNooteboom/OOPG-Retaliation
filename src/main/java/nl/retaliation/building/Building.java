package nl.retaliation.building;

import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.AnimatedSpriteObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.han.ica.OOPDProcessingEngineHAN.View.Viewport;
import nl.retaliation.Explosion;
import nl.retaliation.IRTSObject;
import nl.retaliation.logic.Vector2;
import nl.retaliation.players.IPlayer;
import nl.retaliation.unit.Unit;
import nl.retaliation.unit.weapon.Weapon;
import processing.core.PGraphics;

public abstract class Building extends AnimatedSpriteObject implements IRTSObject{
	private int id;
	private static int amountOfBuildings;
	
	private Vector2 tilePosition;
	private int TILESIZE;
	
	private boolean isIndestructible;
	
	private int maxHealth, health, armor;
	
	private IPlayer player;
	private GameEngine engine;
	
	Building(float x, float y, Sprite sprite, int TILESIZE, int health, int armor, IPlayer player, GameEngine engine){
		super(sprite, 1);
		amountOfBuildings++;
		id = amountOfBuildings + Unit.getAmountOfUnits();
		
		this.TILESIZE = TILESIZE;
		this.setX(x * TILESIZE);
		this.setY(y * TILESIZE);
		tilePosition = new Vector2((int)x, (int)y);
		
		this.setWidth(TILESIZE);
		this.setHeight(TILESIZE);
		
		this.maxHealth = health;
		this.health = maxHealth;
		this.armor = armor;
		this.player = player;
		this.engine = engine;
	}
	
	public void update(){
		if (health <= 0 && isIndestructible == false) {
			destroy();
		}
	}
	
	@Override
	public void drawWithViewport(PGraphics g, Viewport viewport) {
		g.tint(player.getColor());
		super.drawWithViewport(g, viewport);
		g.noTint();
	}
	
	@Override
	public void destroy() {
		engine.addGameObject(new Explosion(tilePosition, TILESIZE, engine));
		player.removeIRTSObject(this);
	}

	@Override
	public void damage(int amount) {
		health -= amount;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
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
	public String serialize() {
		String output = "%";
		output += ("$" + this.getClass());
		output += ("$" + player.getID());
		output += ("$" + id);
		output += ("$" + (int) tilePosition.getX());
		output += ("$" + (int) tilePosition.getY());
		output += ("$" + health);
		
		return output;
	}
	
	@Override
	public void addWeapon(Weapon newWeapon) {
		
	}
	@Override
	public void target(IRTSObject enemy, TileMap terrain, ArrayList<IRTSObject> gameobjects) {
		
	}
	
	public float getHealthPercentage() {
		return (float)health / (float)maxHealth;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	
	@Override
	public int getID() {
		return id;
	}
	@Override
	public void setID(int id) {
		this.id = id;
	}
	
	public static int getAmountOfBuildings() {
		return amountOfBuildings;
	}
}
