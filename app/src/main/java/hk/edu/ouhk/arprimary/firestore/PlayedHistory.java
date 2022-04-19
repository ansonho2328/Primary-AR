package hk.edu.ouhk.arprimary.firestore;

import hk.edu.ouhk.arprimary.viewmodel.unit.UnitView;

public class PlayedHistory {

    private UnitView unit;
    private int scores;
    private String topic;

    public PlayedHistory() {
        this.scores = 0;
        this.topic = "";
    }

    public PlayedHistory(UnitView unit, int scores, String topic) {
        this.unit = unit;
        this.scores = scores;
        this.topic = topic;
    }

    public void setUnit(UnitView unit) {
       this.unit = unit;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public String getTopic() {
        return topic;
    }

    public UnitView getUnit() {
        return unit;
    }

    public int getScores() {
        return scores;
    }
}
