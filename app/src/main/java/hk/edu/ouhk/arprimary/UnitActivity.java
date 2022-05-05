package hk.edu.ouhk.arprimary;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Optional;

import hk.edu.ouhk.arprimary.firestore.PlayedHistory;
import hk.edu.ouhk.arprimary.firestore.User;
import hk.edu.ouhk.arprimary.manager.ApplicationComponent;
import hk.edu.ouhk.arprimary.manager.LessonFragmentManager;
import hk.edu.ouhk.arprimary.manager.QuizFragmentManager;
import hk.edu.ouhk.arprimary.manager.SentenceFragmentManager;
import hk.edu.ouhk.arprimary.model.Lesson;
import hk.edu.ouhk.arprimary.model.LessonFragment;
import hk.edu.ouhk.arprimary.model.Quiz;
import hk.edu.ouhk.arprimary.model.QuizFragment;
import hk.edu.ouhk.arprimary.model.Sentence;
import hk.edu.ouhk.arprimary.model.SentenceFragment;
import hk.edu.ouhk.arprimary.viewmodel.ListExtendableAdapter;
import hk.edu.ouhk.arprimary.viewmodel.ViewListAction;
import hk.edu.ouhk.arprimary.viewmodel.unit.UnitAdapter;
import hk.edu.ouhk.arprimary.viewmodel.unit.UnitView;
import hk.edu.ouhk.arprimary.viewmodel.unit.UnitViewModel;

public class UnitActivity extends AppCompatActivity {

    private static final String LOG_TAG = UnitActivity.class.getSimpleName();

    CollectionReference historyRef, scoresRef;

    UnitViewModel viewModel;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;


    LessonFragmentManager lessonFragmentManager;
    SentenceFragmentManager sentencefragmentManager;
    QuizFragmentManager quizFragmentManager;

    String topic;

    FirebaseUser session;

    UnitView selected;

    ActivityResultLauncher<Intent> lessonLauncher, quizLauncher,sentenceLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        this.topic = getIntent().getExtras().getString("topic");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        this.session = auth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseFirestore store = FirebaseFirestore.getInstance();
        historyRef = store.collection("histories");
        scoresRef = store.collection("scores");


        ApplicationComponent component = ((PrimaryARApplication)getApplicationContext()).appComponent;

        this.quizFragmentManager = component.quizFragmentManager();
        this.lessonFragmentManager = component.lessonFragmentManager();
        this.sentencefragmentManager = component.sentenceFragmentManager();


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

                    this.selected = unitView;


                    if (unitView.isReview()){
                        //TODO add review activity
                        return;
                    }


