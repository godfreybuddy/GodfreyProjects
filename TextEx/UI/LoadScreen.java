package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import corpus.Corpus;
import corpus.Text;

/**
 * LoadScreen components.
 * 
 * @author Phillip Zubov
 * @version 11/1/2018
 *
 */
public class LoadScreen extends JDialog
{

  private JTextField txtFileField;
  private JTextField titleField;
  private JTextField shortTitleField;
  private JButton browse;
  private JButton process;
  private JButton done;
  private JTextArea txtArea;
  private JComboBox txtFileTypeCombo;
  private JLabel tFile;
  private JLabel tFileType;
  private JLabel ttle;
  private JLabel sttle;
  private JPanel fieldContainer;
  private JPanel displayContainer;
  private JPanel shortTitlePanel;
  private JPanel mainpanel;
  public static int corpusIndex = 0;
  private Text text;
  private Corpus corpus = Corpus.getInstance();
  static private File fileChooserDirectory = new File(System.getProperty("user.home"));

  private String[] fileTypeSelection = {"Project Gutenberg File", "HTML Line",
      "Rich Text Format(.rtf)"};

  private Border lineBorder = BorderFactory.createLineBorder(Color.gray);

  /**
   * Assign values/properties to LoadScreen elements.
   * 
   * @param parent
   *          used to set the Dialog owner
   */
  public LoadScreen(JFrame parent)
  {
    super(parent, "Load Texts", Dialog.DEFAULT_MODALITY_TYPE);
    setLayout(new BorderLayout());
    setSize(570, 500);

    browse = new JButton("Browse");
    process = new JButton("Process");
    done = new JButton("Done");

    txtFileField = new JTextField(37);

    fieldContainer = new JPanel(); // top panel with all input fields
    displayContainer = new JPanel();// bottom panel with textArea and done/process buttons

    fieldContainer.setLayout(new BoxLayout(fieldContainer, BoxLayout.Y_AXIS));
    displayContainer.setLayout(new BorderLayout());

    txtFileField.getDocument().addDocumentListener(new TxtFieldListener());

    KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(new KeyEventDispatcher()
        {
          @Override
          public boolean dispatchKeyEvent(KeyEvent e)
          {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER)
            {
              if (!(txtFileField.getText().equals("")))
              {
                try
                {
                  text = new Text(txtFileField.getText());

                  if (fileTypeSelection.equals("Rich Text Format(.rtf)"))
                  {
                    titleField.setText("Unknown");
                    shortTitleField.setText("Unkown");
                  }
                  else
                  {
                    try
                    {
                      titleField.setText(text.getFullName());
                      shortTitleField.setText(text.getShortName());
                    }
                    catch (NullPointerException f)
                    {
                      
                    }
                  }

                  process.setEnabled(true);
                }
                catch (FileNotFoundException f)
                {
                  JOptionPane.showMessageDialog(parent,
                      "File Path " + txtFileField.getText() + " Cannot Be Found",
                      "File Input Error", JOptionPane.ERROR_MESSAGE);
                  return false;
                }
                catch (IOException f)
                {
                  JOptionPane.showMessageDialog(parent,
                      "File Path " + txtFileField.getText() + " Cannot Be Found",
                      "File Input Error", JOptionPane.ERROR_MESSAGE);
                  return false;
                }
                catch (BadLocationException f)
                {
                  JOptionPane.showMessageDialog(parent,
                      "File Path " + txtFileField.getText() + " Cannot Be Found",
                      "File Input Error", JOptionPane.ERROR_MESSAGE);
                  return false;
                }
              }
            }

            return false;
          }
        });

