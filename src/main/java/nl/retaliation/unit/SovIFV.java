package nl.retaliation.unit;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.retaliation.players.IPlayer;

public class SovIFV extends GroundUnit {

	public SovIFV(float x, float y, int tileSize, IPlayer player) {
		
		super(x, y, new Sprite("nl/retaliation/media/sprites/IFV.png"), tileSize, 6, 300, 100, player);
		setStepOnWater(false);
	}

}
