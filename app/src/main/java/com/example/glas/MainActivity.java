package com.example.glas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button btn_register;
    private TextView link_signin;
    EditText mname, memail, mpassword;
    Button msignup;
    ProgressBar loading;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mname = findViewById(R.id.name);
        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        msignup = findViewById(R.id.btn_signup);
        fAuth = FirebaseAuth.getInstance();
        loading = findViewById(R.id.loading);

        link_signin = findViewById(R.id.link_signin);
        link_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MainActivity.this, LoginActivity.class));
            }
        });

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, TeacherDashboard.class));
            finish();
        }


        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    memail.setError("Email s required.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mpassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6){
                    mpassword.setError("Password must be more than 6 characters");
                    return;
                }

                loading.setVisibility(view.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"SignUp successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,TeacherDashboard.class));
                        } else {
                            Toast.makeText(MainActivity.this,"SignUP not successful",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
