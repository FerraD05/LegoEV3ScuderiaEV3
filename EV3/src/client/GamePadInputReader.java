package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class GamePadInputReader {
    private static final float DEADZONE = 0.17f; // Define a deadzone threshold
    private GamePadStatusPanel statusPanel; // Reference to GamePadStatusPanel

    public GamePadInputReader(GamePadStatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public void startReading(Controller controller) throws UnknownHostException, IOException {
        Client c = new Client();
        c.startConnection("127.0.0.1", 12345);
        float prevZvalue=1f;
        float prevRZvalue=1f;
        float prevXvalue=1f;
        float prevYvalue=1f;
        while(true) {
        	controller.poll();

            /* Get the controllers event queue */
            EventQueue queue = controller.getEventQueue();

            /* Create an event object for the underlying plugin to populate */
            Event event = new Event();
            
            /* For each object in the queue */
            while (queue.getNextEvent(event)) {
                StringBuffer buffer = new StringBuffer(controller.getName()).append(": ");
                Component comp = event.getComponent();
                buffer.append(comp.getIdentifier().getName()).append(" changed to ");
                float value = event.getValue();

                // Apply deadzone to analog components
                if (comp.isAnalog()) {
                    if (Math.abs(value) < DEADZONE) {
                        value = 0.0f;
                    }
                }

                // Comparison with previous values
                switch (comp.getIdentifier().getName()) {
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

                // Update status panel with button status
                statusPanel.updateButtonStatus(comp.getIdentifier().getName(), value);

                // DEBUG
                //System.out.println(buffer.toString());
                
                
                
                c.sendMessage(comp.getIdentifier().getName() + " " + value);
            }
        }
    }

    public static void main(String[] args) {
        GamePadStatusPanel statusPanel = new GamePadStatusPanel();
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
        System.out.print("Enter the number of the controller you want to use: ");
        Scanner scanner = new Scanner(System.in);
        /*int choice = scanner.nextInt();
        scanner.close();
        if (choice < 1 || choice > controllers.length) {
            System.out.println("Invalid choice. Exiting...");
            System.exit(0);
        }
        */
        int choice = 21;
        Controller selectedController = controllers[choice - 1];
        System.out.println("Using controller: " + selectedController.getName());
        
        // Initialize frame
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gamepad Status");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(statusPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setSize(700, 500); // Set the desired size of the frame
            frame.setVisible(true);
        });
        
        // Start the reading loop with the selected controller
        try {
            reader.startReading(selectedController);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
