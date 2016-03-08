package nl.retaliation.dashboard;

import nl.han.ica.OOPDProcessingEngineHAN.Dashboard.Dashboard;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import processing.core.PGraphics;

public class Minimap extends Dashboard {
	private TileMap tilemap;
	
	public Minimap(float x, float y, TileMap tilemap) {
		super(x, y, tilemap.getTileMap()[0].length, tilemap.getTileMap().length);
		this.tilemap = tilemap;
	}
	
	@Override
	public void draw(PGraphics g) {
		
		int green = g.color(0x00, 0xFF, 0x00);
		int blue = g.color(0, 0, 255);
		for (int x = 0; x < tilemap.getTileMap()[0].length; x++) {
			for (int y = 0; y < tilemap.getTileMap().length; y++) {
				switch (tilemap.getTileMap()[y][x]) {
					case 0:
						g.set(x, y, green);
						break;
					case 1:
						g.set(x, y, blue);
						break;
				}
			}
		}
	}
}
