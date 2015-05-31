import javax.swing.JFrame;
import java.util.*;
import java.io.*;
public class Driver
{
   public static void main(String[]args) throws Exception
   {
      JFrame frame = new JFrame("GIF Game");
      frame.setSize(768,614);
      frame.setLocation(0,0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
      frame.setContentPane(new GamePanel());
      frame.setVisible(true);
   }
}
