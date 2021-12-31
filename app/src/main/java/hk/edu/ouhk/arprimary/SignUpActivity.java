package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    EditText sigName;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sigName = findViewById(R.id.sigName);
        signUp = findViewById(R.id.signUp);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show loading animation
                findViewById(R.id.view_loading).setVisibility(View.VISIBLE);

                //TODO signup database operation
                new Handler().postDelayed(()->{
                    Toast.makeText(SignUpActivity.this, "Sign up Success", Toast.LENGTH_LONG).show();
                    Intent signUpIntent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(signUpIntent);
                    finish();
                }, 3000);
            }
        });


    }

}