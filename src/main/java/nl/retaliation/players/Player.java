package nl.retaliation.players;

import java.net.Socket;

import nl.retaliation.networking.Client;

public class Player {
	private int id;
	private int color;
	
	private boolean networked;
	private Client client;
	
	
	public Player() {
		this.networked = false;
	}
	public Player(int port, String host) {
		this.networked = true;
	}

	public int getID() {
		return id;
	}
}
