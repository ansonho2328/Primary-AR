package hk.edu.ouhk.arprimary.viewmodel.topic;

public class TopicView {

    private final String title;
    private final int drawable;

    public TopicView(String title, int drawable) {
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
