package hk.edu.ouhk.arprimary.firestore;

import androidx.annotation.NonNull;

import java.text.MessageFormat;
import java.util.Locale;

import hk.edu.ouhk.arprimary.viewmodel.unit.UnitView;

public class PlayedHistory {

    private final UnitView.Type type;
    private final int unit;
    private final int scores;
    private final String topic;

    public PlayedHistory(UnitView.Type type, int unit, int scores, String topic) {
        this.type = type;
        this.unit = unit;
        this.scores = scores;
        this.topic = topic;
    }

    public UnitView.Type getType() {
        return type;
    }

    public int getUnit() {
        return unit;
    }

    public int getScores() {
        return scores;
    }

    public String getTopic() {
        return topic;
    }

    @NonNull
    @Override
    public String toString() {
        return MessageFormat.format("{0}: {1}-{2}, scores: {3}", topic, type.toString().toLowerCase(Locale.ROOT), unit, scores);
    }
}
