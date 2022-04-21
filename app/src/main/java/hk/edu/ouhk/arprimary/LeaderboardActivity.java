package hk.edu.ouhk.arprimary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;

import hk.edu.ouhk.arprimary.firestore.User;

public class LeaderboardActivity extends AppCompatActivity {
    FirebaseFirestore store = FirebaseFirestore.getInstance();
    ListView topUser;
    ArrayList<Board> boards = new ArrayList<>();
    ArrayAdapter<Board> arrayAdapter;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        topUser = (ListView) findViewById(R.id.top_player);
        arrayAdapter = new ArrayAdapter<Board>(this, android.R.layout.simple_list_item_1, boards);
        topUser.setAdapter(arrayAdapter);

        store.collection("scores").orderBy("scores").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 1;
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        int scores = Optional.ofNullable(queryDocumentSnapshot.get("scores", Integer.class)).orElse(0);
                        boards.add(new Board(i, queryDocumentSnapshot.getId(), scores));
                        i++;
                    }
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    String error = "Leaderboard loading failed: " + (task.getException() != null ? task.getException().getMessage() : "");
                    Toast.makeText(LeaderboardActivity.this, error, Toast.LENGTH_LONG).show();
                    if (task.getException() != null) {
                        task.getException().printStackTrace();
                    }
                }
            }
        });


    }


    static class Board {

        final int order;
        final String username;
        final int scores;

        Board(int order, String username, int scores) {
            this.order = order;
            this.username = username;
            this.scores = scores;
        }

        // this is for display on list view
        @NonNull
        @Override
        public String toString() {
            return MessageFormat.format("{0}. {1}  --  scores: {2}", order, username, scores);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != android.R.id.home) return false;
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }
}