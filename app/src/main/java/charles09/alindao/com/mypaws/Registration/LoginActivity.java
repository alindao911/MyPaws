package charles09.alindao.com.mypaws.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import charles09.alindao.com.mypaws.MainActivity;
import charles09.alindao.com.mypaws.R;

public class LoginActivity extends AppCompatActivity {
    //widgets
    private TextView textViewUsername;
    private TextView textViewPassword;
    private Button btnSignIn;
    private Button btnSignUp;
    private ProgressDialog progressDialog;

    //firebase
    private FirebaseAuth mAuth;

    //vars
    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //cast widgets
        textViewPassword = findViewById(R.id.etLoginPassword);
        textViewUsername = findViewById(R.id.etLoginUsername);
        btnSignIn = findViewById(R.id.btnsignin);
        btnSignUp = findViewById(R.id.btnsignup);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check username and password is empty
                username = textViewUsername.getText().toString();
                password = textViewPassword.getText().toString();
                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    toastMethod("Please input all fields!");
                } else {
                    //check if username and password is a user
                    progressDialog.setMessage("Logging in...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void toastMethod(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
