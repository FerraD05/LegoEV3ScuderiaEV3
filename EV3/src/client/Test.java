package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Test {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Client c = new Client();
		
		c.startConnection("216.58.209.46", 80);
		c.sendMessage("tette");
		c.stopConnection();
		
		JFrame frame = new JFrame("Game Input Handler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not used in this example
            }

            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                switch (keyChar) {
                    case 'w':
                        System.out.println("Moving forward");
                        break;
                    case 'a':
                        System.out.println("Moving left");
                        break;
                    case 's':
                        System.out.println("Moving backward");
                        break;
                    case 'd':
                        System.out.println("Moving right");
                        break;
                    default:
                        System.out.println("Invalid input. Use WASD keys.");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not used in this example
            }
        });

        frame.setVisible(true);
	}
}