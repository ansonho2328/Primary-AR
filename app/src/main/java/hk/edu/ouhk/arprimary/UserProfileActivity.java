package hk.edu.ouhk.arprimary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import hk.edu.ouhk.arprimary.firestore.PlayedHistory;
import hk.edu.ouhk.arprimary.firestore.User;

public class UserProfileActivity extends AppCompatActivity {

    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        FirebaseUser session = FirebaseAuth.getInstance().getCurrentUser();

        if (session != null){
            Toast.makeText(this, "invalid session", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        back = findViewById(R.id.Back);

        ArrayAdapter<PlayedHistory> historyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        ListView listView = findViewById(R.id.finished_list);
        listView.setAdapter(historyAdapter);

        TextView totalScore = findViewById(R.id.t_score);
        TextView highestScore = findViewById(R.id.h_score);
        TextView usernameShow = findViewById(R.id.uid);

        // set username
        usernameShow.setText(session.getDisplayName());

        FirebaseFirestore store = FirebaseFirestore.getInstance();

        store.collection("scores").document(session.getDisplayName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                int scores = task.getResult().get("scores", Integer.class);

                // set total scores
                totalScore.setText(String.valueOf(scores));
            }else{
                if (task.getException() != null){
                    task.getException().printStackTrace();
                }
                Toast.makeText(this, "loading failed: "+ (task.getException() == null ? "unknown" : task.getException().getMessage()), Toast.LENGTH_LONG).show();
            }
        });

        store.collection("histories").document(session.getDisplayName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                User user  = task.getResult().toObject(User.class);

                // set highest score
                int maxScore = user.getHistories().stream().mapToInt(v -> v.getScores()).max().orElse(0);
                highestScore.setText(String.valueOf(maxScore));

                // set played histories
                historyAdapter.clear();
                historyAdapter.addAll(user.getHistories());
                historyAdapter.notifyDataSetChanged();

            }else{
                if (task.getException() != null){
                    task.getException().printStackTrace();
                }
                Toast.makeText(this, "loading failed: "+ (task.getException() == null ? "unknown" : task.getException().getMessage()), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != android.R.id.home) return false;
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }
}