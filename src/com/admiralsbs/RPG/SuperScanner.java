package com.admiralsbs.RPG;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("StatementWithEmptyBody")
public class SuperScanner implements ActionListener {

    private final JTextField systemIn;
    private volatile boolean ready = false;
    private volatile String code;
    private int waiting = 0;

    public SuperScanner(JTextField s) {
        systemIn = s;
        systemIn.addActionListener(this);
    }

    public String nextLine() {
        waiting = 1;
        systemIn.setEnabled(true);
        while (!ready) {
        }
        String in = systemIn.getText();
        systemIn.setText("");
        ready = false;
        waiting = 0;
        return in;
    }

    public int nextInt() {
        waiting = 1;
        systemIn.setEnabled(true);
        while (!ready) {
        }
        String in = systemIn.getText();
        systemIn.setText("");
        int inty;
        try {
            inty = Integer.parseInt(in);
        } catch (Exception e) {
            inty = -99;
        }
        if (inty == -1)
            inty = -99;
        ready = false;
        waiting = 0;
        // System.out.println(inty);
        return inty;
    }

    public String next() {
        waiting = 1;
        systemIn.setEnabled(true);
        while (!ready) {
        }
        String in = systemIn.getText();
        systemIn.setText("");
        String[] ins = in.split(" ");
        ready = false;
        waiting = 0;
        return ins[0];
    }

    public String getCode(String... args) {
        waiting = 2;
        systemIn.setEnabled(false);
        waiter:
        while (true) {
            while (!ready) {
            }
            for (String arg : args) {
                if (code.equals(arg)) {
                    // System.out.println(code + " matched");
                    break waiter;
                }
            }
            ready = false;
        }
        // System.out.println("Broke");
        ready = false;
        waiting = 0;
        return code;
    }

    public void sendCode(String s) {
        if (waiting == 2) {
            code = s;
            ready = true;
            // System.out.println(s);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (waiting == 1)
            ready = true;
    }

}