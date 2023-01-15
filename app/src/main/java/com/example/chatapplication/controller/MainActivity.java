package com.example.chatapplication.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapplication.R;
import com.example.chatapplication.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtEmail;
    private Button btnSubmit;
    private TextView txtLoginInfo;

    private boolean isSigningUp = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);

        btnSubmit = findViewById(R.id.btmSubmit);

        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
            finish();
        }

        btnSubmit.setOnClickListener(view -> {

            if (edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
                if (isSigningUp && edtUsername.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (isSigningUp) {
                handleSignUp();
            } else {
                handleLogin();
            }
        });

        txtLoginInfo.setOnClickListener(view -> {
            if (isSigningUp) {
                isSigningUp = false;
                edtUsername.setVisibility(View.GONE);
                btnSubmit.setText("Log in");
                txtLoginInfo.setText("Don't have an account? Sign up");
            } else {
                isSigningUp = true;
                edtUsername.setVisibility(View.VISIBLE);
                btnSubmit.setText("Sign up");
                txtLoginInfo.setText("Already have an account? Log in");
            }
        });
    }

    private void handleLogin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSignUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference("user/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(new User(edtUsername.getText().toString(), edtEmail.getText().toString(), ""));
                startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                Toast.makeText(MainActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}