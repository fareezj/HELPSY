package com.fareez.helpsy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rey.material.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity {

    private CardView publishEvent, manageEvent, publishNews, manageNews, donationPost, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        publishEvent = findViewById(R.id.admin_publish_event);
        manageEvent = findViewById(R.id.admin_manage_events);
        publishNews = findViewById(R.id.admin_donation_list);
        manageNews = findViewById(R.id.admin_manage_news);
        donationPost = findViewById(R.id.donation_post_status);
        logoutBtn = findViewById(R.id.admin_logout);

        CircleImageView adminImageView = findViewById(R.id.admin_profile_image);
        adminImageView.setImageResource(R.drawable.nfn);


        publishEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminPublishEventActivity.class);
                startActivity(intent);
            }
        });

        manageEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminManageEventListActivity.class);
                startActivity(intent);
            }
        });

        publishNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminPublishNewsActivity.class);
                startActivity(intent);
            }
        });

        manageNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminManageNewsList.class);
                startActivity(intent);
            }
        });

        donationPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminManageDonationActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Toast.makeText(AdminHomeActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}
