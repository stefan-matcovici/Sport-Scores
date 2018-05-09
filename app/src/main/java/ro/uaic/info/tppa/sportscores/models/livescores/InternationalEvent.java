package ro.uaic.info.tppa.sportscores.models.livescores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"subscribed"})
public class InternationalEvent {
    String awayScore;
    String awayTeam;
    String homeScore;
    String homeTeam;
    String id;
    String scoreLink;

    String minute;

    public boolean subscribed;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

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

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
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
