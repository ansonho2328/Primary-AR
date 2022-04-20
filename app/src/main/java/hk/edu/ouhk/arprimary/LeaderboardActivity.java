package hk.edu.ouhk.arprimary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        topUser = (ListView) findViewById(R.id.top_player);
        arrayAdapter = new ArrayAdapter<Board>(this, android.R.layout.simple_list_item_1, boards);
        topUser.setAdapter(arrayAdapter);


        store.collection("scores").orderBy("scores").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        User user = queryDocumentSnapshot.toObject(User.class);
                        boards.add(new Board(queryDocumentSnapshot.getId(), user.getScore()));
                    }
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    String error = "Leaderboard loading failed: " + task.getException() != null ? task.getException().getMessage() : "";
                    Toast.makeText(LeaderboardActivity.this, error, Toast.LENGTH_LONG).show();
                    if (task.getException() != null) {
                        task.getException().printStackTrace();
                    }
                }
            }
        });


    }


    class Board {

        final String username;
        final int scores;

        Board(String username, int scores) {
            this.username = username;
            this.scores = scores;
        }

        // this is for display on list view
        @Override
        public String toString() {
            return "Username: " + this.username + ", Scores:" + this.scores;
        }
    }


}