package nl.retaliation.level;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.Tile;

public class WaterTile extends Tile{
	
	public static final Sprite SPRITE = new Sprite("src/main/java/nl/retaliation/media/sprites/water.png");

	public WaterTile() {
		super(SPRITE);
		
	}

}
