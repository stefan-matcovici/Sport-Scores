package ro.uaic.info.tppa.sportscores.models.livescores;

public class InternationalEvent {
    String awayScore;
    String awayTeam;
    String homeScore;
    String homeTeam;
    String id;
    String scoreLink;
    Header header;

    public String getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScoreLink() {
        return scoreLink;
    }

    public void setScoreLink(String scoreLink) {
        this.scoreLink = scoreLink;
    }

    @Override
    public String toString() {
        return "InternationalEvent{" +
                "awayScore='" + awayScore + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeScore='" + homeScore + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", id='" + id + '\'' +
                ", scoreLink='" + scoreLink + '\'' +
                '}';
    }
}
