package nl.retaliation.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.IRTSObject;
import nl.retaliation.players.Player;

public class Server {
	private ArrayList<IRTSObject> RTSObjects;
	private String hostName;
	private int port;
	
	//private ServerSocket socket2;
	//private PrintWriter out;
	//private BufferedReader in;
	private ArrayList<Socket> connectedClients;
	
	private boolean sendTileMap;
	private TileMap tileMap;
	private GameObject[][] gameobjects;
	
	public Server(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
	}
	private void setupServer() {
		try (
				ServerSocket socket = new ServerSocket(port)
			) {
			for (Socket client : connectedClients) {
				connectToPlayer(client);
			}
		} catch (IOException e) {
			System.out.println("Error making server");
		}
	}
	public void waitForClient(ServerSocket serverSocket) {
		while (connectedClients.size() == 0) {
			try {
				connectedClients.add(serverSocket.accept());
			} catch (IOException e){
				System.out.println("Error waiting for clients?");
			}
		}
	}
	
	private void connectToPlayer(Socket client) {
		try (
				Socket connectedClient = client;
				PrintWriter out = new PrintWriter(connectedClient.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
			) {
			transceiveData(out, in);
		} catch (IOException e) {
			System.out.println("Error connecting player");
		}
	}
	private void transceiveData(PrintWriter out, BufferedReader in) {
		//send
		if (sendTileMap) {
			out.println(tileMap.getTileMap());
		}
		out.println(gameobjects);
	}
}
