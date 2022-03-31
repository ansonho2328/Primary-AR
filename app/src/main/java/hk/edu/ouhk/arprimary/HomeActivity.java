package hk.edu.ouhk.arprimary;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    Button journalBtn;
    Button multiplayerBtn;
    Button leaderboardBtn;
    Button speachBtn,scorebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        journalBtn = findViewById(R.id.start_journal_button);
        multiplayerBtn = findViewById(R.id.start_multiplayer_button);
        leaderboardBtn = findViewById(R.id.leaderboard_button);
//        speachBtn = findViewById(R.id.text_speach_button);
        scorebtn = findViewById(R.id.score_button);

        journalBtn.setOnClickListener(view -> {
            Intent startIntent = new Intent(this, TopicActivity.class);
            startActivity(startIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        multiplayerBtn.setOnClickListener(v -> {
            //TODO add multiplayer activity
            Toast.makeText(this, "not finished yet", Toast.LENGTH_LONG).show();
        });

        leaderboardBtn.setOnClickListener(v -> {
            Intent startIntent = new Intent(this, leaderboard.class);
            startActivity(startIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        scorebtn.setOnClickListener(v -> {
            int score = 0;
            String username = "test";
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://primary-ar-default-rtdb.asia-southeast1.firebasedatabase.app");
            DatabaseReference myRef = database.getReference("Leaderboard");
            ArrayList<User> user = new ArrayList<>();
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User value = dataSnapshot.getValue(User.class);
                    user.add(value);
                    User temp = null;
                    for (User x : user) {
                        if(x.getUsername().equals(username)){
                            temp = x;
                        }
                    }

                    if(temp == null){
                        myRef.child(username).setValue(new User(username,0));

                    }

                    if(temp != null){
                        temp.setScore(temp.getScore()+10);
                        Map<String,Object> update = new HashMap<>();
                        update.put(username,temp);
                        myRef.updateChildren(update);

                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        });


//        speachBtn.setOnClickListener(v ->{
//            Intent startIntent = new Intent(this, SpeechToTextActivity.class);
//            startActivity(startIntent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    
    public void onEnterProfile(MenuItem item) {
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onEnterAbout(MenuItem item) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}