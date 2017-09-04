package io.github.AdmiralSbs.RPG;

import javax.swing.*;
import java.awt.*;

public class RPGui extends JPanel {

    // Declare subcomponents
    private static final long serialVersionUID = 2L;
    private JPanel displayPanel;
    private JPanel IOPanel;
    private JTextArea displayOut;
    private JTextField input;
    private JTextArea printOut;

    // Declare layout, instantiate and add subcomponents;
    public RPGui() {
        setLayout(new FlowLayout());
        // setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        displayPanel = new JPanel();
        displayPanel.setLayout(new FlowLayout());
        IOPanel = new JPanel();
        IOPanel.setLayout(new BoxLayout(IOPanel, BoxLayout.Y_AXIS));
        displayOut = new JTextArea(40, 50);
        input = new JTextField("", 5);
        printOut = new JTextArea(39, 50);
        displayOut.setFont(new Font("Courier", displayOut.getFont().getStyle(), displayOut.getFont().getSize()));
        displayOut.setEditable(false);
        printOut.setEditable(false);
        displayPanel.add(displayOut);
        IOPanel.add(printOut);
        IOPanel.add(input);
        add(displayPanel);
        add(IOPanel);
        /*
		 * add(displayOut); add(printOut); add(input);
		 */

    }

    public void setSuperScanner(SuperScanner k) {
        input.addActionListener(k);
    }

    public JTextArea getPrintOut() {
        return printOut;
    }

    public JTextArea getDisplayOut() {
        return displayOut;
    }

    public JTextField getInput() {
        return input;
    }

}