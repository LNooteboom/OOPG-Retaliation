package nl.retaliation.building;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.retaliation.Retaliation;

public class HQRed extends Building{
	public HQRed(float x, float y, int TILESIZE){
		super(x, y, new Sprite("nl/retaliation/media/sprites/HQ_Red.png"), TILESIZE, 1000, 300);
		
	}

	@Override
	public void destroy() {
		// Boom!
	}

	@Override
	public void addToEngine(Retaliation engine) {
		// TODO Auto-generated method stub
		
	}
}
