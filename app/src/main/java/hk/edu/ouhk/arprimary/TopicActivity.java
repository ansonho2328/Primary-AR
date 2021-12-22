package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class TopicActivity extends AppCompatActivity {
    ImageView topAnimal, topFruit, topStationary;
    CardView cardAnimal,cardFruit,cardStationary;
    Toolbar tToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);


        tToolbar = findViewById(R.id.tToolbar);
        cardAnimal = findViewById(R.id.cardAnimal);
        cardFruit = findViewById(R.id.cardFruit);
        cardStationary = findViewById(R.id.cardStationary);

        tToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        tToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(TopicActivity.this, HomeActivity.class);
                startActivity(backIntent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        cardAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent animalIntent = new Intent(TopicActivity.this, HomeActivity.class);
               startActivity(animalIntent);
               finish();
            }
        });

        cardFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fruitIntent = new Intent(TopicActivity.this, HomeActivity.class);
                startActivity(fruitIntent);
                finish();
            }
        });

        cardStationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stationaryIntent = new Intent(TopicActivity.this, HomeActivity.class);
                startActivity(stationaryIntent);
                finish();
            }
        });


    }
}