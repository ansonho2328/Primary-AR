package hk.edu.ouhk.arprimary.viewmodel.armodel;

import com.google.ar.sceneform.rendering.ModelRenderable;

public abstract class Vocabulary implements Comparable<Vocabulary>{

    protected final int order;
    private final ModelRenderable modelRenderable;
    private final String vocab;

    public Vocabulary(int order, ModelRenderable modelRenderable, String vocab) {
        this.order = order;
        this.modelRenderable = modelRenderable;
        this.vocab = vocab;
    }

    public ModelRenderable getModelRenderable() {
        return modelRenderable;
    }

    public String getVocab() {
        return vocab;
    }

    @Override
    public int compareTo(Vocabulary vocabulary) {
        return Integer.compare(this.order, vocabulary.order);
    }
}
