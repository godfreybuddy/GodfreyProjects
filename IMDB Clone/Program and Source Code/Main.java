import Backend.SQLServer;
import UI.Window;
import mdlaf.*;
import java.sql.SQLException;
import javax.swing.*;

/**
 * 474 SQL Server interacting program
 * By Maksim Samoylov, Tyree Mitchell, Phillip Zubov, Buddy Godfrey
 * Group 44
 */
public class Main {

    /**
     * Main method to just launch the program
     */
    public static void main( String[] args ) throws SQLException {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
        } catch(Exception ignored){}

        SQLServer server = new SQLServer();
        Window window = new Window(server);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800,600);
        window.setVisible(true);
        server.connectTo("71.63.48.66", 3306, "imdb", "program", "Yeehaw420$");
    }
}
