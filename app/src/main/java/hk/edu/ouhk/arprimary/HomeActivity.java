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
    Button leaderboardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        journalBtn = findViewById(R.id.start_journal_button);
        leaderboardBtn = findViewById(R.id.leaderboard_button);

        journalBtn.setOnClickListener(view -> {
            Intent startIntent = new Intent(this, TopicActivity.class);
            startActivity(startIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        leaderboardBtn.setOnClickListener(v -> {
            Intent startIntent = new Intent(this, LeaderboardActivity.class);
            startActivity(startIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

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