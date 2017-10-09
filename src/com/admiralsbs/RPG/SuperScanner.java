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
        systemIn.setEnabled(true);
        systemIn.requestFocusInWindow();
        ready = false;
        waiting = 1;
        while (!ready) {
        }
        String in = systemIn.getText();
        systemIn.setText("");
        waiting = 0;
        return in;
    }

    public int nextInt() {
        systemIn.setEnabled(true);
        systemIn.requestFocusInWindow();
        int inty;
        while (true) {
            ready = false;
            waiting = 1;
            while (!ready) {
            }
            String in = systemIn.getText();
            systemIn.setText("");
            try {
                inty = Integer.parseInt(in);
            } catch (Exception e) {
                continue;
            }
            break;
        }

        waiting = 0;
        // System.out.println(inty);
        return inty;
    }

    public int nextInt(int min, int max) {
        systemIn.setEnabled(true);
        systemIn.requestFocusInWindow();
        int inty;
        while (true) {
            ready = false;
            waiting = 1;

            while (!ready) {
            }
            String in = systemIn.getText();
            systemIn.setText("");

            try {
                inty = Integer.parseInt(in);
            } catch (Exception e) {
                continue;
            }
            if (inty >= min && inty <= max)
                break;
        }

        waiting = 0;
        // System.out.println(inty);
        return inty;
    }

    public int nextInt(int min, int max, int... exceptions) {
        systemIn.setEnabled(true);
        systemIn.requestFocusInWindow();
        int inty;

        while (true) {
            ready = false;
            waiting = 1;
            while (!ready) {
            }
            String in = systemIn.getText();
            systemIn.setText("");

            try {
                inty = Integer.parseInt(in);
            } catch (Exception e) {
                continue;
            }
            for (int num : exceptions) {
                if (inty == num)
                    continue;
            }
            if (inty >= min && inty <= max)
                break;
        }
        waiting = 0;
        // System.out.println(inty);
        return inty;
    }


    public String next() {
        systemIn.setEnabled(true);
        systemIn.requestFocusInWindow();
        waiting = 1;
        ready = false;
        while (!ready) {
        }
        String in = systemIn.getText();
        systemIn.setText("");
        String[] ins = in.split(" ");
        waiting = 0;
        return ins[0];
    }

    public String getCode(String... args) {
        waiting = 2;
        systemIn.setEnabled(false);
        waiter:
        while (true) {
            ready = false;
            while (!ready) {
            }
            for (String arg : args) {
                if (code.equals(arg)) {
                    // System.out.println(code + " matched");
                    break waiter;
                }
            }
        }
        // System.out.println("Broke");
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