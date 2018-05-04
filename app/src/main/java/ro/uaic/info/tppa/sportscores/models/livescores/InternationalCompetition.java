package ro.uaic.info.tppa.sportscores.models.livescores;

import java.io.Serializable;

public class InternationalCompetition implements Serializable{
    String link;
    String name;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InternationalCompetition{" +
                "link='" + link + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
