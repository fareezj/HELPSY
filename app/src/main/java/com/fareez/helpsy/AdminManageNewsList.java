package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fareez.helpsy.Model.News;
import com.fareez.helpsy.ViewHolder.NewsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminManageNewsList extends AppCompatActivity {

    private DatabaseReference NewsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_news_list);

        NewsRef = FirebaseDatabase.getInstance().getReference()
                .child("News List")
                .child("Item");

        recyclerView = findViewById(R.id.recycler_news_admin);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<News> options =
                new FirebaseRecyclerOptions.Builder<News>()
                        .setQuery(NewsRef, News.class)
                        .build();

        FirebaseRecyclerAdapter<News, NewsViewHolder> adapter =
                new FirebaseRecyclerAdapter<News, NewsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull final News model)
                    {
                        YoYo.with(Techniques.BounceIn).playOn(holder.newsList);
                        holder.txtNewsDate.setText(model.getPublishDate());
                        holder.txtNewsTitle.setText(model.getNewsTitle());
                        holder.txtNewsContent.setText(model.getNewsContent());
                        Picasso.get().load(model.getNewsImage()).into(holder.newsImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(AdminManageNewsList.this, AdminManageNewsDetailsActivity.class);
                                intent.putExtra("nid", model.getNid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_layout, parent, false);
                        NewsViewHolder holder = new NewsViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
