package hk.edu.ouhk.arprimary.viewmodel.armodel;

public abstract class Sectionable implements Comparable<Sectionable> {

    protected final int order;
    private final String answer;

    protected Sectionable(int order, String answer) {
        this.order = order;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public int compareTo(Sectionable sectionable) {
        return Integer.compare(this.order, sectionable.order);
    }
}
