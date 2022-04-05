package hk.edu.ouhk.arprimary;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import hk.edu.ouhk.arprimary.manager.SQLiteManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000;

    ImageView aniIcon;
    TextView appTitle;

    SQLiteManager sqLiteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        aniIcon = findViewById(R.id.imageView);
        appTitle = findViewById(R.id.appTitle);

        sqLiteManager = ((PrimaryARApplication)getApplicationContext()).appComponent.sqliteManager();

        Log.v("SplashEntity", "sqliteManager is null: "+(sqLiteManager == null));

        // make the animation for application icon
        PropertyValuesHolder rotationY = PropertyValuesHolder.ofFloat("rotationY",0.0F,360.0F);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",0.0F,0,1F);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",0.0F,0,1F);
        ObjectAnimator.ofPropertyValuesHolder(aniIcon,rotationY,scaleX,scaleY).setDuration(3000).start();
        ObjectAnimator.ofPropertyValuesHolder(appTitle,rotationY,scaleX,scaleY).setDuration(3000).start();


        //TODO make loading username from localdb, if found then jump to home activity rather than signup activity.


        new Handler().postDelayed(new Runnable() {  // set the splash screen timeout.
            @Override
            public void run() {
                // only if username exists
                //Toast.makeText(MainActivity.this, "Welcome back, XXXX", Toast.LENGTH_LONG).show();
                Intent splashIntent = new Intent(SplashActivity.this, HomeActivity.class);
               // savedInstanceState.putString("username", "tester");
                startActivity(splashIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }


}