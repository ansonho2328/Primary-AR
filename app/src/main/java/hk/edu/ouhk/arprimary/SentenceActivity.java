package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class SentenceActivity extends AppCompatActivity {

    ImageButton tipsBtn, leave;
    TextView sentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);

        tipsBtn = findViewById(R.id.tipsBtn);
        leave = findViewById(R.id.leave);
        sentence = findViewById(R.id.sentence);

        // Leave button handling for leaving the game
        leave.setOnClickListener(view -> {
            AlertDialog.Builder leave = new AlertDialog.Builder(SentenceActivity.this);
            leave.setIcon(ContextCompat.getDrawable(SentenceActivity.this, R.drawable.confirm_leave));
            leave.setTitle("Leave Game");
            leave.setMessage("Are you sure to leave the game?");

            leave.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    setResult(RESULT_CANCELED);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });

            leave.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            leave.show();
        });

        // Tips button handling for game tips or rules
        tipsBtn.setOnClickListener(view -> {
            AlertDialog.Builder tips = new AlertDialog.Builder(SentenceActivity.this);
            tips.setIcon(ContextCompat.getDrawable(SentenceActivity.this, R.drawable.tips));
            tips.setTitle("Sentence-Making Game Tips");
            tips.setMessage("Present Tense - Subject(S) + Verb(V)/be e.g I am happy. which 'I' is subject and 'am' is verb.\n\n" +
                    "1. select the word by order to make sentence in present tense." +
                    "2. check grammar for the sentence.");
            tips.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            tips.show();
        });

    }
}