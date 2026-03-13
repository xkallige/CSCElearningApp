package com.example.csce_learningapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private Context context;

    public CourseAdapter(List<Course> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.titleTextView.setText(course.getTitle());
        holder.descriptionTextView.setText(course.getDescription());

        holder.viewButton.setOnClickListener(v -> {
            String url = course.getFileUrl();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        });

        holder.downloadButton.setOnClickListener(v -> {
            String url = course.getFileUrl();
            if (url != null && !url.isEmpty()) {
                downloadFile(course.getTitle(), url);
            }
        });
    }

    private void downloadFile(String courseTitle, String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(courseTitle);
        request.setDescription("Downloading course material...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, courseTitle + ".pdf"); // Assuming PDF for now

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Toast.makeText(context, "Download started!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        Button viewButton, downloadButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.courseTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.courseDescriptionTextView);
            viewButton = itemView.findViewById(R.id.viewCourseButton);
            downloadButton = itemView.findViewById(R.id.downloadCourseButton);
        }
    }
}