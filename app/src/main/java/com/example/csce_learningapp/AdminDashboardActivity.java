package com.example.csce_learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminDashboardActivity extends AppCompatActivity {

    private EditText courseTitleEditText, courseDescEditText, courseFileLinkEditText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();
        courseTitleEditText = findViewById(R.id.courseTitleEditText);
        courseDescEditText = findViewById(R.id.courseDescEditText);
        courseFileLinkEditText = findViewById(R.id.courseFileLinkEditText);
        Button uploadCourseButton = findViewById(R.id.uploadCourseButton);
        Button logoutButton = findViewById(R.id.adminLogoutButton);

        uploadCourseButton.setOnClickListener(v -> uploadCourse());
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void uploadCourse() {
        String title = courseTitleEditText.getText().toString().trim();
        String description = courseDescEditText.getText().toString().trim();
        String fileLink = courseFileLinkEditText.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || fileLink.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> course = new HashMap<>();
        course.put("title", title);
        course.put("description", description);
        course.put("fileUrl", fileLink); // Saving the link here

        db.collection("courses").add(course)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AdminDashboardActivity.this, "Course uploaded!", Toast.LENGTH_SHORT).show();
                    courseTitleEditText.setText("");
                    courseDescEditText.setText("");
                    courseFileLinkEditText.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(AdminDashboardActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}