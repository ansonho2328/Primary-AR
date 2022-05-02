package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hk.edu.ouhk.arprimary.firestore.PlayedHistory;
import hk.edu.ouhk.arprimary.firestore.User;
import hk.edu.ouhk.arprimary.manager.FragmentManager;
import hk.edu.ouhk.arprimary.model.Lesson;
import hk.edu.ouhk.arprimary.model.LessonFragment;
import hk.edu.ouhk.arprimary.viewmodel.armodel.practice.UnitSection;

public class ReviewActivity extends AppCompatActivity {
    CollectionReference historyRef;
    FirebaseUser session;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        this.session = auth.getCurrentUser();
        historyRef = store.collection("histories");
        ListView listReview = (ListView) findViewById(R.id.list_review);
        historyRef.document(session.getDisplayName()).get().continueWithTask(task -> {
            User user = Optional
                    .ofNullable(task.getResult())
                    .map(r -> r.toObject(User.class))
                    .orElseGet(User::new);
            List<PlayedHistory> playedHistory = user.getHistories();
            ArrayList<String> topic = new ArrayList<String>();
            ArrayList<String> str = new ArrayList<String>();
            playedHistory.forEach((e) -> {
                topic.add(e.getTopic());
            });
            topic.forEach((t) -> {
                Optional<LessonFragment[]> lessonFragments = fm.getFragmentsByTopicUnit(t,1);
                LessonFragment[] fragments = lessonFragments.get();
                for(int i =0;i < fragments.length;i++){
                    str.add(fragments[i].getModelName());
                }
            });
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1,
                    str);
            listReview.setAdapter(adapter);
            return null;
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

            }else{
                if (task.getException() != null){
                    task.getException().printStackTrace();
                }
            }
        });
    }
}