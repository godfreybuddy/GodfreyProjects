package ui;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import corpus.Corpus;

/**
 * 
 * @author Jake Adkins
 */
public class ClearScreen extends JDialog 
{
  private Container contentPane;

  private JLabel msg;
  private JButton cancel, ok;

  private JPanel cancelp, okp, buttonPane;

  private StartScreen parentFrame;
  /**
   * Constructor for the clear screen.
   * 
   * @param parent - the JFrame to steal focus from
   */
  public ClearScreen(StartScreen parent) 
  {
    super(parent, "Confirm Clear", true);
    this.parentFrame = parent;
    this.setSize(300, 200);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

    contentPane = this.getContentPane();
    contentPane.setLayout(new BorderLayout());

    msg = new JLabel("       Remove all loaded texts?");
    msg.setIcon(new ImageIcon("resources/java.png"));
    msg.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

    cancel = new JButton("Cancel");
    cancel.setPreferredSize(new Dimension(90, 30));
    cancel.addActionListener(new ClearScreenListener());
    cancelp = new JPanel();
    cancelp.add(cancel);

    ok = new JButton("OK");
    ok.setPreferredSize(new Dimension(90, 30));
    ok.addActionListener(new ClearScreenListener());
    okp = new JPanel();
    okp.add(ok);

    buttonPane = new JPanel();
    buttonPane.add(okp);
    buttonPane.add(cancelp);

    contentPane.add(msg, BorderLayout.CENTER);
    contentPane.add(buttonPane, BorderLayout.SOUTH);

    this.setLocationRelativeTo(null);
    this.setVisible(true);

  }

  /**
   * Closes the clear screen instance.
   */
  private void exit() 
  {
    this.setVisible(false);
    this.dispose();
  }

  /**
   * Private action listener for the clear screen buttons.
   */
  private class ClearScreenListener implements ActionListener 
  {
    /**
     * Process button actions.
     * @param e The event source.
     */
    public void actionPerformed(ActionEvent e) 
    {
      if (e.getSource().equals(cancel)) 
      {
        exit();
      }

      if (e.getSource().equals(ok)) 
      {
        Corpus corpus = Corpus.getInstance();
        corpus.removeAll();
        corpus.getIndexer().getIndex().clear();       
        parentFrame.clear();
        exit();
      }
    }
  }
}
