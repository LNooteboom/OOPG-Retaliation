package nl.retaliation.unit.weapon;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;

public class MachineGun extends Weapon{
	
	public MachineGun(GameObject parent, int tileSize) {
		super(parent, 10 * tileSize, 5, false);
		this.damage = 10;
	}

	@Override
	public void fire() {
		enemy.damage(damage);
	}

}
