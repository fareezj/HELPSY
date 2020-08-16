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

import com.fareez.helpsy.Model.Events;
import com.fareez.helpsy.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminManageEventListActivity extends AppCompatActivity {

    private DatabaseReference EventsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        EventsRef = FirebaseDatabase.getInstance().getReference().child("Event List").child("Item");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>()
                        .setQuery(EventsRef, Events.class)
                        .build();

        FirebaseRecyclerAdapter<Events, EventViewHolder> adapter =
                new FirebaseRecyclerAdapter<Events, EventViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull final Events model)
                    {
                        holder.txtEventName.setText(model.getEventName());
                        holder.txtEventDate.setText("Event Date:" + model.getEventDate());
                        //holder.txtEventPax.setText("Total Volunteers:" + model.getEventPax());
                        holder.txtPublishDate.setText("Published at:" +model.getPublishDate());
                        Picasso.get().load(model.getEventImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(AdminManageEventListActivity.this, AdminManageEventDetails.class);
                                intent.putExtra("eid", model.getEid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout, parent, false);
                        EventViewHolder holder = new EventViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
