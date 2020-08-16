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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fareez.helpsy.Model.Events;
import com.fareez.helpsy.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EventListActivity extends AppCompatActivity {

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
                      holder.txtEventDate.setText(model.getEventDate());
                      //holder.txtEventPax.setText(model.getEventPax());
                      holder.txtPublishDate.setText(model.getPublishDate());
                      Picasso.get().load(model.getEventImage()).into(holder.imageView);

                      holder.itemView.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v)
                          {
                              Intent intent = new Intent(EventListActivity.this, EventDetailsActivity.class);
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

//    public static class AdminManageEventDetailsActivity extends AppCompatActivity {
//
//        ImageView eventImage;
//        TextView eventName, eventDate, eventPax, eventDesc;
//        Button joinEventBtn;
//        private String eventID = "";
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = user.getUid();
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_event_details);
//
//            eventID = getIntent().getStringExtra("eid");
//
//
//            eventImage = findViewById(R.id.event_image_details);
//            eventName = findViewById(R.id.event_name_details);
//            eventDate = findViewById(R.id.event_date_details);
//            eventPax = findViewById(R.id.event_pax_details);
//            eventDesc = findViewById(R.id.event_desc_details);
//            joinEventBtn = findViewById(R.id.join_event_btn);
//
//            getEventDetails(eventID);
//
//            joinEventBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v)
//                {
//                    addToVolunteeringList();
//                }
//            });
//        }
//
//        private void addToVolunteeringList()
//        {
//            String saveCurrentTime, saveCurrentDate;
//
//            Calendar callForDate = Calendar.getInstance();
//            SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
//            saveCurrentDate = currentDate.format(callForDate.getTime());
//
//            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//            saveCurrentTime = currentTime.format(callForDate.getTime());
//
//            final DatabaseReference volunteersListRef = FirebaseDatabase.getInstance().getReference().child("Volunteer List");
//
//            final HashMap<String, Object> volunteerMap = new HashMap<>();
//            volunteerMap.put("eid", eventID);
//            volunteerMap.put("eventName", eventName.getText().toString());
//            volunteerMap.put("eventDate", eventDate.getText().toString());
//            volunteerMap.put("eventPax", eventPax.getText().toString());
//            volunteerMap.put("eventDesc", eventDesc.getText().toString());
//            volunteerMap.put("date", saveCurrentDate);
//            volunteerMap.put("time", saveCurrentTime);
//
//
//            volunteersListRef.child("User View")
//                    .child(uid)
//                    .child("Events").child(eventID)
//                    .updateChildren(volunteerMap)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task)
//                        {
//                            if(task.isSuccessful())
//                            {
//                                volunteersListRef.child("Admin View")
//                                        .child("Events").child(eventID).child("Volunteers").child(uid)
//                                        .updateChildren(volunteerMap)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task)
//                                            {
//                                                Toast.makeText(AdminManageEventDetailsActivity.this, "Event Successfully Joined !", Toast.LENGTH_SHORT).show();
//
//                                                Intent intent = new Intent(AdminManageEventDetailsActivity.this, AdminHomeActivity.class);
//                                                startActivity(intent);
//                                            }
//                                        });
//                            }
//                        }
//                    });
//
//        }
//
//        private void getEventDetails(String eventID)
//        {
//            DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Event List").child("Item");
//
//            eventRef.child(eventID).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                {
//                    if(dataSnapshot.exists())
//                    {
//                        Events events = dataSnapshot.getValue(Events.class);
//
//                        eventName.setText(events.getEventName());
//                        eventDate.setText(events.getEventDate());
//                        eventPax.setText(events.getEventPax());
//                        eventDesc.setText(events.getEventDesc());
//                        Picasso.get().load(events.getEventImage()).into(eventImage);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError)
//                {
//
//                }
//            });
//        }
//    }
}
