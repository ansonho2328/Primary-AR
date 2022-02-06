package hk.edu.ouhk.arprimary.model;

import java.io.Serializable;

public class QuizFragment implements Serializable {

    private final String vocab;
    private final String modelResourceName;
    private final int score;

    public QuizFragment(String vocab, String modelResourceName, int score) {
        this.vocab = vocab;
        this.modelResourceName = modelResourceName;
        this.score = score;
    }

    public QuizFragment(String vocab, String modelResourceName) {
        this(vocab, modelResourceName, 1);
    }

    public String getVocab() {
        return vocab;
    }

    public String getModelResourceName() {
        return modelResourceName;
    }

    public int getScore() {
        return score;
    }
}
