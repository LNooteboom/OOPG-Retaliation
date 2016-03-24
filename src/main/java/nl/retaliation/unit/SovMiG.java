package nl.retaliation.unit;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.retaliation.players.IPlayer;

public class SovMiG extends AirUnit{
	public SovMiG(float x, float y, int tileSize, IPlayer player, GameEngine engine){
		super(x, y, new Sprite("nl/retaliation/media/sprites/MiG.png"), tileSize, 6, 300, 1, player, engine);
	}
}
