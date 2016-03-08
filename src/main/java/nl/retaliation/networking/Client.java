package nl.retaliation.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
			
		} catch (IOException e) {
			System.out.println("failed to connect");
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	
	public void transceiveData() {
		try {
			String line = input.readLine();
			if (line != null) {
				System.out.println("received: "+line);
				//socket.close();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
