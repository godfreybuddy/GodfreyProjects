package Backend;

/**
 * a search result (tuple) from a query
 * @author Maksim Samoylov
 */
public abstract class TupleResult {

    private String name;
    private String type;
    private String id;


    TupleResult() {

    }

    /**
     * consructor to store information
     * @param name name/title to display in list
     * @param type what table its from
     * @param id to search again for populating more data
     */
    TupleResult(String name, String type, String id){
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    /**
     * to string function
     * @return
     */
    public String toString(){
        return "["+type+"] "+" ID: "+id+"  "+name;
    }
}
