package hk.edu.ouhk.arprimary;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000;

    ImageView aniIcon;
    TextView appTitle;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        aniIcon = findViewById(R.id.imageView);
        appTitle = findViewById(R.id.appTitle);
        firebaseAuth = FirebaseAuth.getInstance();

        // make the animation for application icon
        PropertyValuesHolder rotationY = PropertyValuesHolder.ofFloat("rotationY",0.0F,360.0F);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX",0.0F,0,1F);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY",0.0F,0,1F);
        ObjectAnimator.ofPropertyValuesHolder(aniIcon,rotationY,scaleX,scaleY).setDuration(3000).start();
        ObjectAnimator.ofPropertyValuesHolder(appTitle,rotationY,scaleX,scaleY).setDuration(3000).start();



        new Handler().postDelayed(new Runnable() {  // set the splash screen timeout.
            @Override
            public void run() {
                FirebaseUser session = firebaseAuth.getCurrentUser();
                if (session != null){
                    Toast.makeText(SplashActivity.this, "Welcome back, "+session.getDisplayName(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }else{
                    Toast.makeText(SplashActivity.this, "Please sign up first", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SplashActivity.this, AuthenticateActivity.class));
                }
                finish();
            }
        },SPLASH_TIME_OUT);
    }


}