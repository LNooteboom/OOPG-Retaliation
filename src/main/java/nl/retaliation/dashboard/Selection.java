package nl.retaliation.dashboard;

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
	int TILESIZE;

	public Selection(Sprite sprite, int TILESIZE, IRTSObject object) {
		super(sprite, 1);
		this.object = object;
		this.TILESIZE = TILESIZE;
		
		this.setX(object.getX());
		this.setY(object.getY());
	}

	@Override
	public void update() {
		this.setX(object.getX());
		this.setY(object.getY());
	}
	
}
