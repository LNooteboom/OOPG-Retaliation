package nl.retaliation.networking;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.IRTSObject;

public class Server {
	private int port;
	private int time = 0;
	private final int wait = 10;
	
	//private ServerSocket socket;
	private RTSProtocolServer protocol = new RTSProtocolServer();
	
	private ArrayList<Socket> connectedClients = new ArrayList<Socket>();
	private ArrayList<BufferedReader> input = new ArrayList<BufferedReader>();
	private ArrayList<PrintWriter> output = new ArrayList<PrintWriter>();
	
	private boolean sendTileMap;
	
	public Server(int port) {
		this.port = port;
		
		setupServer();
	}
	private void setupServer() {
		try (
				ServerSocket socket = new ServerSocket(port)
			) {
			//this.socket = socket;
			waitForClient(socket);
		} catch (IOException e) {
			System.out.println("Error making server");
		}
	}
	public void waitForClient(ServerSocket serverSocket) {
		while (connectedClients.size() < 1) {
			try {
				Socket newClient = serverSocket.accept();
				input.add(new BufferedReader(new InputStreamReader(newClient.getInputStream())));
				output.add(new PrintWriter(new BufferedOutputStream(newClient.getOutputStream()), false));
				connectedClients.add(newClient);
			} catch (IOException e){
				System.out.println("Error waiting for clients?");
			}
		}
	}
	public void sendData(ArrayList<IRTSObject> gameobjects, TileMap tilemap) {
		if (time == wait) {
			for (int i = 0; i < connectedClients.size(); i++) {
				transceiveData(output.get(i), input.get(i), gameobjects, tilemap);
			}
			time = 0;
		} else {
			time++;
		}
	}
	private void transceiveData(PrintWriter out, BufferedReader in, ArrayList<IRTSObject> gameobjects, TileMap tilemap) {
		String output = "";
		for (IRTSObject rtsObject : gameobjects) {
			output += rtsObject.serialize();
		}
		out.println(output);
		out.flush();
	}
	
	public boolean sendTileMap() {
		return sendTileMap;
	}
	public void setSendTileMap(boolean sendTileMap) {
		this.sendTileMap = sendTileMap;
	}
}
