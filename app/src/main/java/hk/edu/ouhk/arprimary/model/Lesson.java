package hk.edu.ouhk.arprimary.model;

import java.io.Serializable;

public class Lesson implements Serializable {

   private final LessonFragment[] fragments;

    public Lesson(LessonFragment[] fragments) {
        this.fragments = fragments;
    }

    public LessonFragment[] getFragments() {
        return fragments;
    }
}
