package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import corpus.Corpus;
import corpus.Text;
import indexing.Searcher;
import indexing.Posting;

/**
 * The Main Screen of the application.
 * 
 * @author Tyree Mitchell
 */

public class StartScreen extends JFrame
{
  private static final long serialVersionUID = 1L;
  private static JLabel loadedLabel;
  private JLabel searchLabel;
  private JLabel linesLabel;
  private JTextField searchField;
  private JList<Posting> hitList;
  private JScrollPane hitScroll;
  private JButton searchButton, clearButton, deleteCurrent, deleteAll;
  private JTabbedPane readerPane;
  private JPanel labelContainer, startContainer, buttonsPanel, fullPanel;
  private GridBagConstraints gridConstraints;
  private Menu dropMenu;
  private Searcher searcher;
  private DefaultListModel<Posting> listModel;
  private HitlistListener hitListener;
  private long start = 0;

  /**
   * Initializes components, and the sections of the main screen.
   */
  public StartScreen()
  {

    setLayout(new BorderLayout());
    startContainer = new JPanel();
    startContainer.setLayout(new BorderLayout());

    fullPanel = new JPanel();
    fullPanel.setLayout(new BorderLayout());

    buttonsPanel = new JPanel();

    labelContainer = new JPanel();
    labelContainer.setLayout(new GridBagLayout());
    gridConstraints = new GridBagConstraints();

    hitScroll = new JScrollPane();
    listModel = new DefaultListModel<Posting>();
    hitList = new JList<Posting>(listModel);
    hitListener = new HitlistListener();
    hitList.addListSelectionListener(hitListener);
    hitList.setValueIsAdjusting(true);

    readerPane = new JTabbedPane();
    readerPane.addChangeListener(new TabListener());

    deleteCurrent = new JButton("Delete Current Reader");
    deleteAll = new JButton("Delete All Readers");
    deleteCurrent.addActionListener(new ButtonListener());
    deleteAll.addActionListener(new ButtonListener());

    createSearch();
    createHitListReaders();

    deleteCurrent.setEnabled(false);
    deleteAll.setEnabled(false);
    buttonsPanel.add(deleteCurrent);
    buttonsPanel.add(deleteAll);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setPreferredSize(new Dimension(800, 700));

    dropMenu = new Menu(this);

    add(dropMenu, BorderLayout.NORTH);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);

    fullPanel.add(labelContainer, BorderLayout.NORTH);
    fullPanel.add(startContainer, BorderLayout.CENTER);
    fullPanel.add(buttonsPanel, BorderLayout.SOUTH);
    add(fullPanel, BorderLayout.CENTER);

