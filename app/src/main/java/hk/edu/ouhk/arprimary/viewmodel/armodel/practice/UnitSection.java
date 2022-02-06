package hk.edu.ouhk.arprimary.viewmodel.armodel.practice;

import com.google.ar.sceneform.rendering.ModelRenderable;

import hk.edu.ouhk.arprimary.viewmodel.armodel.Vocabulary;

public class UnitSection extends Vocabulary {

    private final String definition;

    public UnitSection(int order, ModelRenderable modelRenderable, String definition, String modelName) {
        super(order, modelRenderable, modelName);
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }

}
