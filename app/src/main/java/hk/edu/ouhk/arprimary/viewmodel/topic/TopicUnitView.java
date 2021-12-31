package hk.edu.ouhk.arprimary.viewmodel.topic;

public class TopicUnitView {

    private final String title;
    private final int drawable;

    public TopicUnitView(String title, int drawable) {
        this.title = title;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public int getDrawable() {
        return drawable;
    }

}
