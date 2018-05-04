package ro.uaic.info.tppa.sportscores.models.sportsdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TableListItem {
    public String name;
    @JsonProperty("teamid")
    public String teamId;
    @JsonProperty("goalsdifference")
    public String points;
    public String total;
    public String win;
    public String draw;
    public String loss;
    public String played;
    @JsonProperty("goalsfor")
    public String goalsFor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(String goalsFor) {
        this.goalsFor = goalsFor;
    }

    public String getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(String goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    @JsonProperty("goalsagainst")
    public String goalsAgainst;

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    @Override
    public String toString() {
        return "TableListItem{" +
                "name='" + name + '\'' +
                ", teamId='" + teamId + '\'' +
                ", points='" + points + '\'' +
                ", total='" + total + '\'' +
                ", win='" + win + '\'' +
                ", draw='" + draw + '\'' +
                ", loss='" + loss + '\'' +
                ", goalsFor='" + goalsFor + '\'' +
                ", goalsAgainst='" + goalsAgainst + '\'' +
                '}';
    }
}
