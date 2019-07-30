package UI;

import Backend.PersonResult;
import Backend.ProductionResult;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;

/**
 * 4/20/2019
 * @author Tyree Mitchell
 *
 * 4/24/2019
 * @author Phillip Zubov, Buddy Godfrey
 * Added some adultfilterbool stuff.
 */
public class PersonPane extends DetailPane {
    PersonResult result;
    private JList<ProductionResult> prodList;
    private boolean adultFilterBool = false;

    public PersonPane(Window frame, PersonResult result, boolean adultFilterBool) {
        super(frame);
        this.result = result;
        this.adultFilterBool = adultFilterBool;
        this.result.setAdultFilter(this.adultFilterBool);

        constructUI();
        buildProductionPanel();
        buildTabs();
    }

    private void constructUI() {
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());

        //Create and place back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ButtonListener());
        headerPanel.add(backButton, BorderLayout.WEST);


        //Create and place details such as name and birth/death years
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();

        //Create a cool little placeholder image
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        ImageIcon image = new ImageIcon("Assets/Avatar.png");
        Image scaleImage = image.getImage();
        Image newImage = scaleImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        image = new ImageIcon(newImage);
        JLabel label = new JLabel("", image, JLabel.CENTER);
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(label, BorderLayout.CENTER);
        infoPanel.add(imagePanel, gridConstraints);

        this.add(infoPanel, BorderLayout.WEST);


        //Display name
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 1;
        JLabel nameLabel = new JLabel("<html><b><u>" + result.getName() + "</u></b></html>");
        infoPanel.add(nameLabel, gridConstraints);


        //Display Birth/Death years
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 2;
        String livedString = "Born: " + result.getBirthYear() + " - Death: " + result.getDeathYear();
        JLabel livedYears = new JLabel(livedString);
        infoPanel.add(livedYears, gridConstraints);


        //Now add a description box
        JTextArea descriptionText = new JTextArea();
        descriptionText.setOpaque(false);
        descriptionText.setText("Wow this person is really awesome");
        descriptionText.setEditable(false);
        this.add(descriptionText, BorderLayout.CENTER);

    }

    /**
     * Build the list of productions this person is involved in
     */
    private void buildProductionPanel() {
        JLabel prodLabel = new JLabel("Productions this person is involved in: ");
        headerPanel.add(prodLabel, BorderLayout.EAST);

        //Create a known for productions panel
        DefaultListModel<ProductionResult> listModel = new DefaultListModel();
        prodList = new JList(listModel);
        prodList.addListSelectionListener(new ProductionListener());

        scrollPane = new JScrollPane(prodList);
        scrollPane.setPreferredSize(new Dimension(400, 800));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        result.getKnownProductions();

        for(ProductionResult person : result.getProductions()) {
            listModel.addElement(person);
        }

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.EAST);
    }

    /**
     * Build the tabbed area which shows jobs and characters this person is associated with
     */
    private void buildTabs() {
        JTabbedPane tabPane = new JTabbedPane();

        JTextArea jobsArea = new JTextArea();
        jobsArea.setEditable(false);
        jobsArea.setText(result.jobsString());
        JScrollPane jobPanel = new JScrollPane(jobsArea);
        jobPanel.setPreferredSize(new Dimension(800, 100));
        jobPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTextArea charactersArea = new JTextArea();
        charactersArea.setEditable(false);
        charactersArea.setText(result.charactersString());
        JScrollPane charPanel = new JScrollPane(charactersArea);
        charPanel.setPreferredSize(new Dimension(800, 100));
        charPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);



        tabPane.add("Jobs", jobPanel);
        tabPane.add("Characters", charPanel);
        this.add(tabPane, BorderLayout.SOUTH);

    }


    /**
     * Generate a production pane when clicking a production
     */
    private class ProductionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!prodList.isSelectionEmpty() && !prodList.getValueIsAdjusting()) {
                ProductionResult result = prodList.getSelectedValue();
                DetailPane newProductionPane = new ProductionPane(windowFrame, result, adultFilterBool);
                windowFrame.pushScreen(newProductionPane);
            }
        }
    }
}
