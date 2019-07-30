package Backend;

import com.mysql.jdbc.CommunicationsException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//import java.sql.*;

/**
 * Interact with physical server with this class
 * @author Maskim Samoylov, Buddy Godfrey, Tyree Mitchell, Phillip Zubov
 */
public class SQLServer {

    private Connection conn;
    private MysqlDataSource dataSource;
    private Statement stmt;
    private ResultSet result;
    private boolean adultFilterBool = false;

    String address = "71.63.48.66";
    int port = 3306;
    String database = "IMDB";
    String user = "program";
    String pass = "DBClone";

    boolean connected = false;

    /**
     * no args constructor
     */
    public SQLServer(){
    }

    /**
     * set up the connection to a server
     * @param address
     * @throws SQLException
     */
    public boolean connectTo(String address, int port, String database, String user, String pass) throws SQLException{
        this.address = address;
        this.port = port;
        this.database = database;
        this.user = user;
        this.pass = pass;

        dataSource = new MysqlDataSource();
        dataSource.setPort(port); // could also be 3306
        dataSource.setUseSSL(false);
        dataSource.setDatabaseName(database);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
        dataSource.setServerName(address); // "mysql.cs.jmu.edu" or "71.63.48.66" or "10.0.0.154"

        System.out.println("Connecting...");
        try {
            conn = (Connection) dataSource.getConnection();
        }
        catch (Exception com){
            System.out.println("Could not connect");
            return false;
        }
        System.out.println("Connected\n");
        return connected = true;
    }

    /**
     * perform a search query on the server
     * @return list of the tuples from query
     * @throws SQLException
     */
    public ArrayList<TupleResult> simpleRead(String query) throws SQLException {

        System.out.println( "Building Unique Set . . ." );
        stmt = (Statement) conn.createStatement();
        result = stmt.executeQuery( "SELECT * FROM production WHERE primaryTitle LIKE '%"+query+"%' LIMIT 50;" );

        ArrayList<TupleResult> list = new ArrayList<>();
        while ( result.next() ) {
            String ID = result.getString( "prodID" );
            String description = result.getString( "primaryTitle");
            TupleResult result = new TupleResult(description, "Movie", ID) {
            };
            list.add(result);
        }

        System.out.println("Built "+list.size()+" items\n");
        return list;
    }

    /**
     * A modified method for obtaining various entertainment results.
     * @param query - The search statement.
     * @return - A set of 50 elements that best match a user's search result.
     * @throws SQLException
     */
    public ArrayList<ProductionResult> productionRead(String query) throws SQLException {

        if (!connected) {
            ArrayList<ProductionResult> list = new ArrayList<>();
            list.add(new ProductionResult("", "error",
                    "Error no connection, please check connection or try connecting to different IMDB server",
                    "Error", 0,0,0,0,null));
            return list;
        }
        System.out.println( "Building Unique Set . . ." );
        stmt = (Statement) conn.createStatement();
        result = stmt.executeQuery( "SELECT * FROM production WHERE primaryTitle LIKE '%"+query+"%' LIMIT 50;" );

        ArrayList<ProductionResult> list = new ArrayList<>();
        while ( result.next() ) {
                String type = "Movie";
                String ID = result.getString("prodID");
                String description = result.getString("primaryTitle");
                String originalTitle = result.getString("originalTitle");
                int isAdult = result.getInt("isAdult");
                int startYear = result.getInt("startYear");
                int endYear = result.getInt("endYear");
                int runTime = result.getInt("runTime");
                ProductionResult result = new ProductionResult(ID, type, description, originalTitle,
                        isAdult, startYear, endYear, runTime, this);
                if(!adultFilterBool)
                    list.add(result);
                else if(adultFilterBool && isAdult == 0)
                    list.add(result);


        }

        System.out.println("Built "+list.size()+" items\n");
        return list;
    }

    /**
     * finds all the people who have search term in their name
     * @param query search term
     * @return list of people objects
     * @throws SQLException
     */
    public ArrayList<PersonResult> personRead(String query) throws SQLException {
        if(!connected) {
            return new ArrayList<>();
        }

        stmt = (Statement) conn.createStatement();
        result = stmt.executeQuery("SELECT * FROM Person WHERE primaryName LIKE '%"+query+"%' LIMIT 50;" );

        ArrayList<PersonResult> list = new ArrayList<>();
        while(result.next()) {
            String name = result.getString("primaryName");
            String id = result.getString("personID");
            int birthYear = result.getInt("birthYear");
            int deathYear = result.getInt("deathYear");
            PersonResult newResult = new PersonResult(id, "Person", name, null,
                    null, null, birthYear, deathYear, this);
            list.add(newResult);
        }

        return list;
    }

    /**
     * Used by outside classes to execute custom Queries
     * @param executableStatement
     * @return
     * @throws SQLException
     */
    public ResultSet executeStatement(String executableStatement) throws SQLException {
        stmt = (Statement) conn.createStatement();
        return stmt.executeQuery(executableStatement);
    }

    /**
     * sets the adult filter to the param
     * @param x
     */
    public void setAdultFilterBool(boolean x){
        adultFilterBool = x;
    }

    /**
     * get adultfilter boolean
     * @return adultFilterBool
     */
    public boolean getAdultFilterBool(){
        return adultFilterBool;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public boolean getConnected() {
        return connected;
    }
}
