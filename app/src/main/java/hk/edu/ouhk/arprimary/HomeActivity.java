package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {

    ImageView aniIcon;
    TextView appTitle;
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        
        aniIcon = findViewById(R.id.aniIcon);
        appTitle = findViewById(R.id.appTitle);
        startBtn = findViewById(R.id.startBtn);

        // make the animation for application icon
        PropertyValuesHolder rotationY = PropertyValuesHolder.ofFloat("rotationY",0.0F,360.0F);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",0.0F,0,1F);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",0.0F,0,1F);
        ObjectAnimator.ofPropertyValuesHolder(aniIcon,rotationY,scaleX,scaleY).setDuration(3000).start();
        ObjectAnimator.ofPropertyValuesHolder(appTitle,rotationY,scaleX,scaleY).setDuration(3000).start();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, TopicActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.about:
                Intent aboutIntent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            case R.id.leaderboard:


            default:
        }

        return super.onOptionsItemSelected(item);
    }
}