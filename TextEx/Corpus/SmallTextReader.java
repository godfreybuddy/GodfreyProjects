package corpus;

import java.awt.Font;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 * Little reader for whatever text is inputted.
 * 
 * @author Anthony Thellaeche
 */
public class SmallTextReader
{
  /**
   * 
   * @param args blank
   * @throws IOException IOException
   * @throws BadLocationException BadLocationException
   */
  public static void main(String[] args) throws IOException, BadLocationException
  {
    // testing out the text method
    Text text = new Text("resources/frankenstein.htm");
    System.out.println(text.getFullName());
    System.out.println(text.getShortName());
    System.out.println(text.getLineCount());
    System.out.println(text.getFilePath());

    // testing out what it actually looks like
    JFrame frame = new JFrame();
    frame.setSize(800, 600);
    JTextArea area = new JTextArea();
    JScrollPane scroll = new JScrollPane(area);
    area.setFont(new Font("monospaced", Font.PLAIN, 14));
    area.setText(text.getText());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(scroll);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
