package hk.edu.ouhk.arprimary;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hk.edu.ouhk.arprimary.firestore.PlayedHistory;
import hk.edu.ouhk.arprimary.firestore.User;
import hk.edu.ouhk.arprimary.manager.ApplicationComponent;
import hk.edu.ouhk.arprimary.manager.FragmentManager;
import hk.edu.ouhk.arprimary.manager.LessonFragmentManager;
import hk.edu.ouhk.arprimary.manager.QuizFragmentManager;
import hk.edu.ouhk.arprimary.manager.SentenceFragmentManager;
import hk.edu.ouhk.arprimary.model.LessonFragment;
import hk.edu.ouhk.arprimary.model.SentenceFragment;
import hk.edu.ouhk.arprimary.viewmodel.unit.UnitView;

public class ReviewActivity extends AppCompatActivity {
    CollectionReference historyRef;
    FirebaseUser session;


    LessonFragmentManager lessonFragmentManager;
    SentenceFragmentManager sentencefragmentManager;
    QuizFragmentManager quizFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        this.session = auth.getCurrentUser();

        if (this.session == null){
            Toast.makeText(this, "Not Login", Toast.LENGTH_LONG).show();
            //TODO maybe jump tp AuthenticateActivity?
            return;
        }

        ApplicationComponent component = ((PrimaryARApplication)getApplicationContext()).appComponent;

        this.quizFragmentManager = component.quizFragmentManager();
        this.lessonFragmentManager = component.lessonFragmentManager();
        this.sentencefragmentManager = component.sentenceFragmentManager();

        historyRef = store.collection("histories");
        ListView listReview = (ListView) findViewById(R.id.list_review);
        historyRef.document(session.getDisplayName()).get().continueWithTask(task -> {
            User user = Optional
                    .ofNullable(task.getResult())
                    .map(r -> r.toObject(User.class))
                    .orElseGet(User::new);


            List<PlayedHistory> playedHistory = user.getHistories();
            ArrayList<String> show = new ArrayList<String>();

            playedHistory.forEach((e) -> {

                if (e.getType() == UnitView.Type.PRACTICE) {

                    Optional<LessonFragment[]> lessonFragments = lessonFragmentManager.getFragmentsByTopicUnit(e.getTopic(), e.getUnit());
                    Optional<SentenceFragment[]> sentenceFragments = sentencefragmentManager.getFragmentsByTopicUnit(e.getTopic(), e.getUnit());

                    if (lessonFragments.isPresent()) {
                        LessonFragment[] fragments = lessonFragments.get();
                        show.add(MessageFormat.format("Topic: {0} ({1}", e.getTopic(), "Vocab"));
                        for (LessonFragment fragment : fragments) {
                            show.add(MessageFormat.format("{0}: {1}", fragment.getModelName(), fragment.getDefinition()));
                        }
                    }else if (sentenceFragments.isPresent()){
                        SentenceFragment[] fragments = sentenceFragments.get();
                        show.add(MessageFormat.format("Topic: {0} ({1}", e.getTopic(), "Sentence"));
                        for (SentenceFragment fragment : fragments) {
                            show.add(fragment.getCorrectAnswer());
                        }
                    } else {
                        Toast.makeText(this, MessageFormat.format("Topic {0}-{1}-{2} does not exist.", e.getTopic(), e.getType(), e.getUnit()), Toast.LENGTH_LONG).show();
                    }

                }
            });
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, show);
            listReview.setAdapter(adapter);
            return null;
        }).addOnCompleteListener(task -> {
            if (!task.isSuccessful() && task.getException() != null) {
                task.getException().printStackTrace();
            }
        });
    }
}