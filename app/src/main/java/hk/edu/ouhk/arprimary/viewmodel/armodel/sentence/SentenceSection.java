package hk.edu.ouhk.arprimary.viewmodel.armodel.sentence;

import java.util.List;

import hk.edu.ouhk.arprimary.viewmodel.armodel.Sectionable;

public class SentenceSection  extends Sectionable {

    private final List<String> fragments;


    public SentenceSection(int order, List<String> fragments, String correctAnswer) {
        super(order, correctAnswer);
        this.fragments = fragments;
    }

    public List<String> getFragments() {
        return fragments;
    }
}
