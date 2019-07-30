package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.io.UnsupportedEncodingException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A custom content pane for the reader tabs innards.
 * 
 * @author Tyree Mitchell
 *
 */
public class CustomTabContent extends JScrollPane
{

  private static final long serialVersionUID = 1L;
  private JTextArea reader;
  private JPanel txtContainer;

  /**
   * Construct the tab content.
   * 
   * @param content
   *          - The initial text content of the tab.
   */
  public CustomTabContent(String content)
  {
    reader = new JTextArea(content);
    txtContainer = new JPanel();
    reader.setFont(new Font("monospaced", Font.PLAIN, 12));
    txtContainer.add(reader);
    setPreferredSize(new Dimension(780, 300));
    setViewportView(reader);
    add(txtContainer);
  }

  /**
   * Scroll to the given line number.
   * 
   * @param lineNumber
   *          - The line number of the search phrase.
   */
  public void updateCaret(int lineNumber)
  {
    reader.setCaretPosition(
        reader.getDocument().getDefaultRootElement().getElement(lineNumber).getStartOffset());
  }

  /**
   * Set the text of the tab content.
   * 
   * @param newText
   *          - The text to put into the tab
   */
  public void setText(String newText)
  {
    try
    {
      reader.setText(new String(newText.getBytes(), "UTF-8"));
    }
    catch (UnsupportedEncodingException e)
    {
      System.out.println("Issue setting text to reader. Encoding Issue.");
      e.printStackTrace();
    }
  }
}
