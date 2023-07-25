package edu.wpi.schmittciroli;

import edu.wpi.schmittciroli.controllers.mainScreenController;
import edu.wpi.schmittciroli.model.Device;
import edu.wpi.schmittciroli.model.Subtitle;
import javafx.application.Platform;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server_v2 implements Runnable {

	//Vector for holding connected devices
	static final Vector<ClientHandler> cHVector = new Vector<ClientHandler>();
	private volatile boolean isStopped;
	static int numClients = 0;


	ServerSocket serverStocket;
	private String ip;
	private int port;

	private mainScreenController mSc;

	@Override
	public void run() {
		try{
			InetAddress myIpAddress = InetAddress.getByName("0.0.0.0");
			serverStocket = new ServerSocket(3542, 10, myIpAddress);
			serverStocket.setSoTimeout(0);
		}catch(IOException e){
			throw new RuntimeException("Unable to open server socket: " + e.getMessage());
		}

		serverStocket.getInetAddress();
		try {
			ip = extractIP(String.valueOf(InetAddress.getLocalHost()));
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		port = serverStocket.getLocalPort();
		/**
		 * Delegates Server to a thread and subsequent clients.
		 */
		Thread serverSocketHandler = new Thread(new Runnable() {
			@Override
			public void run() {
				Socket clientSocket;
				while(!isStopped){
					try{ //Establish client socket thread and connections
						System.out.println("Waiting for clients...");
						clientSocket = serverStocket.accept();
						numClients++;
						DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
						DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

						String id = String.valueOf(numClients);

						ClientHandler clientHandler = new ClientHandler(clientSocket, dis, dos, id);
						Thread thread = new Thread(clientHandler);

						Platform.runLater(()-> { //For running the deviceUpdater on JavaFX application thread
							mSc.addDeviceToList(new Device(id));
							mSc.setDeviceCountLBL(numClients);
						});

						synchronized (cHVector){
							cHVector.add(clientHandler);
						}

						thread.start();
					}catch (IOException ex){
						ex.printStackTrace();
					}
				}

			}
		});
		serverSocketHandler.start();
	}

	/**
	 * Sends data to connected clients via DataOutputStream
	 * Argument must be a valid string
	 * @param str string representation of data to be sent
	 * @return 	   returns -1 if failure, 0 if completed.
	 */
	public int sendDataToClients(String str){
		System.out.println(cHVector.size());
		//Send to all connected clients
		for(ClientHandler client : cHVector){
			System.out.println("DataOutputStream for client (" + client.id + "): " + client.dos.toString());
			try{
				System.out.println("Sending to: " + client.s.toString());
				byte[] data = str.getBytes(StandardCharsets.UTF_8);
				client.dos.write(data);
			}catch(IOException e){
				e.printStackTrace();
				return -1;
			}
		}
		return 0;
	}

	/**
	 * Sends subtitle file to connected Clients
	 * @param sub Subtitle object to send.
	 * @throws IOException Throws if it fails
	 */
	public void sendSubtitleFile(Subtitle sub) throws IOException {
		File sub_file = new File(sub.getFileLoc());
		byte[] data = Files.readAllBytes(sub_file.toPath());
		for(ClientHandler c : cHVector){
			try{
				c.dos.writeInt(data.length); //Sends size
				c.dos.write(data); //Sends physical data
			}catch(IOException e){
				e.printStackTrace();
			}
		}

	}

	public String extractIP(String ip){
		String ipAddress = "";

		Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+"); // regular expression for matching IP addresses
		Matcher matcher = pattern.matcher(ip);

		if (matcher.find()) {
			ipAddress = matcher.group();
		}
		return ipAddress;
	}

	/**
	 * Stops the server and client threads
	 */
	public void stop(){
		this.isStopped = true;
		try{
			serverStocket.close();
		}catch (IOException e){
			throw new RuntimeException("Error shutting down server", e);
		}

		//Stop client handlers
		for(ClientHandler clientHandler : cHVector){
			clientHandler.stop();
		}
	}

	public void setMainScreenController(mainScreenController mSc){
		this.mSc = mSc;
	}

	public String getServerInfo() {
		return ip + ":" + String.valueOf(port);
	}

}

class ClientHandler implements Runnable{
	Socket s;
	final DataInputStream dis;
	final DataOutputStream dos;
	final String id;

	private boolean isActive;

	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, String id){
		this.s = s;
		this.dis = dis;
		this.dos = dos;
		this.id = id;
		isActive = true;
	}

	/**
	 * Keeps client alive on thread.
	 */
	@Override
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(dis));

		// Read the HTTP request headers
		try {
			String requestLine = reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Map<String, String> headers = new HashMap<String, String>();
		String headerLine;
		while (true) {
			try {
				if (!((headerLine = reader.readLine()) != null && !headerLine.isEmpty())) break;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			int separatorIndex = headerLine.indexOf(':');
			if (separatorIndex != -1) {
				String name = headerLine.substring(0, separatorIndex).trim().toLowerCase();
				String value = headerLine.substring(separatorIndex + 1).trim();
				headers.put(name, value);
			}
		}

		// Check for the Upgrade header
		String upgrade = headers.get("upgrade");
		if (upgrade != null && upgrade.equalsIgnoreCase("websocket")) {
			// Accept the WebSocket connection

			// Generate a random WebSocket key
			byte[] keyBytes = new byte[16];
			new Random().nextBytes(keyBytes);
			String key = Base64.getEncoder().encodeToString(keyBytes);

			// Send the HTTP response with the necessary headers
			String response = null;
			try {
				response = "HTTP/1.1 101 Switching Protocols\r\n" +
					"Upgrade: websocket\r\n" +
					"Connection: Upgrade\r\n" +
					"Sec-WebSocket-Accept: " + generateWebSocketAccept(key) + "\r\n\r\n";
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			try {
				dos.write(response.getBytes());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		while(isActive){
			//do nothing
		}
	}


	/**
	 * Stopps clienthandler thread and socket.
	 */
	public void stop(){
		isActive = false;
		try{
			s.close();
		}catch (IOException e){
			throw new RuntimeException("Error closing client socket ", e );
		}
	}
	private String generateWebSocketAccept(String key) throws NoSuchAlgorithmException {
		String concatenated = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
		byte[] digest = sha1.digest(concatenated.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(digest);
	}

}



