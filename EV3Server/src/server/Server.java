package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread listenerThread;
    private volatile boolean running = true;
    
    private static final String SQUARE = "0";
	private static final String X = "1";
	private static final String O = "2";
	private static final String TRIANGLE = "3";
	private static final String L1 = "4";
	private static final String R1 = "5";
	private static final String ARROWS = "pov";
	private static final String SHARE = "8";
	private static final String OPTION = "9";
	private static final String LSTICKBTN = "10";
	private static final String RSTICKBTN = "11";
	private static final String PSBTN = "12";
	private static final String TOUCHPADBTN = "13";
	private static final String RSTICKXAXIS = "z";
	private static final String RSTICKYAXIS = "rz";
	private static final String LSTICKXAXIS = "x";
	private static final String LSTICKYAXIS = "y";
	private static final String R2 = "ry";
	private static final String L2 = "rx";
	
    public Server() {
        super();
    }

    public void startListening(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started, waiting for client to connect...");

            clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            startListenerThread();
        } catch (IOException e) {
            System.err.println("I/O error when setting up server");
            e.printStackTrace();
        }
    }

    private void startListenerThread() {
        listenerThread = new Thread(() -> {
            while (running) {
                try {
                    String message = in.readLine();
                    if (message != null) {
                        //System.out.println("Incoming message: " + message);
                    	String[] values = message.split(" ");
                    	String btnName = values[0];
                    	float btnValue = Float.parseFloat(values[1]);
                    	//System.out.println(btName+" " + btValue);
                    	switch (btnName) {
                        case LSTICKXAXIS:
                            System.out.println(btnValue > 0f ? "DESTRA" + ": " + btnValue * 100 + "%" : "SINISTRA" + ": " + btnValue * 100 + "%");
                            break;
                        case RSTICKYAXIS:
                            System.out.println(btnValue > 0f ? "GIU" + ": " + btnValue * 100 + "%" : "SU" + ": " + btnValue * 100 + "%");
                            break;
                        case R2:
                            System.out.println("AVANTI" + ": " + btnValue * 100 + "%");
                            break;
                        case L2:
                            System.out.println("INDIETRO" + ": " + btnValue * 100 + "%");
                            break;
                        case O:
                        	System.out.println("BOOST: ON");
                        	break;
                        case SQUARE:
                        	System.out.println("ACTION: ON");
                        	break;
                        default:
                        	//DEBUG
                            System.out.println("Unknown button: " + btnName);
                    }

                    } else {
                        //stopConnection();
                    }
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error reading incoming message");
                        e.printStackTrace();
                        stopConnection();
                    }
                }
            }
        });
        listenerThread.start();
    }

    public String sendMessage(String msg) {
        if (clientSocket != null && clientSocket.isConnected()) {
            out.println(msg);
        } else {
            System.err.println("No client connected. Unable to send message.");
        }
        return msg;
    }

    public void stopConnection() {
        running = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
            System.out.println("Connection closed successfully");
        } catch (IOException e) {
            System.err.println("Error while closing connection");
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
		   Server server = new Server();
	        server.startListening(12345);
	        
    }
    
}
