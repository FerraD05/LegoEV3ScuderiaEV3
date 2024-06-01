package server;

import java.io.BufferedReader;
import java.io.IOException;

//import lejos.hardware.motor.EV3LargeRegulatedMotor;
//simport lejos.hardware.port.MotorPort;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread listenerThread;
    private volatile boolean running = true;
    //private static EV3LargeRegulatedMotor leftM = new EV3LargeRegulatedMotor(MotorPort.B);	// getting the left motor
    //private static EV3LargeRegulatedMotor rightM = new EV3LargeRegulatedMotor(MotorPort.A);	// getting the right motor
    //private int topSpeed = 1000;	// setting the top speed of the motor
    
    public static final String SQUARE = "0";
	public static final String X = "1";
	public static final String O = "2";
	public static final String TRIANGLE = "3";
	public static final String L1 = "4";
	public static final String R1 = "5";
	public static final String ARROWS = "pov";
	public static final String SHARE = "8";
	public static final String OPTION = "9";
	public static final String LSTICKBTN = "10";
	public static final String RSTICKBTN = "11";
	public static final String PSBTN = "12";
	public static final String TOUCHPADBTN = "13";
	public static final String RSTICKXAXIS = "z";
	public static final String RSTICKYAXIS = "rz";
	public static final String LSTICKXAXIS = "x";
	public static final String LSTICKYAXIS = "y";
	public static final String R2 = "ry";
	public static final String L2 = "rx";
	
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
        listenerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (running) {
				    try {
				        String message = in.readLine();
				        if (message != null) {
				            String[] values = message.split(" ");
				            String btnName = values[0];
				            float btnValue = Float.parseFloat(values[1]);
				            String direction = "";
				            String action = "";
				            switch (btnName) {
				                case LSTICKXAXIS:
				                    direction = btnValue > 0f ? "DESTRA" : "SINISTRA";
				                    break;
				                case RSTICKYAXIS:
				                    direction = btnValue > 0f ? "GIU" : "SU";
				                    break;
				                case R2:
				                    direction = "AVANTI";
				                    action = "forward";
				                    break;
				                case L2:
				                    direction = "INDIETRO";
				                    action = "backward";
				                    break;
				                case O:
				                    continue;
				                case SQUARE:
				                    continue;
				                default:
				                    continue;
				            }
				            System.out.println(direction + ": " + btnValue * 100 + "%");
				            /*if (!action.isEmpty()) {
				                // setting the speed of the motors
				                leftM.setSpeed(topSpeed * Math.abs(btnValue));
				                rightM.setSpeed(topSpeed * Math.abs(btnValue));
				                
				                if(direction.equals("DESTRA")) {
				                	rightM.setSpeed(rightM.getSpeed()*Math.abs(btnValue));
				                }else 
				                	if(direction.equals("SINISTRA")){
				                	leftM.setSpeed(leftM.getSpeed()*Math.abs(btnValue));
				                }
				                
				                // moving the motors
				                if (action.equals("forward")) {
				                    leftM.forward();
				                    rightM.forward();
				                } else {
				                    leftM.backward();
				                    rightM.backward();
				                }
				            }*/
				        }
				    } catch (IOException e) {
				        if (running) {
				            System.err.println("Error reading incoming message");
				            e.printStackTrace();
				            stopConnection();
				        }
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
    	int portaAscolto = 6969;
		   Server server = new Server();
	        server.startListening(portaAscolto);
	        
    }
    
}