    searcher = new Searcher(Corpus.getInstance().getIndexer());
    this.setTitle("TextEx");
  }

  /**
   * Create search bar, buttons, and search information.
   */
  private void createSearch()
  {
    gridConstraints.insets = new Insets(2, 5, 2, 5);
    loadedLabel = new JLabel("Loaded Texts: None");
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 0;
    gridConstraints.gridwidth = 4;
    labelContainer.add(loadedLabel, gridConstraints);

    gridConstraints.gridwidth = 1;
    searchLabel = new JLabel("Search: ");
    gridConstraints.anchor = GridBagConstraints.WEST;
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 1;
    labelContainer.add(searchLabel, gridConstraints);

    searchField = new JTextField(40);
    searchField.getDocument().addDocumentListener(new SearchListener());
    gridConstraints.weightx = 1;
    gridConstraints.gridx = 1;
    gridConstraints.gridy = 1;
    gridConstraints.gridwidth = 5;
    labelContainer.add(searchField, gridConstraints);

    searchButton = new JButton("Search");
    searchButton.addActionListener(new ButtonListener());
    gridConstraints.weightx = 0;
    gridConstraints.gridx = 12;
    gridConstraints.gridy = 1;
    gridConstraints.gridwidth = 1;
    labelContainer.add(searchButton, gridConstraints);
    searchButton.setEnabled(false);

    clearButton = new JButton("Clear");
    clearButton.addActionListener(new ButtonListener());
    gridConstraints.gridx = 13;
    gridConstraints.gridy = 1;
    labelContainer.add(clearButton, gridConstraints);
    clearButton.setEnabled(false);

    linesLabel = new JLabel("Lines Found: None");
    gridConstraints.gridx = 0;
    gridConstraints.gridy = 3;
    gridConstraints.gridwidth = 4;
    labelContainer.add(linesLabel, gridConstraints);
  }

  /**
   * Create the hitlist section, and the tabbed reader section.
   */
  private void createHitListReaders()
  {
    // Create the hitlist
    hitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    hitScroll.setViewportView(hitList);
    // hitScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    hitList.setLayoutOrientation(JList.VERTICAL);
    hitList.setFont(new Font("monospaced", Font.PLAIN, 12));
    startContainer.add(hitScroll, BorderLayout.NORTH);

    // readerScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    readerPane.addTab("Empty", createTab("", 0));
    readerPane.addTab("+", createTab("", 0));
    startContainer.add(readerPane, BorderLayout.SOUTH);
  }

  /**
   * Populates the hit list.
   */
  private void populateHitList()
  {
    // Completely refresh the hitlist
    hitList.removeListSelectionListener(hitListener);
    hitList.clearSelection();
    hitList.removeAll();

    if (searcher.getSearchResults() != null)
    {
      ArrayList<Posting> posts = searcher.getSearchResults();
      listModel = new DefaultListModel<Posting>();

      for (int i = 0; i < posts.size(); i++)
      {
        // System.out.println(i);
        listModel.addElement(posts.get(i));
      }
      long finish = System.currentTimeMillis();
      double secondsElapsed = ((finish - start) / 1000.0);
      System.out.println("Finished entire search and hitlist generation: " + (finish - start)
          + "ms or " + secondsElapsed + " seconds ...waiting on Swing");
      hitList.setModel(listModel);
      hitList.addListSelectionListener(hitListener);
    }
  }

  /**
   * Creates container of text for a new tab and returns the container.
   * 
   * @param readerText
   *          - text that goes inside the reader.
   * @param lineNumber
   *          - The line in which the search appears
   * @return the container for a new tab.
   */
  public CustomTabContent createTab(String readerText, int lineNumber)
  {
    CustomTabContent tabContent = new CustomTabContent(readerText);
    tabContent.updateCaret(lineNumber);

    return tabContent;
  }

  /**
   * Replace currently selected reader's title and text with selected hit.
   * 
   * @param readerTitle
   *          - title of reader
   * @param lineNumber
   *          - The line in which the hit appears in the text
   * @throws IOException
   *           - If there was any issue with reading the text string
   */
  public void modifyCurrentReader(String readerTitle, int lineNumber) throws IOException
  {
    int currentSelected = readerPane.getSelectedIndex();

    Text currentText = Corpus.getInstance().getByShortName(readerTitle);

    int localLine = 0;
    StringBuilder strBuild = new StringBuilder();

    BufferedReader reader = new BufferedReader(new StringReader(currentText.getText()));
    String nextLine;

    while ((nextLine = reader.readLine()) != null)
    {
      if (localLine == lineNumber)
      {
        strBuild.append(String.format("> %5d  %s \n", localLine, nextLine));
      }
      else
      {
        strBuild.append(String.format("  %5d  %s \n", localLine, nextLine));
      }
      localLine++;
    }

    reader.close();
    currentSelected = readerPane.getSelectedIndex();

    readerPane.setTitleAt(currentSelected, readerTitle);
    CustomTabContent tabContent = (CustomTabContent) readerPane.getSelectedComponent();
    tabContent.setText(strBuild.toString());
    tabContent.updateCaret(lineNumber);
  }

  /**
   * Sets the loaded texts label.
   * 
   * @param texts
   *          - String to set label to
   */
  public static void setLoadedTexts(String texts)
  {
    loadedLabel.setText("Loaded Texts: " + Corpus.getInstance().toString());
  }

  /**
   * Clears all loaded texts, indices and searches.
   */
  public void clear()
  {
    // TODO Clear all loaded texts, indices and searches.
    searchField.setText("");
    hitList.removeListSelectionListener(hitListener);
    hitList.clearSelection();
    listModel = new DefaultListModel<Posting>();
    hitList.removeAll();
    hitList.setModel(listModel);
    hitList.addListSelectionListener(hitListener);
    linesLabel.setText("Lines Found: None");
    loadedLabel.setText("Loaded Texts: None");
    LoadScreen.corpusIndex = 0;
  }

  /**
   * Class that adds button functionality to tabs.
   * 
   * @author Tyree Mitchell
   *
   */
  private class TabListener implements ChangeListener
  {

    @Override
    public void stateChanged(ChangeEvent arg0)
    {
      int nextPlace = readerPane.indexOfTab("+");
      // If haven't reached max tab limit, add new tab
      if (readerPane.getSelectedIndex() == readerPane.indexOfTab("+"))
      {
        readerPane.setTitleAt(nextPlace, "Empty");
        readerPane.addTab("+", createTab("", 0));

        nextPlace = readerPane.indexOfTab("+");
        // If we have more than one tab
        if (nextPlace > 1)
        {
          deleteCurrent.setEnabled(true);
          deleteAll.setEnabled(true);
        }
      }
    }
  }

  /**
   * Implements all of the buttons for the main screen.
   * 
   * @author Tyree Mitchell
   */
  private class ButtonListener implements ActionListener
  {

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
      if (arg0.getActionCommand().equals("Delete Current Reader"))
      {
        int indexToRemove = readerPane.getSelectedIndex();
        if (indexToRemove - 1 >= 0)
        {
          readerPane.setSelectedIndex(readerPane.getSelectedIndex() - 1);
        }
        else
        {
          readerPane.setSelectedIndex(0);
        }
        readerPane.remove(indexToRemove);
        int currentAddIndex = readerPane.indexOfTab("+");
        // If we have less than 2 readers, disable the delete buttons
        if (currentAddIndex < 2)
        {
          deleteCurrent.setEnabled(false);
          deleteAll.setEnabled(false);
        }
      }

      if (arg0.getActionCommand().equals("Clear"))
      {
        listModel.clear();
        searchField.setText("");
        linesLabel.setText("Lines Found: None");
      }

      if (arg0.getActionCommand().equals("Delete All Readers"))
      {
        int currentAddIndex = readerPane.indexOfTab("+");
        readerPane.setSelectedIndex(0);
        // If we have 2 or more readers delete all readers before the "+" tab
        while (currentAddIndex > 1)
        {
          readerPane.remove(currentAddIndex - 1);
          currentAddIndex = readerPane.indexOfTab("+");
        }
        // If we have less than 2 readers, disable the delete buttons
        if (currentAddIndex < 2)
        {
          deleteCurrent.setEnabled(false);
          deleteAll.setEnabled(false);
        }
      }

      if (arg0.getActionCommand().equals("Search"))
      {
        start = System.currentTimeMillis();
        // System.out.println("Search");
        linesLabel.setText("Lines Found: None");
        searcher.resetSearch();
        searcher.buildSearchPostings(searchField.getText());
        // searcher.resetSearch();
        if ((searcher.getSearchResults() != null))
        {
          populateHitList();
          linesLabel.setText("Lines Found: " + searcher.getSearchResults().size());
        }
      }
    }

  }

  /**
   * Handles search field changes, and enables/disables search and clear buttons.
   * 
   * @author Tyree Mitchell
   *
   */
  private class SearchListener implements DocumentListener
  {

    /**
     * If searchfield has text enable buttons, otherwise disable buttons.
     */
    private void updateVisibility()
    {
      if (!searchField.getText().trim().equals(""))
      {
        searchButton.setEnabled(true);
        clearButton.setEnabled(true);
      }
      else
      {
        searchButton.setEnabled(false);
        clearButton.setEnabled(false);
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
      // TODO Auto-generated method stub
      updateVisibility();
    }

  }

  /**
   * Handles hitlist selections.
   * 
   * @author Phillip Zubov
   *
   */
  private class HitlistListener implements ListSelectionListener
  {

    @Override
    public void valueChanged(ListSelectionEvent arg0)
    {
      // If the hit list isn't empty, and also if this is the last click event(prevent double click)
      if (!hitList.isSelectionEmpty() && !hitList.getValueIsAdjusting())
      {
        Posting selectedHit = hitList.getSelectedValue();

        try
        {
          modifyCurrentReader(selectedHit.getText().getShortName(), selectedHit.getLineNumber());
        }
        catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
}
