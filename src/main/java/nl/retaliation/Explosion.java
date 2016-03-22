package nl.retaliation;

import nl.han.ica.OOPDProcessingEngineHAN.Alarm.Alarm;
import nl.han.ica.OOPDProcessingEngineHAN.Alarm.IAlarmListener;
import nl.han.ica.OOPDProcessingEngineHAN.Engine.GameEngine;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.AnimatedSpriteObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.retaliation.logic.Vector2;

public class Explosion extends AnimatedSpriteObject implements IAlarmListener{
	private GameEngine gameEngine;
	private final double SECONDS_PER_FRAME = 1.0/24;
	private Alarm alarm;
	
	public Explosion(Vector2 pos, int TILESIZE, GameEngine gameEngine){
		super(new Sprite("nl/retaliation/media/sprites/explosion.png"), 24);
		
		this.gameEngine = gameEngine;
		this.setX(pos.getX() * TILESIZE);
		this.setY(pos.getY() * TILESIZE);
		alarm = new Alarm(this.toString(), SECONDS_PER_FRAME);
		alarm.addTarget(this);
		alarm.start();
	}

	@Override
	public void update() {
		if(getCurrentFrameIndex() == 23){
			alarm.stop();
			gameEngine.deleteGameObject(this);
		}
	}

	@Override
	public void triggerAlarm(String alarmName) {
		if(this.toString().equals(alarmName)){
			nextFrame();
			
			alarm = new Alarm(this.toString(), SECONDS_PER_FRAME);
			alarm.addTarget(this);
			alarm.start();
		}
		else{
			System.out.println("WARNING: Should not be able to get to this code.");
			System.out.println("Explosion.triggerAlarm()");
		}
	}
}
