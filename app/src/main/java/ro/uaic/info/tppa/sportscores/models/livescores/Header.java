package ro.uaic.info.tppa.sportscores.models.livescores;

public class Header {
    String competition;
    String competitionLink;
    String stage;
    String stageLink;
    String date;

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getCompetitionLink() {
        return competitionLink;
    }

    public void setCompetitionLink(String competitionLink) {
        this.competitionLink = competitionLink;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStageLink() {
        return stageLink;
    }

    public void setStageLink(String stageLink) {
        this.stageLink = stageLink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
