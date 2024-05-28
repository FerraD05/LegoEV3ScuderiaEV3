package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class CustomAnalogStickLabel extends CustomAnalogButtonLabel{
	private static final long serialVersionUID = 1L;
	private float valueX = 0f;
	private float valueY = 0f;
	
	public CustomAnalogStickLabel(String text, float width, float height) {
		super(text, width, height);
	}

	public float getValueX() {
		return valueX;
	}

	public void setValueX(float valueX) {
		this.valueX = valueX;
		repaint(); // Repaint to update the appearance
	}
	public float getValueY() {
		return valueY;
	}

	public void setValueY(float valueY) {
		this.valueY = valueY;
		repaint(); // Repaint to update the appearance
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        
        // Draw the outer circle
        g2d.setColor(Color.GRAY);
        g2d.fillOval(0, 0, (int) width, (int) height);
        
        // Calculate the position of the inner circle based on valueX and valueY
        int centerX = (int) (width / 2);
        int centerY = (int) (height / 2);
        int radius = (int) (width / 2);
        int innerRadius = radius / 2; // Inner circle radius is half of the outer circle radius

        // Invert the Y value
        int innerCircleX = (int) (centerX + valueX * radius) - innerRadius; // Adjust to center the smaller circle
        int innerCircleY = (int) (centerY + valueY * radius) - innerRadius; // Invert and adjust to center the smaller circle
        
        
        // Ensure the inner circle stays within the bounds of the outer circle
        int distanceFromCenter = (int) Math.sqrt(Math.pow(innerCircleX + innerRadius - centerX, 2) + Math.pow(innerCircleY + innerRadius - centerY, 2));
        if (distanceFromCenter > radius - innerRadius) {
            double angle = Math.atan2(innerCircleY + innerRadius - centerY, innerCircleX + innerRadius - centerX);
            innerCircleX = (int) (centerX + (radius - innerRadius) * Math.cos(angle)) - innerRadius;
            innerCircleY = (int) (centerY + (radius - innerRadius) * Math.sin(angle)) - innerRadius;
        }
        
        
        // Draw button shape (customize based on your controller's buttons)
        int mappedXValue = (int) ((Math.abs(valueX)) * 255f); // Maps -1-1 to 0-255
        int mappedYValue = (int) ((Math.abs(valueY)) * 255f); // Maps -1-1 to 0-255
        int result;
        if(mappedXValue+mappedYValue>255) {
        	result=255;
        }else {
        	result=mappedXValue+mappedYValue;
        }
        
        g2d.setColor(new Color(0,result,0));
        if(valueX==0f && valueY==0f) {
        	g2d.setColor(Color.DARK_GRAY);
        }
        g2d.fillOval(innerCircleX, innerCircleY, innerRadius * 2, innerRadius * 2);
        
        g2d.dispose();
    }
	
}
