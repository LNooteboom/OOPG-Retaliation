package nl.retaliation.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.IRTSObject;

public class Client {
	private int hostPort;
	private String hostName;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	
	public Client(String hostName, int hostPort) {
		this.hostName = hostName;
		this.hostPort = hostPort;
		
		createClient();
		System.out.println("c: "+socket.isClosed());
	}
	
	private void createClient() {
		try 
				//Socket newSocket = new Socket(hostName, hostPort);
				//PrintWriter out = new PrintWriter(newSocket.getOutputStream(), true);
				//BufferedReader in = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
			 {
			socket = new Socket(hostName, hostPort);
			socket.setKeepAlive(true);
			
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.output = new PrintWriter(socket.getOutputStream(), true);
			
			System.out.println("connected!");
			
		} catch (UnknownHostException e){
			System.out.println("Could not find host");
		} catch (IOException e) {
			System.out.println("failed to connect");
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	
	public void transceiveData(TileMap tilemap) {
		try {
			String line = input.readLine();
			System.out.println(line);
			if (line != null && line == "start") {
				System.out.println("received: "+input.readLine());
				//socket.close();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	private void deserializeGameObjects() {
		char objectSeperator = '%';
		
	}
	private IRTSObject deserializeGameObject(String input) {
		char seperator = '$';
		int amountOfSeperators = 0;
		
		
		int[] sepLocations;
		
	}
}
