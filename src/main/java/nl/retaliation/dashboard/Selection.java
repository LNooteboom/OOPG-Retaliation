package nl.retaliation.dashboard;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.AnimatedSpriteObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.retaliation.IRTSObject;

/**
 * 
 * @author Jonathan Vos
 *
 */

public class Selection extends AnimatedSpriteObject{
	IRTSObject object;
	HealthBar healthBar;
	int TILESIZE;

	public Selection(GameEngine gameEngine, Sprite sprite, int TILESIZE, IRTSObject object) {
		super(sprite, 1);
		healthBar = new HealthBar(object, TILESIZE);
		gameEngine.addGameObject(healthBar);
		
		this.object = object;
		this.TILESIZE = TILESIZE;
		
		this.setX(object.getX());
		this.setY(object.getY());
	}
	
	public void removeSelf(GameEngine gameEngine){
		gameEngine.deleteGameObject(healthBar);
		gameEngine.deleteGameObject(this);
	}

	@Override
	public void update() {
		this.setX(object.getX());
		this.setY(object.getY());
	}
	
}
