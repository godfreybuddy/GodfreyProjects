package UI;

import Backend.PersonResult;
import Backend.ProductionResult;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ProductionPane extends DetailPane {
    private ProductionResult result;
    private JList<PersonResult> list;
    private DefaultListModel<PersonResult> listModel;
    private boolean adultFilterBool = false;

    public ProductionPane(Window frame, ProductionResult result, boolean adultFilterBool) {
        super(frame);
        this.result = result;
        this.adultFilterBool = adultFilterBool;

        //Construct entire pane
        constructUI();
        buildCastAndCrewPanel();
        buildTabs();
    }

    private void constructUI() {


        JLabel titleLabel = new JLabel();

        JLabel adultRating;
        JLabel startEndYears;
        GridBagConstraints gridConstraints;

        JLabel runTime;
        JLabel genreLabel;

        //Build various labels
        if(result.getName() != null)
            titleLabel.setText("<html><b><u>" + result.getName() + "</u></b></html>");


        String yearString = "||Years running: ";
        yearString += result.getStartYear() + " - " + result.getEndYear();
        startEndYears = new JLabel(yearString);


        runTime = new JLabel("||Run time: " + result.getRunTime());
        genreLabel = new JLabel("||Genre: " + result.getGenre());


        String aRating = "";
        if(result.isAdult())
            aRating = "Adult Rating: Mature audiences only";
        else
            aRating = "Adult Rating: Everyone";

        adultRating = new JLabel(aRating);

        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());



        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());

        result.loadRatings();
        JLabel ratingLabel = new JLabel();
        if(result.getAverageRating() > 0) {
            ratingLabel.setText("|| Rating: " + result.getAverageRating() + "/10 " +
                    "Number of Votes: " + result.getNumberVotes());
        }


        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ButtonListener());
        headerPanel.add(backButton, BorderLayout.WEST);

        //Build the layout of the content page
        gridConstraints = new GridBagConstraints();
        gridConstraints.insets = new Insets(1, 1, 2, 2);

        gridConstraints.gridx = 5;
        gridConstraints.gridy = 0;
        gridConstraints.gridwidth = 6;
        titlePanel.add(titleLabel, gridConstraints);

        gridConstraints.gridx = 0;
        gridConstraints.gridy = 1;
        gridConstraints.gridwidth = 2;
        titlePanel.add(adultRating, gridConstraints);

        gridConstraints.gridx = 3;
        gridConstraints.gridy = 1;
        gridConstraints.gridwidth = 3;
        titlePanel.add(startEndYears, gridConstraints);

        gridConstraints.gridx = 7;
        gridConstraints.gridy = 1;
        gridConstraints.gridwidth = 3;
        titlePanel.add(runTime, gridConstraints);

        gridConstraints.gridx = 10;
        gridConstraints.gridy = 1;
        gridConstraints.gridwidth = 3;
        titlePanel.add(genreLabel, gridConstraints);

        gridConstraints.gridx = 5;
        gridConstraints.gridy = 2;
        gridConstraints.gridwidth = 3;
        titlePanel.add(ratingLabel, gridConstraints);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        //Create a description box
        JTextArea descriptionText = new JTextArea();
        descriptionText.setOpaque(false);
        if(result.getAverageRating() >= 7) {
            descriptionText.setText("What a great production.");
        } else {
            descriptionText.setText("Mediocre at best.");
        }

        descriptionText.setEditable(false);
        this.add(descriptionText, BorderLayout.CENTER);
        //Finally add it to the main frame.
        this.add(headerPanel, BorderLayout.NORTH);
    }

    private void buildCastAndCrewPanel() {
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        list.addListSelectionListener(new PersonListener());
        scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(400, 800));
        result.addCastCrew();

        for(PersonResult person : result.getCastCrew()) {
            listModel.addElement(person);
        }

        this.add(scrollPane, BorderLayout.EAST);
    }

    /**
     * Build the tabbed area which shows jobs and characters this person is associated with
     */
    private void buildTabs() {
        result.loadEpisodes();
        result.loadVersions();
        JTabbedPane tabPane = new JTabbedPane();

        JTextArea versionText = new JTextArea();
        versionText.setEditable(false);
        versionText.setText(result.versionsString());
        JScrollPane versionPanel = new JScrollPane(versionText);
        versionPanel.setPreferredSize(new Dimension(800, 100));
        versionPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTextArea seasonText = new JTextArea();
        seasonText.setEditable(false);
        seasonText.setText(result.episodesString());
        JScrollPane epPanel = new JScrollPane(seasonText);
        epPanel.setPreferredSize(new Dimension(800, 100));
        epPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);



        tabPane.add("Versions", versionPanel);
        tabPane.add("Season Information", epPanel);
        this.add(tabPane, BorderLayout.SOUTH);

    }

    /**
     * Generate a person pane when clicking a person
     */
    private class PersonListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!list.isSelectionEmpty() && !list.getValueIsAdjusting()) {
                PersonResult result = list.getSelectedValue();
                DetailPane newPersonPane = new PersonPane(windowFrame, result, result.getAdultFilter());
                windowFrame.pushScreen(newPersonPane);
            }
        }
    }
}
