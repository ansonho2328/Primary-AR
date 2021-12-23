package hk.edu.ouhk.arprimary;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import hk.edu.ouhk.arprimary.viewmodel.ViewListAction;
import hk.edu.ouhk.arprimary.viewmodel.topic.TopicUnitAdapter;
import hk.edu.ouhk.arprimary.viewmodel.topic.TopicUnitView;
import hk.edu.ouhk.arprimary.viewmodel.topic.TopicUnitViewModel;

public class TopicActivity extends AppCompatActivity {

    private static final String LOG_TAG = TopicActivity.class.getSimpleName();

    TopicUnitViewModel viewModel;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshLayout = findViewById(R.id.topic_refresh_layout);
        refreshLayout.setOnRefreshListener(this::onUpdateView);

        viewModel = new ViewModelProvider(this).get(TopicUnitViewModel.class);
        recyclerView = findViewById(R.id.recycle_topics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProgressBar loadMore = findViewById(R.id.topic_list_loading_more);

        viewModel.getViewList().observe(this, topic -> {
            TopicUnitAdapter adapter = (TopicUnitAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                if (topic.action == ViewListAction.Action.ADD) { // add
                    loadMore.setVisibility(View.GONE);
                    adapter.addAll(topic.list);
                    Log.v(LOG_TAG, "Added " + topic.list.size() + " items.");
                } else if (topic.action == ViewListAction.Action.SET) {
                    refreshLayout.setRefreshing(false);
                    findViewById(R.id.empty_hints).setVisibility(topic.list.size() == 0 ? View.VISIBLE : View.GONE);
                    adapter.setAll(topic.list);
                    Log.v(LOG_TAG, "Set " + topic.list.size() + " items.");
                }

            } else {
                refreshLayout.setRefreshing(false);
                adapter = new TopicUnitAdapter(topic.list, this, v -> {
                    int pos = recyclerView.getChildAdapterPosition(v);
                    TopicUnitView topicUnitView = topic.list.get(pos);

                    //TODO start activity with that topic
                    Toast.makeText(TopicActivity.this, "Starting Topic " + topicUnitView.getTitle(), Toast.LENGTH_LONG).show();
                });
                recyclerView.setAdapter(adapter);
                adapter.notifyItemRangeInserted(0, topic.list.size());
                Log.v(LOG_TAG, "Set " + topic.list.size() + " items.");
                findViewById(R.id.empty_hints).setVisibility(topic.list.size() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() < TopicUnitViewModel.AMOUNT_PER_PAGE)
                    return;
                if (!recyclerView.canScrollVertically(1)) {
                    loadMore.setVisibility(View.VISIBLE);
                    viewModel.loadMore();
                }
            }
        });

        // calling this method for first loading topics
        this.onUpdateView();

    }


    private void onUpdateView() {
        refreshLayout.setRefreshing(true);
        TopicUnitAdapter adapter = (TopicUnitAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.resetAll();
        //viewModel.reset();
        // Test Fake load with delay
        new Handler().postDelayed(viewModel::reset, 3000);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != android.R.id.home) return false;
        onBackPressed();
        return true;
    }
}