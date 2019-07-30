package Backend;

public class VersionResult extends TupleResult {
    private String regionID;
    private String languageID;
    private int isOriginal;
    private String comments;
    public VersionResult(String id, String type, String name, String regionID, String languageID,
                         int isOriginal, String comments) {
        super(name, type, id);
        this.regionID = regionID;
        this.languageID = languageID;
        this.isOriginal = isOriginal;
        this.comments = comments;
    }

    @Override
    public String toString() {
        String returnString = "";
        if(regionID != null)
            returnString += " Region: " + regionID;

        if(languageID != null)
            returnString += " Language: " + languageID;

            if (isOriginal == 1)
                returnString += " Original";
            else if(isOriginal == 0)
                returnString += " Not Original";

        if(comments != null)
            returnString += " Comments: " + comments;


        returnString += "\n";

        return returnString;
    }
}
