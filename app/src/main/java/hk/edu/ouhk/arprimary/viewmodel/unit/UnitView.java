package hk.edu.ouhk.arprimary.viewmodel.unit;

public class UnitView {

    private int no;
    private Type type;

    public UnitView() {
        this.no = 0;
    }

    public UnitView(int no, Type type) {
        this.no = no;
        this.type = type;
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