                    if (unitView.getType() == UnitView.Type.PRACTICE){

                        Optional<LessonFragment[]> fragmentsOpt = lessonFragmentManager.getFragmentsByTopicUnit(topic, unitView.getNo());
                        Optional<SentenceFragment[]> senFragmentsOpt = sentencefragmentManager.getFragmentsByTopicUnit(topic, unitView.getNo());

                        if (fragmentsOpt.isPresent()){
                            Intent intent = new Intent(UnitActivity.this, LessonActivity.class);
                            LessonFragment[] fragments = fragmentsOpt.get();
                            Lesson lesson = new Lesson(fragments);
                            intent.putExtra("lesson", lesson);
                            lessonLauncher.launch(intent);
                        } else if (senFragmentsOpt.isPresent()){
                           Intent senIntent = new Intent(UnitActivity.this, SentenceActivity.class);
                           SentenceFragment[] fragments = senFragmentsOpt.get();

                           Sentence sentence = new Sentence(fragments);
                          // Toast.makeText(this, "sentence: "+ sentence, Toast.LENGTH_LONG).show();

                           senIntent.putExtra("sentence", sentence);
                           sentenceLauncher.launch(senIntent);
                        } else {
                            Toast.makeText(this, "Cannot find practice fragments on "+topic+"-"+unitView.getNo(), Toast.LENGTH_LONG).show();
                        }


                    } else if (unitView.getType() == UnitView.Type.QUIZ){

                        Intent intent = new Intent(UnitActivity.this, QuizActivity.class);

                        Optional<QuizFragment[]> fragmentsOpt = quizFragmentManager.getFragmentsByTopicUnit(topic, unitView.getNo());

                       if (fragmentsOpt.isPresent()){
                           QuizFragment[] fragments = fragmentsOpt.get();
                           Quiz quiz = new Quiz(fragments);
                           intent.putExtra("quiz", quiz);
                           quizLauncher.launch(intent);
                       }else{
                           Toast.makeText(this, "Cannot find quiz fragments on "+topic+"-"+unitView.getNo(), Toast.LENGTH_LONG).show();
                       }

                    } else {
                        Toast.makeText(this, "Unknown quiz type", Toast.LENGTH_LONG).show();
                    }



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
                    viewModel.loadMore(session.getDisplayName(), topic);
                }
            }
        });

        // calling this method for first loading units
        this.onUpdateView();
        lessonLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onLessonResult);
        quizLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onQuizResult);
        sentenceLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onLessonResult);
    }

    public void onLessonResult(ActivityResult result) {
        viewModel.reset(session.getDisplayName(),topic);
        if (result.getResultCode() == RESULT_OK) {
            // passed
            historyRef.document(session.getDisplayName()).get().continueWithTask(task -> {
                User user = Optional
                        .ofNullable(task.getResult())
                        .map(r -> r.toObject(User.class))
                        .orElseGet(User::new);
                user.addHistory(new PlayedHistory(selected.getType(), selected.getNo(), 0, topic));
                return historyRef.document(session.getDisplayName()).set(user);
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(UnitActivity.this, "You history has been updated to cloud", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(UnitActivity.this, "history update failed.", Toast.LENGTH_LONG).show();
                    if (task.getException() != null){
                        task.getException().printStackTrace();
                    }
                }
            });

            Toast.makeText(this, "You passed the course!", Toast.LENGTH_LONG).show();
        } else if (result.getResultCode() == RESULT_CANCELED){
            // cancelled
            Toast.makeText(this, "You cancelled the course", Toast.LENGTH_LONG).show();
        }
    }

    public void onQuizResult(ActivityResult result){
        viewModel.reset(session.getDisplayName(),topic);
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() == null){
                Toast.makeText(this, "intent data is null", Toast.LENGTH_LONG).show();
                return;
            }
            int score = result.getData().getIntExtra("scores", 0)*10;
            // passed
            Toast.makeText(this, "You score is: "+score, Toast.LENGTH_LONG).show();

            Task<Void> historyTask = historyRef.document(session.getDisplayName()).get().continueWithTask(task -> {
                User user = Optional
                        .ofNullable(task.getResult())
                        .map(r -> r.toObject(User.class))
                        .orElseGet(User::new);
                user.addHistory(new PlayedHistory(selected.getType(), selected.getNo(), score, topic));
                return historyRef.document(session.getDisplayName()).set(user);
            });

            Task<Void> scoreTask = scoresRef.document(session.getDisplayName()).get().continueWithTask(task -> {
                int scores = Optional
                        .ofNullable(task.getResult())
                        .map(r -> r.get("scores", Integer.class))
                        .orElse(0);
                scores += score;
                return scoresRef.document(session.getDisplayName()).update("scores", scores);
            });

            Tasks.whenAllComplete(historyTask, scoreTask).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(UnitActivity.this, "You progress has been updated to cloud", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(UnitActivity.this, "progress upload failed.", Toast.LENGTH_LONG).show();
                    if (task.getException() != null){
                        task.getException().printStackTrace();
                    }
                }
            });
        } else if (result.getResultCode() == RESULT_CANCELED){
            // cancelled
            Toast.makeText(this, "You cancelled the course", Toast.LENGTH_LONG).show();
        }
    }

    private void onUpdateView(){
        refreshLayout.setRefreshing(true);
        ListExtendableAdapter<?, ?> adapter = (ListExtendableAdapter<?, ?>) recyclerView.getAdapter();
        if (adapter != null) adapter.resetAll();
        viewModel.reset(session.getDisplayName(), topic);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != android.R.id.home) return false;
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }
}