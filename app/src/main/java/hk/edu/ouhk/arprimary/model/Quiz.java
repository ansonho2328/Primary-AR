package hk.edu.ouhk.arprimary.model;

import java.io.Serializable;

public class Quiz implements Serializable {

    private final QuizFragment[] quizFragments;

    public Quiz(QuizFragment[] quizFragments) {
        this.quizFragments = quizFragments;
    }

    public QuizFragment[] getQuizFragments() {
        return quizFragments;
    }
}
