package nl.retaliation.building;

import java.util.ArrayList;

import processing.core.PGraphics;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.*;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.han.ica.OOPDProcessingEngineHAN.View.Viewport;
import nl.retaliation.IRTSObject;
import nl.retaliation.logic.Vector2;
import nl.retaliation.players.IPlayer;
import nl.retaliation.players.Player;
import nl.retaliation.unit.weapon.Weapon;

public abstract class Building extends AnimatedSpriteObject implements IRTSObject{
	private Vector2 tilePosition;
	
	private boolean isIndestructible;
	
	private int health, armor;
	
	private IPlayer player;
	
	Building(float x, float y, Sprite sprite, int TILESIZE, int health, int armor, IPlayer player){
		super(sprite, 1);
		
		this.setX(x * TILESIZE);
		this.setY(y * TILESIZE);
		tilePosition = new Vector2((int) x / TILESIZE, (int) y / TILESIZE);
		
		this.setWidth(TILESIZE);
		this.setHeight(TILESIZE);
		
		this.health = health;
		this.armor = armor;
		this.player = player;
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
	
	public abstract void destroy();

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
	public Player getOwner() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String serialize() {
		//TODO: Add serialization
		return "";
	}
	
	@Override
	public void addWeapon(Weapon newWeapon) {
		
	}
	@Override
	public void target(IRTSObject enemy, TileMap terrain, ArrayList<IRTSObject> gameobjects) {
		
	}
	
	public float getHealthPercentage() {
		return 0;
	}
}
