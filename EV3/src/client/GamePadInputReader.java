package client;


import java.io.IOException;
import java.net.UnknownHostException;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;



public class GamePadInputReader {
	private static final float DEADZONE = 0.17f; // Define a deadzone threshold
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Client c = new Client();
		
		c.startConnection("127.0.0.1", 12345);		
		while (true) {
            /* Get the available controllers */
            Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
            if (controllers.length == 0) {
                System.out.println("Found no controllers.");
                System.exit(0);
            }

            for (Controller controller : controllers) {
                /* Remember to poll each one */
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
                    
                    if(0f==value) {
                    	continue;
                    }
                    
                    /* Apply deadzone for analog components */
                    if (comp.isAnalog()) {
                    	if (Math.abs(value) < DEADZONE) {
                            value = 0;
                            continue;
                        }
                        buffer.append(value);
                    } else {
                        if (value == 1.0f) {
                            buffer.append("On");
                        } else {
                            buffer.append("Off");
                        }
                    }
                    System.out.println(buffer.toString());
                    
                    c.sendMessage(comp.getIdentifier().getName() +" "+ value);
                    
                }
            }

            /* Sleep for 20 milliseconds, so it doesn't thrash the system. */
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
		//c.stopConnection();
	}
}