package nl.retaliation.groundUnit;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;

public class SovIFV extends GroundUnit {

	public SovIFV(float x, float y, int tileSize) {
		
		super(x, y, new Sprite("nl/retaliation/media/sprites/IFV_red.png"), tileSize, 3, 300);
	}

}
