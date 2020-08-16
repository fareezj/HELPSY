package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fareez.helpsy.Model.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminManageNewsDetailsActivity extends AppCompatActivity {

    ImageView newsImage;
    TextView txtNewsDate, txtNewsTitle, txtNewsContent, txtNewsAuthor;
    Button editNewsBtn, deleteNewsBtn;
    private String NewsID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_news_details);

        NewsID = getIntent().getStringExtra("nid");

        newsImage = findViewById(R.id.news_image_details);
        txtNewsDate = findViewById(R.id.news_date_details);
        txtNewsTitle = findViewById(R.id.news_title_details);
        txtNewsAuthor = findViewById(R.id.news_author_details);
        txtNewsContent = findViewById(R.id.news_content_details);
        editNewsBtn = findViewById(R.id.edit_news_btn);
        deleteNewsBtn = findViewById(R.id.delete_news_btn);

        getNewsDetails(NewsID);

        editNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageNewsDetailsActivity.this, AdminUpdateNewsActivity.class);
                intent.putExtra("eid", NewsID);
                startActivity(intent);
            }
        });

        deleteNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DELETE FROM NEWS LIST TREE
                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference()
                        .child("News List")
                        .child("Item")
                        .child(NewsID);
                eventRef.removeValue();


                Toast.makeText(AdminManageNewsDetailsActivity.this, "Event Deleted Successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AdminManageNewsDetailsActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getNewsDetails(String newsID)
    {
        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference().child("News List").child("Item");

        newsRef.child(newsID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    News news = dataSnapshot.getValue(News.class);

                    txtNewsDate.setText(news.getPublishDate());
                    txtNewsTitle.setText(news.getNewsTitle());
                    txtNewsAuthor.setText(news.getNewsAuthor());
                    txtNewsContent.setText(news.getNewsContent());
                    Picasso.get().load(news.getNewsImage()).into(newsImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
