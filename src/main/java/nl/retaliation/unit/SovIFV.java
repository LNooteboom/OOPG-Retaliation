package nl.retaliation.unit;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;

public class SovIFV extends GroundUnit {

	public SovIFV(float x, float y, int tileSize) {
		
		super(x, y, new Sprite("nl/retaliation/media/sprites/IFV_red.png"), tileSize, 6, 300);
		setStepOnWater(true);
	}

}
