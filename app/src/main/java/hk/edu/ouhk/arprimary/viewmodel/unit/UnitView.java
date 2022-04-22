package hk.edu.ouhk.arprimary.viewmodel.unit;

public class UnitView {

    private int no;
    private Type type;
    private boolean isReview;

    public UnitView() {
        this(0, Type.PRACTICE);
    }

    public UnitView(int no, Type type) {
        this(no, type, false);
    }

    public UnitView(int no, Type type, boolean isReview) {
        this.no = no;
        this.type = type;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getNo() {
        return no;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        QUIZ, PRACTICE
    }

}
