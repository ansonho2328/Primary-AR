package hk.edu.ouhk.arprimary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class UnitActivity extends AppCompatActivity {
    Button back,u1,u2,u3,u4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        back = findViewById(R.id.Back);
        u1 = findViewById(R.id.u1);
        u2 = findViewById(R.id.u2);
        u3 = findViewById(R.id.u3);
        u4 = findViewById(R.id.u4);

        u1.setText("test1");
        u2.setText("test2");
        u3.setText("test3");
        u4.setText("test4");

    }

    public void back(View view) {
        Intent backIntent = new Intent(this, HomeActivity.class);
        startActivity(backIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != android.R.id.home) return false;
        onBackPressed();
        return true;
    }
}