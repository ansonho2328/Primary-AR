package hk.edu.ouhk.arprimary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import hk.edu.ouhk.arprimary.viewmodel.ListExtendableAdapter;
import hk.edu.ouhk.arprimary.viewmodel.ViewListAction;
import hk.edu.ouhk.arprimary.viewmodel.topic.TopicAdapter;
import hk.edu.ouhk.arprimary.viewmodel.unit.UnitAdapter;
import hk.edu.ouhk.arprimary.viewmodel.unit.UnitView;
import hk.edu.ouhk.arprimary.viewmodel.unit.UnitViewModel;

public class UnitActivity extends AppCompatActivity {

    private static final String LOG_TAG = UnitActivity.class.getSimpleName();

    UnitViewModel viewModel;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        this.topic = getIntent().getExtras().getString("topic");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this::onUpdateView);

        viewModel = new ViewModelProvider(this).get(UnitViewModel.class);
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProgressBar loadMore = findViewById(R.id.list_loading_more);

        viewModel.getViewList().observe(this, unit -> {
            UnitAdapter adapter = (UnitAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                if (unit.action == ViewListAction.Action.ADD) { // add
                    loadMore.setVisibility(View.GONE);
                    adapter.addAll(unit.list);
                    Log.v(LOG_TAG, "Added " + unit.list.size() + " items.");
                } else if (unit.action == ViewListAction.Action.SET) {
                    refreshLayout.setRefreshing(false);
                    findViewById(R.id.empty_hints).setVisibility(unit.list.size() == 0 ? View.VISIBLE : View.GONE);
                    adapter.setAll(unit.list);
                    Log.v(LOG_TAG, "Set " + unit.list.size() + " items.");
                }

            } else {
                refreshLayout.setRefreshing(false);
                adapter = new UnitAdapter(unit.list, this, v -> {
                    int pos = recyclerView.getChildAdapterPosition(v);
                    UnitView unitView = unit.list.get(pos);
                    Toast.makeText(UnitActivity.this, unitView.getNo()+"-"+unitView.getType(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UnitActivity.this, LessonActivity.class);
                    intent.putExtra("topic", topic);
                    intent.putExtra("unit-no", unitView.getNo());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                });
                recyclerView.setAdapter(adapter);
                adapter.notifyItemRangeInserted(0, unit.list.size());
                Log.v(LOG_TAG, "Set " + unit.list.size() + " items.");
                findViewById(R.id.empty_hints).setVisibility(unit.list.size() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() < UnitViewModel.AMOUNT_PER_PAGE)
                    return;
                if (!recyclerView.canScrollVertically(1)) {
                    loadMore.setVisibility(View.VISIBLE);
                    viewModel.loadMore();
                }
            }
        });

        // calling this method for first loading units
        this.onUpdateView();
    }

    private void onUpdateView(){
        refreshLayout.setRefreshing(true);
        ListExtendableAdapter<?, ?> adapter = (ListExtendableAdapter<?, ?>) recyclerView.getAdapter();
        if (adapter != null) adapter.resetAll();
        viewModel.reset();
        // Test Fake load with delay
        //new Handler().postDelayed(viewModel::reset, 3000);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != android.R.id.home) return false;
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }
}