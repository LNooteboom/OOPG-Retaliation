package nl.retaliation.building;

import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.retaliation.Retaliation;
import nl.retaliation.players.IPlayer;

public class HQRed extends Building{
	public HQRed(float x, float y, int TILESIZE, IPlayer player, GameEngine gameEngine){
		super(x, y, new Sprite("nl/retaliation/media/sprites/HQ.png"), TILESIZE, 1000, 5, 1000, player, gameEngine);
		
	}

	@Override
	public void addToEngine(Retaliation engine) {
		engine.addGameObject(this);
	}
}
