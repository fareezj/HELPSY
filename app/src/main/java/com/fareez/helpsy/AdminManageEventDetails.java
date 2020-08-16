package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fareez.helpsy.Model.Events;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminManageEventDetails extends AppCompatActivity {

    ImageView eventImage;
    TextView eventName, eventDate, eventPax, eventDesc;
    Button editEventBtn, volunteerListBtn, deleteEventBtn;
    private String eventID = "";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_event_details);

        eventID = getIntent().getStringExtra("eid");


        eventImage = findViewById(R.id.event_image_details);
        eventName = findViewById(R.id.event_name_details);
        eventDate = findViewById(R.id.event_date_details);
        eventPax = findViewById(R.id.event_pax_details);
        eventDesc = findViewById(R.id.event_desc_details);
        volunteerListBtn = findViewById(R.id.volunteer_list_btn);
        editEventBtn = findViewById(R.id.edit_event_btn);
        deleteEventBtn = findViewById(R.id.delete_event_btn);

        getEventDetails(eventID);

        volunteerListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageEventDetails.this, AdminVolunteerList.class);
                intent.putExtra("eid", eventID);
                startActivity(intent);
            }
        });

        editEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageEventDetails.this, AdminUpdateEvent.class);
                intent.putExtra("eid", eventID);
                startActivity(intent);
            }
        });

        deleteEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DELETE FROM EVENT LIST TREE
                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference()
                        .child("Event List")
                        .child("Item")
                        .child(eventID);
                eventRef.removeValue();

                //DELETE FROM ADMIN VIEW
                DatabaseReference eventRefAdmin = FirebaseDatabase.getInstance().getReference()
                        .child("Volunteer List")
                        .child("Admin View")
                        .child("Events")
                        .child(eventID);
                eventRefAdmin.removeValue();

                //DELETE FROM USER VIEW
                DatabaseReference eventRefUser = FirebaseDatabase.getInstance().getReference()
                        .child("Volunteer List")
                        .child("User View");

                eventRefUser.orderByChild(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            String userKeys = childSnapshot.getKey();
                            if(userKeys != null){
                                DatabaseReference eventRefUser = FirebaseDatabase.getInstance().getReference()
                                        .child("Volunteer List")
                                        .child("User View")
                                        .child(userKeys)
                                        .child(eventID);
                                eventRefUser.removeValue();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Toast.makeText(AdminManageEventDetails.this, "Event Deleted Successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AdminManageEventDetails.this, AdminHomeActivity.class);
                startActivity(intent);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}

