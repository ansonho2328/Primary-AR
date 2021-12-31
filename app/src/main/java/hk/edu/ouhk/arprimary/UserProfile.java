package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserProfile extends AppCompatActivity {
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        back = findViewById(R.id.Back);


    }
    public void Back(View v) {
        Intent HomeIntent = new Intent(UserProfile.this, HomeActivity.class);
        startActivity(HomeIntent);
        finish();
    }
}