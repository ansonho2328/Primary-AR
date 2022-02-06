package hk.edu.ouhk.arprimary.viewmodel.armodel.quiz;

import com.google.ar.sceneform.rendering.ModelRenderable;

import hk.edu.ouhk.arprimary.viewmodel.armodel.Vocabulary;

public class QuizSection extends Vocabulary {

    private int score = 1; // default score

    public QuizSection(int order, ModelRenderable modelRenderable, String vocab) {
        super(order, modelRenderable, vocab);
    }

    public QuizSection(int order, ModelRenderable modelRenderable, String vocab, int score){
        this(order, modelRenderable, vocab);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
