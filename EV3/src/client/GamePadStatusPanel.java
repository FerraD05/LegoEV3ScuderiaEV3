package client;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GamePadStatusPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private Map<String, JLabel> buttonLabels;

    public GamePadStatusPanel() {
        setLayout(null); // Use null layout for absolute positioning
        buttonLabels = new HashMap<>();
        initComponents();
    }

    private void initComponents() {
        // Add labels for each gamepad button with fixed positions and sizes
    	addButtonLabel("0", 500, 250, 50, 50); // Square button
        addButtonLabel("1", 550, 300, 50, 50); // X button
        addButtonLabel("2", 600, 250, 50, 50); // O button
        addButtonLabel("3", 550, 200, 50, 50); // Triangle button
        addAnalogStickLabel("xy", 100, 350, 100, 100); // Left stick
        addAnalogStickLabel("zrz", 400, 350, 100, 100); // Right stick
        addAnalogButtonLabel("rx", 50, 100, 80, 80); // L2 button
        addAnalogButtonLabel("ry", 450, 100, 80, 80); // R2 button
        
        // Set panel properties
        setBorder(BorderFactory.createTitledBorder("Gamepad Status"));
    }

    private void addButtonLabel(String buttonName, int x, int y, int width, int height) {
        CustomButtonLabel label = new CustomButtonLabel(buttonName, width, height);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBounds(x, y, width, height); // Set position and size
        buttonLabels.put(buttonName, label);
        add(label);
    }

    private void addAnalogButtonLabel(String buttonName, int x, int y, int width, int height) {
        CustomAnalogButtonLabel label = new CustomAnalogButtonLabel(buttonName, width, height);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBounds(x, y, width, height); // Set position and size
        buttonLabels.put(buttonName, label);
        add(label);
    }

    private void addAnalogStickLabel(String buttonName, int x, int y, int width, int height) {
        CustomAnalogStickLabel label = new CustomAnalogStickLabel(buttonName, width, height);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBounds(x, y, width, height); // Set position and size
        buttonLabels.put(buttonName, label);
        add(label);
    }

    public void updateButtonStatus(String buttonName, float value) {
        JLabel label;
        switch (buttonName) {
            case "x":
                label = buttonLabels.get("xy");
                if (label != null) {
                    ((CustomAnalogStickLabel) label).setValueX(value);
                    label.setText(buttonName + ": " + value);
                    repaint(); // Repaint the panel after updating the label
                }
                break;
            case "y":
                label = buttonLabels.get("xy");
                if (label != null) {
                    ((CustomAnalogStickLabel) label).setValueY(value);
                    label.setText(buttonName + ": " + value);
                    repaint(); // Repaint the panel after updating the label
                }
                break;
            case "z":
                label = buttonLabels.get("zrz");
                if (label != null) {
                    ((CustomAnalogStickLabel) label).setValueX(value);
                    label.setText(buttonName + ": " + value);
                    repaint(); // Repaint the panel after updating the label
                }
                break;
            case "rz":
                label = buttonLabels.get("zrz");
                if (label != null) {
                    ((CustomAnalogStickLabel) label).setValueY(value);
                    label.setText(buttonName + ": " + value);
                    repaint(); // Repaint the panel after updating the label
                }
                break;
            default:
                label = buttonLabels.get(buttonName);
                if (label != null) {
                    ((CustomButtonLabel) label).setValue(value);
                    label.setText(buttonName + ": " + value);
                    repaint(); // Repaint the panel after updating the label
                }
                break;
        }
    }
}
