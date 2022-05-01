package hk.edu.ouhk.arprimary.firestore;

import java.util.List;

public class PlayedHistories {

    List<PlayedHistory> histories; // match the path of document

    // no-arg constructor
    // setter and getter

    public List<PlayedHistory> getHistories() {
        return histories;
    }

    public void setHistories(List<PlayedHistory> histories) {
        this.histories = histories;
    }
}