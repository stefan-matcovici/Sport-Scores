package ro.uaic.info.tppa.sportscores.models.livescores;

import java.io.Serializable;
import java.util.List;

public class InternationalCompetition implements Serializable {
    String key;
    String link;
    String name;
    List<InternationalEvent> events;

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

    public List<InternationalEvent> getEvents() {
        return events;
    }

    public void setEvents(List<InternationalEvent> events) {
        this.events = events;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "InternationalCompetition{" +
                "link='" + link + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
