package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A modified version of TupleResult meant for holding various data about entertainment titles.
 * Author: Tyree Mitchell
 */
public class ProductionResult extends TupleResult {

    private String originalTitle;
    private boolean isAdult;
    private int startYear;
    private int endYear;
    private int runTime;


    private SQLServer server;

    private String genre;
    private float averageRating;
    private int numberVotes;

    private ArrayList<PersonResult> castCrew;
    private ArrayList<VersionResult> prodVersions;
    private ArrayList<EpisodeResult> episodes;


    ProductionResult(String id, String type, String primaryTitle, String originalTitle, int isAdult, int startYear,
                     int endYear, int runTime, SQLServer server) {
        super(primaryTitle, type, id);
        this.originalTitle = originalTitle;
        this.isAdult = (isAdult != 0);
        this.startYear = startYear;
        this.endYear = endYear;
        this.runTime = runTime;
        this.server = server;

        castCrew = new ArrayList<>();
        prodVersions = new ArrayList<>();
        episodes = new ArrayList<>();


        averageRating = 0;
        numberVotes = 0;
    }


    public String versionsString() {
        String returnString = "";
        for(VersionResult vResult : prodVersions)
            returnString += vResult.toString();

        return returnString;
    }

    public String episodesString() {
        String returnString = "";
        for(EpisodeResult epResult : episodes)
            returnString += epResult.toString();

        return returnString;
    }

    public void loadRatings() {
        ResultSet result = null;
        try {
            result = server.executeStatement("SELECT * FROM ratings WHERE prodID = " + "'" + getId() + "'" + ";");
            if(result.next()) {
                averageRating = result.getFloat("averageRating");
                numberVotes = result.getInt("numVotes");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public float getAverageRating() {
        return averageRating;
    }

    public int getNumberVotes() {
        return numberVotes;
    }

    /**
     * Get genre of the production
     */
    public void addGenre() {
        ResultSet result = null;
        try {
            result = server.executeStatement("SELECT genreID FROM productiongenre WHERE prodID = " + "'" + getId() + "'" + ";");
            if(result.next())
                genre = result.getString("genreID");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void loadEpisodes() {
        ResultSet result = null;
        try {
            result = server.executeStatement("SELECT * FROM episode WHERE parentProdID = " + "'" + getId() + "'" + ";");
            while(result.next()) {
                int seasonNumber = result.getInt("seasonNumber");
                int episodeNumber = result.getInt("episodeNumber");
                EpisodeResult newResult = new EpisodeResult(seasonNumber, episodeNumber);
                episodes.add(newResult);
            }
        } catch (SQLException e) {

        }
    }

    public void loadVersions() {
        ResultSet result = null;
        try {
            result = server.executeStatement("SELECT * FROM version WHERE prodID = " + "'" + getId() + "'" + ";");
            while(result.next()) {
                String name = result.getString("title");
                String id = result.getString("sequence");
                String regionID = result.getString("regionID");
                String languageID = result.getString("languageID");
                int isOriginal = result.getInt("isOriginal");
                String comments = result.getString("comments");
                VersionResult newResult = new VersionResult(id, "Version", name, regionID, languageID, isOriginal, comments);
                prodVersions.add(newResult);
            }
        } catch (SQLException e) {

        }
    }

    /**
     * Add various cast and crew members/characters to this production
     */
    public void addCastCrew() {
        ResultSet result = null;
        try {
            result = server.executeStatement("SELECT * FROM castandcrew INNER JOIN Person ON castandcrew.personID = person.personID WHERE castandcrew.prodID = " + "'" + getId() + "'" + ";");
            while(result.next()) {
                String name = result.getString("primaryName");
                String id = result.getString("personID");
                String jobID = result.getString("jobID");
                String characterPlayed = result.getString("characterPlayed");
                String jobTitle = result.getString("jobTitle");
                int birthYear = result.getInt("birthYear");
                int deathYear = result.getInt("deathYear");
                PersonResult newResult = new PersonResult(id, "Person", name, jobTitle, characterPlayed, jobID, birthYear, deathYear, server);
                castCrew.add(newResult);
            }
        } catch (SQLException e) {

        }
    }

    public String getGenre() {
        if(genre == null) {
            addGenre();
        }
        return genre;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getRunTime() {
        return runTime;
    }


    public ArrayList<PersonResult> getCastCrew() {
        return castCrew;
    }
}
