package client;

import javax.swing.*;
import java.awt.*;

public class CustomButtonLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private Color colorPressed = Color.GREEN;
    private Color colorReleased = Color.GRAY;
    private boolean pressed;
	private float value = 0f;
	protected float width;
	protected float height;
	
	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
		if(value>0) {
			pressed=true;
		}else {
			pressed=false;
		}
	}
    public CustomButtonLabel(String text, float width, float height) {
        super(text);
        this.width=width;
        this.height=height;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
        repaint(); // Repaint to update the appearance
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw button shape (customize based on your controller's buttons)
        if (pressed) {
            g2d.setColor(colorPressed);
        } else {
            g2d.setColor(colorReleased);
        }
        g2d.fillOval(0, 0, (int)width, (int)height); // Example: Circular button shape

        g2d.dispose();
    }
}
