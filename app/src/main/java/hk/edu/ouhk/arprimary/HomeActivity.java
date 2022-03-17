package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    Button journalBtn;
    Button multiplayerBtn;
    Button leaderboardBtn;
    Button speachBtn;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        journalBtn = findViewById(R.id.start_journal_button);
        multiplayerBtn = findViewById(R.id.start_multiplayer_button);
        leaderboardBtn = findViewById(R.id.leaderboard_button);
//        speachBtn = findViewById(R.id.text_speach_button);

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