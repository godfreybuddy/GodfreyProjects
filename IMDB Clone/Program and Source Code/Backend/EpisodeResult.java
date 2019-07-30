package Backend;

public class EpisodeResult extends TupleResult {
    private int seasonNumber;
    private int episodeNumber;

    public EpisodeResult(int seasonNumber, int episodeNumber) {
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String toString() {
        return "Season: " + seasonNumber + " Episode: " + episodeNumber + "\n";
    }
}
