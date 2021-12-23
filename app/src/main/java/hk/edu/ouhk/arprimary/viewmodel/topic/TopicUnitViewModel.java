package hk.edu.ouhk.arprimary.viewmodel.topic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hk.edu.ouhk.arprimary.R;
import hk.edu.ouhk.arprimary.viewmodel.ListViewModel;

public class TopicUnitViewModel extends ListViewModel<TopicUnitView> {

    public static final long AMOUNT_PER_PAGE = 5L;

    private static final List<TopicUnitView> TOPIC_UNIT_LIST = new ArrayList<>();

    static {
        TOPIC_UNIT_LIST.add(new TopicUnitView("Animal", R.drawable.animal));
        TOPIC_UNIT_LIST.add(new TopicUnitView("Fruit", R.drawable.fruit));
        TOPIC_UNIT_LIST.add(new TopicUnitView("Stationary", R.drawable.stationary));
    }

    // each page show 5 topics
    @Override
    protected List<TopicUnitView> readFromDataSource(int page) {
        return TOPIC_UNIT_LIST.stream().skip(Math.max(0, page-1) * AMOUNT_PER_PAGE).limit(AMOUNT_PER_PAGE).collect(Collectors.toList());
    }

}
