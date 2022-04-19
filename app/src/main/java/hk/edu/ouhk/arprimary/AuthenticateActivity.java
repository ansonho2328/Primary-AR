package hk.edu.ouhk.arprimary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthenticateActivity extends AppCompatActivity {

    EditText sigName, sigPassword;
    Button signUp, signIn;

    FirebaseAuth auth;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();

        sigName = findViewById(R.id.sigName);
        sigPassword = findViewById(R.id.sigPassword);

        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);

        ActionCodeSettings settings = ActionCodeSettings.newBuilder()
                .setHandleCodeInApp(true)
                .setAndroidPackageName("hk.edu.ouhk.arprimary", true, "28")
                .build();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(sigName.getText())) {
                    sigName.setError("username is required");
                    return;
                }

                if (TextUtils.isEmpty(sigPassword.getText())) {
                    sigPassword.setError("password is required");
                    return;
                } else if (TextUtils.getTrimmedLength(sigPassword.getText()) < 4) {
                    sigPassword.setError("password must be at least 4 characters");
                    return;
                }

                String email = sigName.getText().toString()+"@arprimary.user";

                auth
                        .createUserWithEmailAndPassword(email, sigPassword.getText().toString())
                        .continueWithTask(task -> {
                            if (task.isSuccessful()){
                                AuthResult result = task.getResult();
                                return result.getUser().updateProfile(
                                  new UserProfileChangeRequest.Builder()
                                  .setDisplayName(sigName.getText().toString())
                                  .build()
                                );
                            }else if (task.getException() != null){
                                task.getException().printStackTrace();
                                Toast.makeText(AuthenticateActivity.this, "Sign Up Failed: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(AuthenticateActivity.this, "Sign Up Failed: Unknown", Toast.LENGTH_LONG).show();
                            }
                            return Tasks.forCanceled();
                        })
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AuthenticateActivity.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AuthenticateActivity.this, HomeActivity.class));
                            }
                        });
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(sigName.getText())) {
                    sigName.setError("username is required");
                    return;
                }

                if (TextUtils.isEmpty(sigPassword.getText())) {
                    sigPassword.setError("password is required");
                    return;
                }

                String email = sigName.getText().toString()+"@arprimary.user";

                auth.signInWithEmailAndPassword(email, sigPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(AuthenticateActivity.this, HomeActivity.class));
                        }else{
                            Toast.makeText(AuthenticateActivity.this, "Sign In Failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }

}