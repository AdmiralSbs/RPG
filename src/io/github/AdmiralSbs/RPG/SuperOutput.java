package io.github.AdmiralSbs.RPG;
   import javax.swing.*;
	
public class SuperOutput extends JPanel {
   
	private static final long serialVersionUID = 3L;
	private JTextArea source;
   
      public SuperOutput(JTextArea source) {
         this.source = source;
      }
   	
      public void print(String s) {
         source.append(s);
      }
   	
      public void print(char c) {
         source.append(c + "");
      }
   	
      public void println(String s) {
         source.append(s + "\n");
      }
   	
      public void println(char c) {
         source.append(c + "\n");
      }
   	
      public void println() {
         source.append("\n");
      }
   	
      public void println(int i) {
         source.append(Integer.toString(i) + "\n");
      }
   	
      public void clear() {
         source.setText("");
      }
   }