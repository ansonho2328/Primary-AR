package hk.edu.ouhk.arprimary.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class ListExtendableAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    protected final List<T> list;
    protected final Context context;
    protected final View.OnClickListener onClickListener;

    public ListExtendableAdapter(List<T> list, Context context, View.OnClickListener onClickListener) {
        this.list = list;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public void addAll(List<T> views) {
        int lastPos = list.size() - 1;
        this.list.addAll(views);
        this.notifyItemInserted(lastPos);
    }

    public void setAll(List<T> views) {
        this.list.clear();
        this.list.addAll(views);
        this.notifyItemRangeChanged(0, list.size());
    }

    public void resetAll() {
        int lastSize = list.size();
        this.list.clear();
        this.notifyItemRangeRemoved(0, lastSize);
    }


    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(getLayout(), parent, false);
        view.setOnClickListener(onClickListener);
        return getViewHolder(view);
    }

    public abstract V getViewHolder(View view);

    public abstract int getLayout();

    @Override
    public int getItemCount() {
        return list.size();
    }
}
