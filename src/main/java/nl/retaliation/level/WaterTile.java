package nl.retaliation.level;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.Tile;

public class WaterTile extends Tile{
	
	private static Sprite sprite = new Sprite("src/main/java/nl/retaliation/sprites/water.png");

	public WaterTile() {
		super(sprite);
		
	}

}
