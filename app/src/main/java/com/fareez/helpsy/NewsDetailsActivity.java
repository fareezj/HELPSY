package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fareez.helpsy.Model.Events;
import com.fareez.helpsy.Model.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NewsDetailsActivity extends AppCompatActivity {

    ImageView newsImage;
    TextView txtNewsDate, txtNewsTitle, txtNewsContent, txtNewsAuthor;
    private String NewsID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        NewsID = getIntent().getStringExtra("nid");

        newsImage = findViewById(R.id.news_image_details);
        txtNewsDate = findViewById(R.id.news_date_details);
        txtNewsTitle = findViewById(R.id.news_title_details);
        txtNewsAuthor = findViewById(R.id.news_author_details);
        txtNewsContent = findViewById(R.id.news_content_details);

        getNewsDetails(NewsID);
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
