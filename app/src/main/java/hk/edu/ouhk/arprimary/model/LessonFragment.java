package hk.edu.ouhk.arprimary.model;

import java.io.Serializable;

public class LessonFragment implements Serializable {

    private final String modelName;
    private final String modelResourceName;
    private final String definition;

    public LessonFragment(String modelName, String modelResourceName, String definition) {
        this.modelName = modelName;
        this.modelResourceName = modelResourceName;
        this.definition = definition;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModelResourceName() {
        return modelResourceName;
    }

    public String getDefinition() {
        return definition;
    }
}
