package hk.edu.ouhk.arprimary.viewmodel.armodel;

import com.google.ar.sceneform.rendering.ModelRenderable;

public class UnitSection implements Comparable<UnitSection>{

    private final ModelRenderable modelRenderable;
    private final String definition;
    private final String modelName;
    private final int order;

    public UnitSection(int order, ModelRenderable modelRenderable, String definition, String modelName) {
        this.modelRenderable = modelRenderable;
        this.definition = definition;
        this.modelName = modelName;
        this.order = order;
    }

    public ModelRenderable getModelRenderable() {
        return modelRenderable;
    }

    public String getDefinition() {
        return definition;
    }

    public String getModelName() {
        return modelName;
    }



    @Override
    public int compareTo(UnitSection unitSection) {
        return Integer.compare(this.order, unitSection.order);
    }
}
