package hk.edu.ouhk.arprimary.viewmodel.armodel;

import com.google.ar.sceneform.rendering.ModelRenderable;

public abstract class Vocabulary extends Sectionable {

    private final ModelRenderable modelRenderable;
    private final String vocab;

    public Vocabulary(int order, ModelRenderable modelRenderable, String vocab) {
        super(order, vocab);
        this.modelRenderable = modelRenderable;
        this.vocab = vocab;
    }

    public ModelRenderable getModelRenderable() {
        return modelRenderable;
    }

    public String getVocab() {
        return vocab;
    }

}
