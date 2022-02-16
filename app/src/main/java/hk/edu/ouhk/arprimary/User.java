package hk.edu.ouhk.arprimary;

public class User {
    private String username;
    private int score;
    public User(String username,int score){
        this.username = username;
        this.score = score;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return this.username +":"+this.score;
    }
    public User(){}
}
