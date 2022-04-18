package hk.edu.ouhk.arprimary.viewmodel.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hk.edu.ouhk.arprimary.viewmodel.ListViewModel;

public class UnitViewModel extends ListViewModel<UnitView> {

    public static final long AMOUNT_PER_PAGE = 15L;

    private final static List<UnitView> UNIT_VIEW_LIST = new ArrayList<>();

    static {
        UNIT_VIEW_LIST.add(new UnitView(1, UnitView.Type.PRACTICE));
        UNIT_VIEW_LIST.add(new UnitView(2, UnitView.Type.PRACTICE));
        UNIT_VIEW_LIST.add(new UnitView(1, UnitView.Type.QUIZ));
    }

    @Override
    protected List<UnitView> readFromDataSource(int page) {
        return UNIT_VIEW_LIST.stream().skip(Math.max(0, page-1) * AMOUNT_PER_PAGE).limit(AMOUNT_PER_PAGE).collect(Collectors.toList());
    }
}
