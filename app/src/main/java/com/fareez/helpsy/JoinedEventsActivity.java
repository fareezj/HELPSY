package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fareez.helpsy.Model.JoinedEvent;
import com.fareez.helpsy.ViewHolder.JoinedEventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class JoinedEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_events);

        recyclerView = findViewById(R.id.joined_event_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference joinedEventRef = FirebaseDatabase.getInstance().getReference().child("Volunteer List")
                .child("User View")
                .child(uid);


        FirebaseRecyclerOptions<JoinedEvent> options =
                new FirebaseRecyclerOptions.Builder<JoinedEvent>()
                .setQuery(joinedEventRef, JoinedEvent.class)
                        .build();

        FirebaseRecyclerAdapter<JoinedEvent, JoinedEventViewHolder> adapter
                = new FirebaseRecyclerAdapter<JoinedEvent, JoinedEventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull JoinedEventViewHolder joinedEventViewHolder, int position, @NonNull final JoinedEvent model)
            {
                joinedEventViewHolder.txtEventName.setText(model.getEventName());
                joinedEventViewHolder.txtEventDate.setText(model.getEventDate());
                Picasso.get().load(model.getEventImage()).into(joinedEventViewHolder.joinedEventImage);

                joinedEventViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinedEventsActivity.this);
                        builder.setTitle("Event Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                if(i == 0)
                                {
                                    joinedEventRef.child(model.geteid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) 
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        //DELETE FROM ADMIN VIEW
                                                        DatabaseReference eventRefAdmin = FirebaseDatabase.getInstance().getReference()
                                                                .child("Volunteer List")
                                                                .child("Admin View")
                                                                .child("Events")
                                                                .child(model.geteid())
                                                                .child("Volunteers")
                                                                .child(uid);
                                                        eventRefAdmin.removeValue();


                                                        Toast.makeText(JoinedEventsActivity.this, "Event Removed From Joined List", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(JoinedEventsActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public JoinedEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_event_layout, parent, false);
                JoinedEventViewHolder holder = new JoinedEventViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
