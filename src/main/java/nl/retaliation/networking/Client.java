package nl.retaliation.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private int hostPort;
	private String hostName;
	private Socket socket;
	
	Client(String hostName, int hostPort) {
		this.hostName = hostName;
		this.hostPort = hostPort;
		
		createClient();
	}
	
	private void createClient() {
		try (
				Socket newSocket = new Socket(hostName, hostPort);
				PrintWriter out = new PrintWriter(newSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
			) {
			socket = newSocket;
			System.out.println("connected!");
		} catch (IOException e) {
			System.out.println("failed to connect");
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
}
