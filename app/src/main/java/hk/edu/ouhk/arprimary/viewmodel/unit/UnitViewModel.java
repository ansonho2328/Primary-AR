package hk.edu.ouhk.arprimary.viewmodel.unit;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hk.edu.ouhk.arprimary.firestore.User;
import hk.edu.ouhk.arprimary.viewmodel.ListViewModel;
import hk.edu.ouhk.arprimary.viewmodel.ViewListAction;

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
        return UNIT_VIEW_LIST
                .stream()
                .skip(Math.max(0, page - 1) * AMOUNT_PER_PAGE)
                .limit(AMOUNT_PER_PAGE)
                .collect(Collectors.toList());
    }


    private Task<List<UnitView>> getFromFireStore(String username, String topic, List<UnitView> list){
        return FirebaseFirestore
                .getInstance()
                .collection("histories")
                .document(username)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null){
                        User user = task.getResult().toObject(User.class);
                        if (user == null){
                            return list;
                        }
                        Stream<UnitView> nonFinishedQuiz = list.stream()
                                .filter(v ->
                                        v.getType() == UnitView.Type.QUIZ &&
                                                user.getHistories()
                                                        .stream()
                                                        .anyMatch(h ->
                                                                h.getUnit() == v.getNo() &&
                                                                        h.getTopic().equals(topic)
                                                        )
                                );

                        Stream<UnitView> finishedPractice = nonFinishedQuiz.filter(v ->
                                v.getType() == UnitView.Type.PRACTICE &&
                                        user.getHistories()
                                                .stream()
                                                .noneMatch(h ->
                                                        h.getUnit() < v.getNo() &&
                                                                h.getTopic().equals(topic)
                                                )
                        );

                        return finishedPractice.collect(Collectors.toList());
                    }else{
                        if (task.getException() != null){
                            task.getException().printStackTrace();
                        }
                        return list;
                    }
                });
    }

    public void reset(String username, String topic) {
        page = 1;
        List<UnitView> list = readFromDataSource(page);
        getFromFireStore(username, topic, list).addOnCompleteListener(task -> {
            if (task.getResult() != null){
                this.viewList.postValue(new ViewListAction<>(task.getResult(), ViewListAction.Action.SET));
            }
        });
    }

    public void loadMore(String username, String topic) {
        page++;
        List<UnitView> list = readFromDataSource(page);
        getFromFireStore(username, topic, list).addOnCompleteListener(task -> {
            if (task.getResult() != null){
                this.viewList.postValue(new ViewListAction<>(task.getResult(), ViewListAction.Action.ADD));
            }
        });
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("use reset(String username, String topic) instead.");
    }


    @Override
    public void loadMore() {
        throw new UnsupportedOperationException("use loadMore(String username, String topic) instead.");
    }
}
