package ro.uaic.info.tppa.sportscores.models.sportsdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    @JsonProperty("idEvent")
    String id;

    @JsonProperty("strHomeTeam")
    String homeTeam;

    @JsonProperty("strAwayTeam")
    String awayTeam;

    @JsonProperty("intHomeScore")
    String homeScore;

    @JsonProperty("intAwayScore")
    String awayScore;

    String round;

    @JsonProperty("strDate")
    String date;

    @JsonProperty("strTime")
    String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
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

    public String getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeScore='" + homeScore + '\'' +
                ", awayScore='" + awayScore + '\'' +
                ", round='" + round + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
