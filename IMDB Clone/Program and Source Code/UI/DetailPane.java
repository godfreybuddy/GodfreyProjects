package UI;

import Backend.PersonResult;
import Backend.ProductionResult;
import Backend.TupleResult;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Main class for building and displaying a rich content pane for search results
 * Author: Tyree Mitchell
 */
public class DetailPane extends JPanel {
    Window windowFrame;

    JPanel headerPanel;
    JPanel detailPanel;
    JScrollPane scrollPane;

    JTabbedPane extrasPane;



    /**
     * Create a new panel.
     * @param frame
     */
    public DetailPane(Window frame) {
        setLayout(new BorderLayout());
        windowFrame = frame;
        setVisible(false);
    }

    /**
     * Button listener
     * To add more button listeners just copy the if block and modify getActionCommand
     */
    public class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Back")) {
                //windowFrame.mainScreen();
                windowFrame.goBack();
            }
        }
    }




}
