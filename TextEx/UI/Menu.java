package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import corpus.Corpus;

/**
 * Drop down menu located at the top left of the start screen.
 * 
 * @author Tyree Mitchell
 */
public class Menu extends JMenuBar
{
  private JMenu textsMenu;
  private JMenuItem loadItem;
  private JMenuItem clearItem;
  private JMenuItem summaryItem;
  private StartScreen frame;
  private LoadScreen loadScreen;

  /**
   * Adds the 3 menu items to the menu with their respective ActionListener.
   * 
   * @param frame
   *          | frame the menu is added to
   */
  public Menu(StartScreen frame)
  {
    textsMenu = new JMenu("Texts");
    loadItem = new JMenuItem("Load");
    clearItem = new JMenuItem("Clear");
    summaryItem = new JMenuItem("Summary");

    textsMenu.add(loadItem);
    textsMenu.add(clearItem);
    textsMenu.add(summaryItem);

    this.frame = frame;
    add(textsMenu);

    handleButtons();
  }

  /**
   * adds an ActionListener to each menu item.
   */
  private void handleButtons()
  {
    loadItem.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        if (loadScreen == null)
        {
          loadScreen = new LoadScreen(frame);
        }
        else
        {
          loadScreen.setVisible(true);
        }
      }
    });

    clearItem.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        new ClearScreen(frame);
      }
    });

    summaryItem.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        String message = "No texts are currently loaded";
        if (Corpus.getInstance().getNumberOfTexts() > 0)
        {
          message = new String();
          for (int i = 0; i < Corpus.getInstance().getNumberOfTexts(); i++)
          {
            message += String.format(
                "Text number %d:\n    Source file: %s\n"
                    + "    Title: %s (%s)\n    Size: %d lines\n",
                i, Corpus.getInstance().get(i).getFilePath(),
                Corpus.getInstance().get(i).getFullName(),
                Corpus.getInstance().get(i).getShortName(),
                Corpus.getInstance().get(i).getLineCount());
          }
          
          message += String.format("\n\nTotal word types:  %d\n" + "Total word tokens %d\n",
              Corpus.getInstance().getTotalWordTypes(), Corpus.getInstance().getTotalWordTokens());
        }

        JOptionPane.showMessageDialog(frame, message, "Texts Summary",
            JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/java.png"));
      }
    });
  }
}
