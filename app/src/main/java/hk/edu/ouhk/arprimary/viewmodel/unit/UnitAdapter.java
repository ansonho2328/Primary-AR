package hk.edu.ouhk.arprimary.viewmodel.unit;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Predicate;

import hk.edu.ouhk.arprimary.R;
import hk.edu.ouhk.arprimary.viewmodel.ListExtendableAdapter;

public class UnitAdapter extends ListExtendableAdapter<UnitView, UnitAdapter.ViewHolder> {


    public UnitAdapter(List<UnitView> list, Context context, View.OnClickListener onClickListener){
        super(list, context, onClickListener);
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getLayout() {
        return R.layout.recyclerview_units;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UnitView view = list.get(position);
        String title = view.getType() == UnitView.Type.PRACTICE ? "Unit" : "Quiz";
        String txt = MessageFormat.format("{0}-{1}", title, view.getNo());
        if (view.isReview()){
            holder.title.setText(txt);
            holder.title.setTextColor(Color.WHITE);
            holder.title.setBackgroundColor(Color.GRAY);
        }else{
            holder.title.setText(txt);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.unit_button);
        }
    }
}
