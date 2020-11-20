package com.example.goeat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goeat.auth.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;


public class SignUpActivity extends AppCompatActivity {
    private EditText emailTV, passwordTV;
    private Button regBtn, toLoginBtn;
    private Auth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = Auth.getInstance();
        initializeUI();


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
    private void registerNewUser() {

        String email, password;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        User user = new User();
        user.setEmail(email);
        user.setAvatarURL("https://cdn.pixabay.com/photo/2015/06/25/14/13/fern-821293_1280.jpg");
        user.setBirthdate(new Date().getTime());
        user.setGender("Male");
        user.setHometown("HCM");
        user.setUsername("blalalla");
        Log.d("Test","before");
        mAuth.createUserWithEmailAndPassword(user, password)
                .addOnCompleteListener(new OnCompleteListener<User>() {
                    @Override
                    public void onComplete(@NonNull Task<User> task) {
                        Log.d("Test","after");
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            Log.d("Acc", "onComplete: " + task.getResult());
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("State:","onStop");
        finish();
    }
    private void initializeUI() {
        emailTV = findViewById(R.id.email);
        passwordTV = findViewById(R.id.password);
        regBtn = findViewById(R.id.register);
        toLoginBtn=findViewById(R.id.toLogin);
    }
}