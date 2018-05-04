package ro.uaic.info.tppa.sportscores.models.livescores;

public class Commentary {
    String minute;
    String text;
    String type;

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return minute + "' " + text;
    }
}
