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
import nl.retaliation.Retaliation;
import nl.retaliation.dashboard.Selection;
import nl.retaliation.logic.Vector2;
import nl.retaliation.unit.Unit;

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

	private ArrayList<Socket> connectedClients = new ArrayList<Socket>();
	private ArrayList<BufferedReader> input = new ArrayList<BufferedReader>();
	private ArrayList<PrintWriter> output = new ArrayList<PrintWriter>();

	private Retaliation engine;
	private TileMap tileMap;
	private String tileMapString;

	public Server(int port, Retaliation engine) {
		this.port = port;
		this.tileMap = engine.getTileMap();
		this.engine = engine;

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
		try {
			if (in.ready()) {
				String line = in.readLine();
				System.out.println(line);
				if (line != null && line.length() > 0) {
					getSelection(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String output = "";
		for (IRTSObject rtsObject : gameobjects) {
			output += rtsObject.serialize();
		}
		out.println(output);
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
		return tileMapString;
	}
	private void getSelection(String input) {
		int prevSepPos = 0;
		ArrayList<Integer> inputData = new ArrayList<Integer>();
		for (int i = 1; i < input.length(); i++) {
			if (input.charAt(i) == '$') {
				inputData.add(Integer.parseInt(input.substring(prevSepPos + 1, i)));
				prevSepPos = i;
			} else if (input.charAt(i) == '%') {
				inputData.add(Integer.parseInt(input.substring(prevSepPos + 1, i)));
				prevSepPos = i + 1;
				break;
			}
		}
		//System.out.println(input.substring(prevSepPos));
		Vector2 desiredPos = new Vector2(0, 0);
		for (int i = prevSepPos + 1; i < input.length(); i++) {
			if (input.charAt(i) == '$') {
				desiredPos.setX(Integer.parseInt(input.substring(prevSepPos + 1, i)));
				prevSepPos = i;
				break;
			}
		}
		desiredPos.setY(Integer.parseInt(input.substring(prevSepPos + 1, input.length())));

		ArrayList<Selection> newSelection = new ArrayList<Selection>();
		System.out.println(inputData.size());
		for (int id : inputData) {
			IRTSObject selectedObject = engine.getObjectFromID(id, engine.getObjects());
			if (selectedObject != null) {
				newSelection.add(new Selection(engine, 32, selectedObject));
				//if (selectedObject instanceof Unit) {
					//((Unit)selectedObject).setPath(desiredPos, engine.getTileMap(), engine.getObjects(), 0.1f);
				//}
			}
		}
		engine.getPlayer().setSelection(newSelection);
		engine.getPlayer().setPathOfSelection(desiredPos, engine.getTileMap(), engine.getObjects());
	}
}
