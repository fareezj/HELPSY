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

import com.fareez.helpsy.Model.Events;
import com.fareez.helpsy.Model.Users;
import com.fareez.helpsy.Prevalent.Prevalent;
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

public class EventDetailsActivity extends AppCompatActivity {

    ImageView eventImage;
    TextView eventName, eventDate, eventPax, eventDesc;
    Button joinEventBtn;
    private String eventID = "";
    String eventImageURL = "";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventID = getIntent().getStringExtra("eid");


        eventImage = findViewById(R.id.event_image_details);
        eventName = findViewById(R.id.event_name_details);
        eventDate = findViewById(R.id.event_date_details);
        eventPax = findViewById(R.id.event_pax_details);
        eventDesc = findViewById(R.id.event_desc_details);
        joinEventBtn = findViewById(R.id.join_event_btn);


        getEventDetails(eventID);

        joinEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addToVolunteeringList();
            }
        });
    }

    private void addToVolunteeringList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference volunteersListRef = FirebaseDatabase.getInstance().getReference().child("Volunteer List");

        final HashMap<String, Object> volunteerMap = new HashMap<>();
        volunteerMap.put("eid", eventID);
        volunteerMap.put("eventName", eventName.getText().toString());
        volunteerMap.put("eventDate", eventDate.getText().toString());
        volunteerMap.put("eventPax", eventPax.getText().toString());
        volunteerMap.put("eventDesc", eventDesc.getText().toString());
        volunteerMap.put("eventImage", eventImageURL);
        volunteerMap.put("date", saveCurrentDate);
        volunteerMap.put("time", saveCurrentTime);


        volunteersListRef.child("User View")
                .child(uid)
                .child(eventID)
                .updateChildren(volunteerMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {


                            //GET USER DETAILS
                            final DatabaseReference joinUserRef = FirebaseDatabase.getInstance().getReference().child("Volunteer List");


                            final HashMap<String, Object> volunteerUserMap = new HashMap<>();
                            volunteerUserMap.put("username", Prevalent.currentOnlineUser.getUsername());
                            volunteerUserMap.put("email", Prevalent.currentOnlineUser.getEmail());
                            volunteerUserMap.put("phone", Prevalent.currentOnlineUser.getPhone());
                            volunteerUserMap.put("address", Prevalent.currentOnlineUser.getAddress());
                            volunteerUserMap.put("image", Prevalent.currentOnlineUser.getImage());


                            joinUserRef.child("Admin View")
                                    .child("Events").child(eventID).child("Volunteers").child(uid)
                                    .updateChildren(volunteerUserMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) 
                                        {
                                            Toast.makeText(EventDetailsActivity.this, "Event Successfully Joined !", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(EventDetailsActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        }
                    }
                });

    }

    private void getEventDetails(String eventID)
    {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Event List").child("Item");

        eventRef.child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Events events = dataSnapshot.getValue(Events.class);

                    eventName.setText(events.getEventName());
                    eventDate.setText(events.getEventDate());
                    //eventPax.setText(events.getEventPax());
                    eventDesc.setText(events.getEventDesc());
                    Picasso.get().load(events.getEventImage()).into(eventImage);
                    eventImageURL = events.getEventImage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}
