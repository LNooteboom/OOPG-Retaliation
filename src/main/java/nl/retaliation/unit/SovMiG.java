package nl.retaliation.unit;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;

public class SovMiG extends AirUnit{
	public SovMiG(float x, float y, int tileSize){
		super(x, y, new Sprite("nl/retaliation/media/sprites/Russian_Privjet.png"), tileSize, 6, 300, 100);
	}
}
