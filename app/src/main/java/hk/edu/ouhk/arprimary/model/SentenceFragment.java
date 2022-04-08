package hk.edu.ouhk.arprimary.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class SentenceFragment implements Serializable {

    private final List<String> fragments;
    private final String correctAnswer;

    public SentenceFragment(List<String> fragments, String correctAnswer) {
        this.fragments = fragments;
        this.correctAnswer = correctAnswer;

    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getFragments() {
        return fragments;
    }
}
