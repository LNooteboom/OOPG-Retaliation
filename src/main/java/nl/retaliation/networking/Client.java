package nl.retaliation.networking;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;

import nl.han.ica.OOPDProcessingEngineHAN.Objects.GameObject;
import nl.han.ica.OOPDProcessingEngineHAN.Objects.Sprite;
import nl.han.ica.OOPDProcessingEngineHAN.Tile.TileMap;
import nl.retaliation.IRTSObject;

public class Client {
	private int hostPort;
	private String hostName;
	private BufferedReader input;
	private PrintWriter output;
	
	public Client(String hostName, int hostPort) {
		this.hostName = hostName;
		this.hostPort = hostPort;
		
		createClient();
		//System.out.println("c: "+socket.isClosed());
	}
	
	private void createClient() {
		try {
			Socket socket = new Socket(hostName, hostPort);
			//socket.setKeepAlive(true);
			socket.setTcpNoDelay(true);
			
			this.input = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
			//this.output = new PrintWriter(socket.getOutputStream(), true);
			
			System.out.println("connected!");
			
		} catch (UnknownHostException e){
			System.out.println("Could not find host");
		} catch (IOException e) {
			System.out.println("failed to connect");
		}
	}
	
	public IRTSObject transceiveData(TileMap tilemap) {
		try {
			String line = input.readLine();
			//System.out.println(line);
			
			return deserializeGameObject(line);
			
			//if (line != null && line == "start") {
				//System.out.println("received: "+input.readLine());
				//return deserializeGameObject(line);
				//socket.close();
			//}
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}
	private void deserializeGameObjects(String input) {
		char objectSeperator = '\n';
		int nextSep = 0;
		
	}
	private IRTSObject deserializeGameObject(String input) {
		Class<IRTSObject> objectClass = null;
		boolean classExists = false;
		
		IRTSObject newObject;
		int xPos = 0;
		int yPos = 0;
		float direction;
		
		int prevSepPos = 0;
		char seperator = '$';
		for (int i = 1; i < input.length(); i++) {
			if (input.charAt(i) == seperator) {
				String className = input.substring(7, i);
				//System.out.println(className);
				
				try {
					objectClass = (Class<IRTSObject>) Class.forName(className);
					classExists = true;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				prevSepPos = i;
				break;
			}
		}
		for (int i = prevSepPos + 1; i < input.length(); i++) {
			if (input.charAt(i) == '$') {
				xPos = Integer.parseInt(input.substring(prevSepPos + 1, i));
				//System.out.println(xPos);
				//System.out.println(input.substring(prevSepPos + 1, i));
				prevSepPos = i;
				break;
			}
		}
		for (int i = prevSepPos + 1; i < input.length(); i++) {
			if (input.charAt(i) == '$') {
				yPos = Integer.parseInt(input.substring(prevSepPos + 1, i));
				prevSepPos = i;
				break;
			}
		}
		if (classExists) {
			
			Class<?>[] pars = {Float.TYPE, Float.TYPE, Integer.TYPE};
			Object[] initargs = {xPos, yPos, 32};
			try {
				Constructor<?> constructor = objectClass.getConstructor(pars);
				try {
					newObject = (IRTSObject) constructor.newInstance(initargs);
					//System.out.println(newObject.getX());
					//Holy shit het werkt!!
					return newObject;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
