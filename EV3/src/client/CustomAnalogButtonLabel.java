package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class CustomAnalogButtonLabel extends CustomButtonLabel{
	private static final long serialVersionUID = 1L;
	private float value = -1f;

	
	public CustomAnalogButtonLabel(String text, float width, float height) {
		super(text, width, height);
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
		repaint(); // Repaint to update the appearance
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw button shape (customize based on your controller's buttons)
        int mappedValue = (int) ((value + 1) * 127.5f); // Maps -1-1 to 0-255
        g2d.setColor(new Color(0,mappedValue,0));
        if(value==-1f) {
        	g2d.setColor(Color.GRAY);
        }
        
        g2d.fillOval(0, 0, (int)width, (int)height); // Example: Circular button shape

        g2d.dispose();
    }

}
