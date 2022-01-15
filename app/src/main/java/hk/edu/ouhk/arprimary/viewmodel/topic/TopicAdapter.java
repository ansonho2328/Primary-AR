package hk.edu.ouhk.arprimary.viewmodel.topic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hk.edu.ouhk.arprimary.R;
import hk.edu.ouhk.arprimary.viewmodel.ListExtendableAdapter;

public class TopicAdapter extends ListExtendableAdapter<TopicView, TopicAdapter.ViewHolder> {

    public TopicAdapter(List<TopicView> list, Context context, View.OnClickListener onClickListener) {
        super(list, context, onClickListener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_topics, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getLayout() {
        return R.layout.recyclerview_topics;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopicView unit = list.get(position);
        holder.topicTitle.setText(unit.getTitle());
        holder.topicImage.setImageDrawable(AppCompatResources.getDrawable(context, unit.getDrawable()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView topicImage;
        final TextView topicTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topicImage = itemView.findViewById(R.id.topic_logo);
            topicTitle = itemView.findViewById(R.id.topic_title);
        }
    }
}
