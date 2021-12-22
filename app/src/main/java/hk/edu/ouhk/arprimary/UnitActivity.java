package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UnitActivity extends AppCompatActivity {
    Button back,u1,u2,u3,u4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        back = findViewById(R.id.Back);
        u1 = findViewById(R.id.u1);
        u2 = findViewById(R.id.u2);
        u3 = findViewById(R.id.u3);
        u4 = findViewById(R.id.u4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(UnitActivity.this, HomeActivity.class);
                startActivity(signUpIntent);
                finish();
            }
        });

        u1.setText("test1");
        u2.setText("test2");
        u3.setText("test3");
        u4.setText("test4");

    }
}