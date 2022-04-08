package hk.edu.ouhk.arprimary.model;

import java.io.Serializable;

public class Sentence implements Serializable {

   private final SentenceFragment[] fragments;

    public Sentence(SentenceFragment[] fragments) {
        this.fragments = fragments;
    }

    public SentenceFragment[] getFragments() {
        return fragments;
    }
}
