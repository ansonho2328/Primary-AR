package hk.edu.ouhk.arprimary.firestore;

import java.util.ArrayList;
import java.util.List;

public class User {

    private int score;
    private final List<PlayedHistory> histories;

    public User(int score, List<PlayedHistory> histories){
        this.score = score;
        this.histories = histories;
    }

    public User(int score){
        this(score, new ArrayList<>());
    }

    public User(){
        this(0);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public List<PlayedHistory> getHistories() {
        return histories;
    }

    public void addHistory(PlayedHistory history){
        this.histories.add(history);
    }

}
