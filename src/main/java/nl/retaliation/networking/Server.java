package nl.retaliation.networking;

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
				output.add(new PrintWriter(newClient.getOutputStream(), true));
				connectedClients.add(newClient);
			} catch (IOException e){
				System.out.println("Error waiting for clients?");
			}
		}
	}
	public void sendData(ArrayList<IRTSObject> gameobjects, TileMap tilemap) {
		for (int i = 0; i < connectedClients.size(); i++) {
			transceiveData(output.get(i), input.get(i), gameobjects, tilemap);
		}
	}
	private void transceiveData(PrintWriter out, BufferedReader in, ArrayList<IRTSObject> gameobjects, TileMap tilemap) {
		try {
			String input = in.readLine();
			String output = protocol.processInput(input);
			if (output != "") {
				out.println(output);
			}
		} catch (IOException e) {
			System.out.println("Error sending/receiving transmission");
		}
	}
	
	public boolean sendTileMap() {
		return sendTileMap;
	}
	public void setSendTileMap(boolean sendTileMap) {
		this.sendTileMap = sendTileMap;
	}
}
