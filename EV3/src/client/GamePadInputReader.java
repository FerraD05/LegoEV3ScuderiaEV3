package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

/**
 * The GamePadInputReader class reads input from a gamepad controller and sends the input data to a client.
 * It also updates a status panel with the current state of the gamepad buttons and axes.
 */
public class GamePadInputReader {
    private static final float DEADZONE = 0.17f; // Define a deadzone threshold
    private static final long DEBOUNCE_PERIOD_MS = 16; // Define the debounce period in milliseconds
    private GamePadStatusPanel statusPanel; // Reference to GamePadStatusPanel
    private static final long POLLING_INTERVAL_MS = 50; // Define the polling interval in milliseconds

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
    
    
    // Map to track the last event time for each button
    private Map<String, Long> lastEventTimeMap = new HashMap<>();

    /**
     * Constructor for GamePadInputReader.
     *
     * @param statusPanel the GamePadStatusPanel to update with gamepad status
     */
    public GamePadInputReader(GamePadStatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    /**
     * Starts reading input from the specified controller.
     *
     * @param controller the gamepad controller to read input from
     * @param ip the IP address to connect to
     * @param port the port to connect to
     * @throws UnknownHostException if the host is unknown
     * @throws IOException if an I/O error occurs
     */
    public void startReading(Controller controller, String ip, int port) throws UnknownHostException, IOException {
        Client c = new Client();
        c.startConnection(ip, port);
        float prevZvalue = 1f;
        float prevRZvalue = 1f;
        float prevXvalue = 1f;
        float prevYvalue = 1f;

        while (true) {
            long pollStartTime = System.currentTimeMillis();
            controller.poll();

            // Get the controller's event queue
            EventQueue queue = controller.getEventQueue();

            // Create an event object for the underlying plugin to populate
            Event event = new Event();

            // For each object in the queue
            while (queue.getNextEvent(event)) {
                StringBuffer buffer = new StringBuffer(controller.getName()).append(": ");
                Component comp = event.getComponent();
                buffer.append(comp.getIdentifier().getName()).append(" changed to ");
                float value = event.getValue();
                String componentName = comp.getIdentifier().getName();

                // Get the current time
                long currentTime = System.currentTimeMillis();
                long lastEventTime = lastEventTimeMap.getOrDefault(componentName, 0L);

                // Apply deadzone to analog components and debouncing to non-analog buttons
                if (comp.isAnalog()) {
                    if (Math.abs(value) < DEADZONE) {
                        value = 0.0f;
                    }
                } else {
                    if (currentTime - lastEventTime < DEBOUNCE_PERIOD_MS) {
                        continue; // Skip this event if within debounce period
                    }
                    lastEventTimeMap.put(componentName, currentTime);
                }

                // Comparison with previous values
                switch (componentName) {
                    case "z":
                        if (prevZvalue == value && prevZvalue == 0.0f) {
                            continue;
                        }
                        prevZvalue = value;
                        break;
                    case "rz":
                        if (prevRZvalue == value && prevRZvalue == 0.0f) {
                            continue;
                        }
                        prevRZvalue = value;
                        break;
                    case "x":
                        if (prevXvalue == value && prevXvalue == 0.0f) {
                            continue;
                        }
                        prevXvalue = value;
                        break;
                    case "y":
                        if (prevYvalue == value && prevYvalue == 0.0f) {
                            continue;
                        }
                        prevYvalue = value;
                        break;
                    default:
                        break;
                }

                
                switch (componentName) {
                case LSTICKXAXIS:
                    break;
                case RSTICKYAXIS:
                    break;
                case R2:
                    value=(value+1)/2;
                    break;
                case L2:
                	value=(value+1)/2;
                    break;
                case O:
                	break;
                case SQUARE:
                	break;
                default:
                	//DEBUG
                    //System.out.println("Unknown button: " + btnName);
            }
                
                // Update status panel with button status
                statusPanel.updateButtonStatus(componentName, value);

                // DEBUG
                System.out.println(buffer.toString());

                c.sendMessage(componentName + " " + value);

                // Calculate how long the polling loop took and sleep for the remaining time
                long pollDuration = System.currentTimeMillis() - pollStartTime;
                long sleepTime = POLLING_INTERVAL_MS - pollDuration;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * The main method to start the GamePadInputReader application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final GamePadStatusPanel statusPanel = new GamePadStatusPanel();
        GamePadInputReader reader = new GamePadInputReader(statusPanel);

        // Get the available controllers
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        if (controllers.length == 0) {
            System.out.println("Found no controllers.");
            System.exit(0);
        }
        // Display available controllers to the user
        System.out.println("Available controllers:");
        for (int i = 0; i < controllers.length; i++) {
            System.out.println((i + 1) + ". " + controllers[i].getName());
        }
        // Prompt the user to choose a controller
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of the controller you want to use: ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > controllers.length) {
            System.out.println("Invalid choice. Exiting...");
            scanner.close();
            System.exit(0);
        }
        Controller selectedController = controllers[choice - 1];
        System.out.println("Using controller: " + selectedController.getName());

        // Prompt the user to enter the IP address and port
        System.out.print("Enter the IP address to connect to: ");
        String ip = scanner.next();
        System.out.print("Enter the port to connect to: ");
        int port = scanner.nextInt();
        scanner.close();

        // Initialize frame
        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
			    JFrame frame = new JFrame("Gamepad Status");
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.getContentPane().add(statusPanel);
			    frame.pack();
			    frame.setLocationRelativeTo(null);
			    frame.setSize(700, 500); // Set the desired size of the frame
			    frame.setVisible(true);
			}
		});
       
        // Start the reading loop with the selected controller
        try {
            reader.startReading(selectedController, ip, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
}
