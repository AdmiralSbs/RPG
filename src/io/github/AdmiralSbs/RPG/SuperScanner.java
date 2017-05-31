package io.github.AdmiralSbs.RPG;
import javax.swing.*;
import java.awt.event.*;

public class SuperScanner implements ActionListener{

   private JTextField systemIn;
   private volatile boolean ready = false;

   public SuperScanner(JTextField s) {
      systemIn = s;
      systemIn.addActionListener(this);
   }

   public String nextLine() {
      while (!ready) {}
      String in = systemIn.getText();
      systemIn.setText("");
      ready = false;
      return in;
   }
	
   public int nextInt() {
      while (!ready) {}
      String in = systemIn.getText();
      systemIn.setText("");
      int inty = -99;
      try {
         inty = Integer.parseInt(in);
      }
      catch (Exception e) {
         inty = -99;
      }
      if (inty == -1)
         inty = -99;
      ready = false;
      //System.out.println(inty);
      return inty;
   }
	
   public String next() {
      while (!ready) {}
      String in = systemIn.getText();
      systemIn.setText("");
      String[] ins = in.split(" ");
      ready = false;
      return ins[0];
   }
	
   public void actionPerformed(ActionEvent e) {
      ready = true;
      //System.out.println(ready);
      
   }
	
}