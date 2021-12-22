package hk.edu.ouhk.arprimary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView aDesc;
    Toolbar aToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aDesc = findViewById(R.id.aDesc);
        aToolbar = findViewById(R.id.aToolbar);
        aDesc.setText("Primary AR is a English Learning mobile application that provides game with AR technology " +
                "for primary students learning English. " +
                "Application is developed by: \n\n Suen Chi Hong 12523232\n Ho Chun Hei 12529734 \n Ng Wai Pang 12601411\n" +
                " Lam Chak Wai 12628712");

        aToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        aToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(AboutActivity.this, HomeActivity.class);
                startActivity(backIntent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
    }
}