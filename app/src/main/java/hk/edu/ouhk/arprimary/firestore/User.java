package hk.edu.ouhk.arprimary.firestore;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final List<PlayedHistory> histories;

    public User(List<PlayedHistory> histories){
        this.histories = histories;
    }

    public User(){
        this(new ArrayList<>());
    }

    public List<PlayedHistory> getHistories() {
        return histories;
    }

    public void addHistory(PlayedHistory history){
        this.histories.add(history);
    }

}