    browse.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(fileChooserDirectory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fc.setFileFilter(filter);
        int result = fc.showOpenDialog(mainpanel);

        if (result == JFileChooser.APPROVE_OPTION)
        {
          fileChooserDirectory = fc.getSelectedFile();
          txtFileField.setText(fileChooserDirectory.getAbsolutePath());

          if (fileTypeSelection.equals("Rich Text Format(.rtf)"))
          {
            titleField.setText("Unknown");
            shortTitleField.setText("Unkown");
          }
          else
          {
            try
            {
              text = new Text(txtFileField.getText());

              titleField.setText(text.getFullName());
              shortTitleField.setText(text.getShortName());
            }
            catch (NullPointerException e)
            {

            }
            catch (FileNotFoundException f)
            {
              JOptionPane.showMessageDialog(parent,
                  "File Path " + txtFileField.getText() + " Cannot Be Found", "File Input Error",
                  JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException f)
            {
              JOptionPane.showMessageDialog(parent,
                  "File Path " + txtFileField.getText() + " Cannot Be Found", "File Input Error",
                  JOptionPane.ERROR_MESSAGE);
            }
            catch (BadLocationException f)
            {
              JOptionPane.showMessageDialog(parent,
                  "File Path " + txtFileField.getText() + " Cannot Be Found",
                  "File Input Error", JOptionPane.ERROR_MESSAGE);
            }
          }
          process.setEnabled(true);
        }
      }
    });

    process.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent arg0)
      {

        try
        {
          
          if (corpus.getByFilePath(txtFileField.getText()) == null)
          {
            text.processText();
            
            // add corpus to the arrayList
            corpus.add(text);

            String source = txtFileField.getText();
            
            String title = titleField.getText().trim().equals("") 
                ? "Unknown" : titleField.getText().trim();
            String shortTitle = shortTitleField.getText().trim().equals("") 
                ? "Unknown" : shortTitleField.getText().trim();
            text.setFullName(title);
            text.setShortName(shortTitle);

            int txtSize = text.getLineCount();
            int txtNum = corpusIndex;
            int txtWordToken = text.getWordTokens();
            int totalWordTypes = corpus.getTotalWordTypes();
            int totalWordTokens = corpus.getTotalWordTokens();

            txtFileField.setText("");
            titleField.setText("");
            shortTitleField.setText("");

            txtArea.setText("Source: " + source + "\nTitle: " + title + "\nShort title: "
                + shortTitle + "\nText size: " + txtSize + "\nText number: " + txtNum
                + "\nText word tokens: " + txtWordToken + "\nTotal word types: " + totalWordTypes
                + "\nTotal word tokens: " + totalWordTokens);
            corpusIndex++;
            StartScreen.setLoadedTexts(corpus.toString());
          }

        }
        catch (FileNotFoundException e)
        {
          JOptionPane.showMessageDialog(parent,
              "File Path " + txtFileField.getText() + " Cannot Be Found", "File Input Error",
              JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException f)
        {
          // TODO
          JOptionPane.showMessageDialog(parent,
              "File Path " + txtFileField.getText() + " Cannot Be Found", "File Input Error",
              JOptionPane.ERROR_MESSAGE);
        }
        catch (BadLocationException f)
        {
          JOptionPane.showMessageDialog(parent,
              "File Path " + txtFileField.getText() + " Cannot Be Found",
              "File Input Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    done.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        hideDialog();
      }

    });

    JPanel p1 = new JPanel();
    tFile = new JLabel("Text File: ");
    p1.add(tFile, BorderLayout.WEST);

    Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 10);

    txtFileField.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
    p1.add(txtFileField, BorderLayout.CENTER);
    p1.add(browse, BorderLayout.EAST);
    fieldContainer.add(p1);

    JPanel p2 = new JPanel();
    tFileType = new JLabel("Text File Type: ");
    p2.add(tFileType, BorderLayout.WEST);

    txtFileTypeCombo = new JComboBox(fileTypeSelection);
    txtFileTypeCombo.setPreferredSize(new Dimension(470, 20));

    p2.add(txtFileTypeCombo, BorderLayout.EAST);
    fieldContainer.add(p2);

    JPanel p3 = new JPanel();
    JPanel titlePanel = new JPanel();
    titleField = new JTextField(26);
    ttle = new JLabel("Title: ");
    titlePanel.add(ttle, BorderLayout.WEST);
    titlePanel.add(titleField, BorderLayout.EAST);

    shortTitlePanel = new JPanel();
    sttle = new JLabel("Short Title: ");
    shortTitleField = new JTextField(13);
    shortTitlePanel.add(sttle, BorderLayout.WEST);
    shortTitlePanel.add(shortTitleField, BorderLayout.EAST);

    p3.add(titlePanel, BorderLayout.WEST);
    p3.add(shortTitlePanel, BorderLayout.EAST);
    fieldContainer.add(p3);

    JPanel processButtonPanel = new JPanel();
    processButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    process.setEnabled(false);
    processButtonPanel.add(process);
    displayContainer.add(processButtonPanel, BorderLayout.WEST);

    JPanel doneButtonPanel = new JPanel();
    doneButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    doneButtonPanel.add(done);
    displayContainer.add(doneButtonPanel, BorderLayout.EAST);

    txtArea = new JTextArea(20, 50);
    displayContainer.add(txtArea, BorderLayout.SOUTH);

    mainpanel = new JPanel();
    mainpanel.add(fieldContainer, BorderLayout.NORTH);
    mainpanel.add(displayContainer, BorderLayout.CENTER);

    add(mainpanel);
    setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);
    setVisible(true);
  }

  /**
   * Used to set process button to enabled or disabled.
   * 
   * @author Phillip Zubov
   */
  private class TxtFieldListener implements DocumentListener
  {

    /**
     * If txtFileField has text, disable process button.
     */
    private void updateVisibility()
    {
      if (!(txtFileField.getText().trim().equals("")))
      {
        process.setEnabled(true);
      }
      else
      {
        process.setEnabled(false);
      }
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
      // TODO Auto-generated method stub
      updateVisibility();
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
      // TODO Auto-generated method stub
      updateVisibility();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
      updateVisibility();
    }
  }

  /**
   * Set this JDialog visibility to false.
   */
  private void hideDialog()
  {
    this.setVisible(false);
  }

}
