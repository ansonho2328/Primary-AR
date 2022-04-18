package hk.edu.ouhk.arprimary.firestore;

import hk.edu.ouhk.arprimary.viewmodel.unit.UnitView;

public class PlayedHistory {

    private final UnitView unit;
    private final int scores;
    private final String topic;

    public PlayedHistory(UnitView unit, int scores, String topic) {
        this.unit = unit;
        this.scores = scores;
        this.topic = topic;
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
