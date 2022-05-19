package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {
    TextView txtScore;
    Button btnFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        int s = getIntent().getIntExtra("scores",0);
        ArrayList<String> summary = getIntent().getStringArrayListExtra("summary");
        ListView list_summary = findViewById(R.id.list_summary);
        txtScore.setText(s);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                summary);
        list_summary.setAdapter(adapter);

        btnFinish.setOnClickListener(view -> {
            Intent startIntent = new Intent(this, HomeActivity.class);
            startActivity(startIntent);
        });
    }
}