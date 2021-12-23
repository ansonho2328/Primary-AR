package hk.edu.ouhk.arprimary.viewmodel;


import java.util.List;

public final class ViewListAction<T> {
    public final List<T> list;
    public final Action action;

    public ViewListAction(List<T> list, Action action) {
        this.list = list;
        this.action = action;
    }

    public enum Action {
        SET, ADD
    }
}
