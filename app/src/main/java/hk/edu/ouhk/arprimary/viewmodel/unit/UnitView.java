package hk.edu.ouhk.arprimary.viewmodel.unit;

public class UnitView {

    private final int no;
    private final Type type;

    public UnitView(int no, Type type) {
        this.no = no;
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
