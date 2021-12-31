package hk.edu.ouhk.arprimary.viewmodel;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public abstract class ListViewModel<T> extends ViewModel {

    protected MutableLiveData<ViewListAction<T>> viewList = new MutableLiveData<>();
    protected int page = 1;

    public MutableLiveData<ViewListAction<T>> getViewList() {
        return viewList;
    }

    protected abstract List<T> readFromDataSource(int page);

    public void reset(){
        page = 1;
        ExecutorService service = ForkJoinPool.commonPool();
        service.submit(() -> {
            try{
                List<T> list = readFromDataSource(page);
                this.viewList.postValue(new ViewListAction<>(list, ViewListAction.Action.SET));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public void loadMore(){
        page++;
        ExecutorService service = ForkJoinPool.commonPool();
        service.submit(() -> {
            try{
                List<T> list = readFromDataSource(page);
                this.viewList.postValue(new ViewListAction<>(list, ViewListAction.Action.ADD));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
