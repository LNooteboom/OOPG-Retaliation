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

/**
 * Server/Host class for sending and receiving objects to client
 * 
 * @author Luke Nooteboom
 *
 */
public class Server {
	private int port;
	private int time = 0;
	private final int wait = 10;
	private int tileMapTime = 0;
	private final int tileMapWait = 200;
	
	private ArrayList<Socket> connectedClients = new ArrayList<Socket>();
	private ArrayList<BufferedReader> input = new ArrayList<BufferedReader>();
	private ArrayList<PrintWriter> output = new ArrayList<PrintWriter>();
	
	private boolean sendTileMap = true;
	private TileMap tileMap;
	private String tileMapString;
	
	public Server(int port, TileMap tileMap) {
		this.port = port;
		this.tileMap = tileMap;
		
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
	private void waitForClient(ServerSocket serverSocket) {
		while (connectedClients.size() < 1) {
			try {
				Socket newClient = serverSocket.accept();
				input.add(new BufferedReader(new InputStreamReader(newClient.getInputStream())));
				PrintWriter printOut = new PrintWriter(new BufferedOutputStream(newClient.getOutputStream()), false);
				
				sendTileMap(tileMap);
				
				output.add(printOut);
				printOut.println(sendTileMap(tileMap));
				connectedClients.add(newClient);
			} catch (IOException e){
				System.out.println("Error waiting for clients?");
			}
		}
	}
	/**
	 * Sends the gameobjects to the connected clients
	 * 
	 * @param gameobjects The list of gameobjects
	 * @param tilemap The map
	 */
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
//		if (tileMapTime >= tileMapWait) {
//			out.println(sendTileMap(tileMap));
//			tileMapTime = 0;
//		} else {
			String output = "";
			for (IRTSObject rtsObject : gameobjects) {
				output += rtsObject.serialize();
			}
			out.println(output);
			tileMapTime++;
		//}
		out.flush();
	}
	/**
	 * Returns true if the tilemap has been sent
	 * 
	 * @return result
	 */
	public String sendTileMap(TileMap tilemap) {
		tileMapString = "#";
		int indexMap[][] = tilemap.getTileMap();
		tileMapString += "$" + indexMap[0].length;
		tileMapString += "$" + indexMap.length;
		for (int y = 0; y < indexMap.length; y++) {
			for (int x = 0; x < indexMap[0].length; x++) {
				tileMapString += ("$" + indexMap[y][x]);
			}
		}
		sendTileMap = false;
		return tileMapString;
	}
	/**
	 * Set this to true if the tilemap needs to be sent again
	 * 
	 * @param sendTileMap
	 */
	public void setSendTileMap(boolean sendTileMap) {
		this.sendTileMap = sendTileMap;
	}
}
