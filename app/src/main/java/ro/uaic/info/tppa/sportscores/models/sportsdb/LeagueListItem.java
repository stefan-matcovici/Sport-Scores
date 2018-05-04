package ro.uaic.info.tppa.sportscores.models.sportsdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LeagueListItem implements Serializable {

    @JsonProperty("idLeague")
    public String id;

    @JsonProperty("strLeague")
    public String league;

    @JsonProperty("strSport")
    public String sport;

    @JsonProperty("strLeagueAlternate")
    public String leagueAlternates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLeagueAlternates() {
        return leagueAlternates;
    }

    public void setLeagueAlternates(String leagueAlternates) {
        this.leagueAlternates = leagueAlternates;
    }

    @Override
    public String toString() {
        return "LeagueListItem{" +
                "id='" + id + '\'' +
                ", league='" + league + '\'' +
                ", sport='" + sport + '\'' +
                ", leagueAlternates='" + leagueAlternates + '\'' +
                '}';
    }
}
