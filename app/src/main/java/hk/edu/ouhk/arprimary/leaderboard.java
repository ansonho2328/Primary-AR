package hk.edu.ouhk.arprimary;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class leaderboard extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://primary-ar-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef = database.getReference("Leaderboard");
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        back = findViewById(R.id.Back);

        back.setOnClickListener(v -> {
            Intent startIntent = new Intent(this, HomeActivity.class);
            startActivity(startIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });



    }



}