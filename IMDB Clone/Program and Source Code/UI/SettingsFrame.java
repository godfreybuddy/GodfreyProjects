package UI;

import Backend.PersonResult;
import Backend.SQLServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Settings menu code for program. Able to change server that user is connecting to as well as turn on nsfw filter on
 * and off.
 *  4/22/2019
 * @author Phillip Zubov, Maksim Samoylov
 *
 */
public class SettingsFrame extends JFrame{

    private JLabel addressLabel,portLabel,nameLabel,usernameLabel,passwordLabel, connectedLabel;
    private JPanel changeConnectionPanel, addressPanel, portPanel, namePanel,userPanel, passPanel;
    private JTextField address,port,databaseName,username,password;
    private JCheckBox adultFilter;
    private JButton changeConnectionButton;
    private SQLServer newConnectionServer;


    /**
     * constructor for SettingsFrame.
     * @param server used for setting up new connection(so as not to declare a whole new SQLServer object)
     */
    public SettingsFrame(SQLServer server){
        newConnectionServer = server;
        new JFrame("Settings");
        changeConnectionPanel = new JPanel();
        GridLayout layout = new GridLayout(8,1);
        setLayout(layout);

        addressLabel = new JLabel("Server address: ");
        portLabel = new JLabel("Server port: ");
        nameLabel = new JLabel("Database name: ");
        usernameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");
        connectedLabel = new JLabel("Connected?: "+server.getConnected());
        connectedLabel.setHorizontalAlignment(SwingConstants.CENTER);

        address = new JTextField(server.getAddress());
        port = new JTextField(Integer.toString(server.getPort()));
        databaseName = new JTextField(server.getDatabase());
        username = new JTextField(server.getUser());
        password = new JTextField(server.getPass());


        addressPanel = new JPanel();
        portPanel = new JPanel();
        namePanel = new JPanel();
        userPanel = new JPanel();
        passPanel = new JPanel();


        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.X_AXIS));
        portPanel.setLayout(new BoxLayout(portPanel, BoxLayout.X_AXIS));
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.X_AXIS));

        changeConnectionButton = new JButton("Change Server");
        changeConnectionButton.addActionListener(changeConnection());

        adultFilter = new JCheckBox("Turn on NSFW filter");
        adultFilter.setHorizontalAlignment(JCheckBox.CENTER);
        adultFilter.addActionListener(turnOnAdultFilter());

        addressPanel.add(addressLabel);
        addressPanel.add(address);
        portPanel.add(portLabel);
        portPanel.add(port);
        namePanel.add(nameLabel);
        namePanel.add(databaseName);
        userPanel.add(usernameLabel);
        userPanel.add(username);
        passPanel.add(passwordLabel);
        passPanel.add(password);

        addressPanel.setBorder(new EmptyBorder(2,15,0,15));
        portPanel.setBorder(new EmptyBorder(2,15,0,15));
        namePanel.setBorder(new EmptyBorder(2,15,0,15));
        userPanel.setBorder(new EmptyBorder(2,15,0,15));
        passPanel.setBorder(new EmptyBorder(2,15,0,15));
        add(addressPanel);
        add(portPanel);
        add(namePanel);
        add(userPanel);
        add(passPanel);

        changeConnectionPanel.setBorder(new EmptyBorder(0,10,0,10));
        add(changeConnectionButton);
        add(connectedLabel);
        add(adultFilter);

        setSize(800,300);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        //setVisible(true);
    }

    /**
     * action listener for change connection button
     * @return actionlistener for changing connection button
     */
    private ActionListener changeConnection(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                /**
                 * Split and try to connect to new server using the input in the textfield. Catch any exceptions it
                 * encounters and create a dialog box that will warn the user that their input is invalid.
                 */
                try {
                    if( newConnectionServer.connectTo(address.getText(), Integer.parseInt(port.getText()),
                            databaseName.getText(), username.getText(), password.getText())) {
                        connectedLabel.setText("Connected?: True");

                    }

                } catch (Exception s) {
                    JDialog error = new JDialog(SettingsFrame.this, "Error!");
                    error.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    JPanel panel = new JPanel();
                    BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                    panel.setLayout(boxLayout);
                    panel.setBorder(new EmptyBorder(new Insets(15,20,15,20)));

                    JButton okButton = new JButton("OK");
                    JLabel errorLabel = new JLabel("Something went wrong!\n" +
                            " Make sure all fields are correct.");

                    //closes the window when ok button pressed
                    okButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            error.dispose();
                        }
                    });

                    okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panel.add(errorLabel);
                    panel.add(okButton);
                    error.add(panel);

                    error.setLocationRelativeTo(null);
                    error.pack();
                    error.setVisible(true);
                }

            }
        };
    }

    /**
     * action listener for adultFilter checkbox
     * @return ActionListener for the checkbox button to use
     */
    private ActionListener turnOnAdultFilter(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * set adultFilter bool to true if box checked
                 */
                if(adultFilter.isSelected()) {
                    adultFilter.setSelected(true);
                    try {
                        newConnectionServer.setAdultFilterBool(true);
                    } catch(Exception s) {
                        System.out.println("Adult filter when checked is throwing error.");
                    }
                }
                /**
                 * set adultFilter bool to false if box checked
                 */
                else{
                    adultFilter.setSelected(false);
                    try{
                        newConnectionServer.setAdultFilterBool(false);
                    }catch (Exception s){
                        System.out.println("Adult filter when not checked is throwing error.");
                    }
                }
            }
        };
    }
}
