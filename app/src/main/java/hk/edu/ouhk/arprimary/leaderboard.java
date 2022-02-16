package hk.edu.ouhk.arprimary;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class leaderboard extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://primary-ar-default-rtdb.asia-southeast1.firebasedatabase.app");
    Button back;
    ListView topUser;
    ArrayList<String>  user = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference myRef = database.getReference("Leaderboard");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        back = findViewById(R.id.Back);
        topUser = (ListView) findViewById(R.id.top_player);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,user);
        topUser.setAdapter(arrayAdapter);

        back.setOnClickListener(v -> {
            Intent startIntent = new Intent(this, HomeActivity.class);
            startActivity(startIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,String s) {
                String value = dataSnapshot.getValue(User.class).toString();
                user.add(value);
                arrayAdapter.notifyDataSetChanged();
                Log.d(TAG, "Value is: " + value);
                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }



}