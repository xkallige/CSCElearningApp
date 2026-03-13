package com.example.csce_learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText regNoEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        regNoEditText = findViewById(R.id.loginRegNoEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.loginProgressBar);
        TextView signupTextView = findViewById(R.id.signupTextView);

        loginButton.setOnClickListener(v -> loginUser());
        signupTextView.setOnClickListener(v -> startActivity(
                new Intent(LoginActivity.this, SignupActivity.class)));
    }

    private void loginUser() {
        String regNo = regNoEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(regNo) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        db.collection("users").whereEqualTo("regNo", regNo).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String email = "";
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            email = document.getString("email");
                        }
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, authTask -> {
                                    if (authTask.isSuccessful()) {
                                        checkUserRole();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        loginButton.setEnabled(true);
                                        Toast.makeText(LoginActivity.this, "Login failed: " + authTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        Toast.makeText(this, "Registration Number not found", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        navigateBasedOnRole(role);
                    }
                });
    }

    private void navigateBasedOnRole(String role) {
        progressBar.setVisibility(View.GONE);
        if ("admin".equals(role)) {
            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        finish();
    }
}