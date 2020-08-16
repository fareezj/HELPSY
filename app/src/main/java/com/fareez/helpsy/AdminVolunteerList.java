package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fareez.helpsy.Model.Events;
import com.fareez.helpsy.Model.Users;
import com.fareez.helpsy.ViewHolder.EventViewHolder;
import com.fareez.helpsy.ViewHolder.VolunteerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminVolunteerList extends AppCompatActivity {

    private DatabaseReference volunteerRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String eventID = "";
    private String userID = "";
    private String attendanceStatus = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_volunteer_list);

        eventID = getIntent().getStringExtra("eid");


        volunteerRef = FirebaseDatabase.getInstance().getReference()
                .child("Volunteer List")
                .child("Admin View")
                .child("Events")
                .child(eventID)
                .child("Volunteers");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(volunteerRef, Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users, VolunteerViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, VolunteerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final VolunteerViewHolder volunteerViewHolder, int i, @NonNull final Users users)
                    {
                        volunteerViewHolder.txtVolunteerName.setText(users.getUsername());
                        volunteerViewHolder.txtVolunteerPhone.setText(users.getPhone());
                        volunteerViewHolder.txtVolunteerEmail.setText(users.getEmail());
                        volunteerViewHolder.txtVolunteerAttendance.setText(users.getStatus());
                        Picasso.get().load(users.getImage()).into(volunteerViewHolder.volunteerImage);

                        //UPDATE VOLUNTEER'S ATTENDANCE

                        volunteerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Attend",
                                                "Absent"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminVolunteerList.this);
                                builder.setTitle("Event Options:");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i)
                                    {
                                        // 1) Set Attendance Status
                                        if(i == 0)
                                        {

                                            Log.d("MAIN", "Attendees:" + users.getUsername());
                                            String catchedUsername = users.getUsername();

                                            // 2) Retrieve Volunteer ID
                                            DatabaseReference eventRefUser = FirebaseDatabase.getInstance().getReference()
                                                    .child("Volunteer List")
                                                    .child("Admin View")
                                                    .child("Events")
                                                    .child(eventID)
                                                    .child("Volunteers");

                                            eventRefUser.orderByChild("username").equalTo(catchedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                                        final String userID = childSnapshot.getKey();
                                                        if(userID != null){
                                                            Log.d("MAIN", "Attendees Success:" + userID);

                                                            // 3) Update Accomplishment Database

                                                            final DatabaseReference AccomplisherRef = FirebaseDatabase.getInstance().getReference()
                                                                    .child("Accomplishment")
                                                                    .child("Volunteers")
                                                                    .child(userID);

                                                            AccomplisherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot)
                                                                {
                                                                    if (dataSnapshot.exists())
                                                                    {

                                                                        String star = dataSnapshot.child("stars").getValue().toString();
                                                                        int stars = Integer.parseInt(star);
                                                                        int marks = stars + 10;

                                                                        HashMap<String, Object> eventMap = new HashMap<>();
                                                                        eventMap.put("stars", marks);

                                                                        AccomplisherRef.updateChildren(eventMap)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                                    {
                                                                                        if(task.isSuccessful())
                                                                                        {
                                                                                            volunteerViewHolder.txtVolunteerAttendance.setText("Present");
                                                                                            updateAttendanceStatus("Attend", eventID, userID);
                                                                                            attendanceStatus = "Attend";
                                                                                            Toast.makeText(AdminVolunteerList.this, "Saved !", Toast.LENGTH_SHORT ).show();
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            String message = task.getException().toString();
                                                                                            Toast.makeText(AdminVolunteerList.this, "Error :" + message, Toast.LENGTH_SHORT ).show();

                                                                                        }
                                                                                    }
                                                                                });

                                                                    }else{
                                                                        HashMap<String, Object> eventMap = new HashMap<>();
                                                                        eventMap.put("stars", 10);

                                                                        AccomplisherRef.updateChildren(eventMap)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                                    {
                                                                                        if(task.isSuccessful())
                                                                                        {
                                                                                            updateAttendanceStatus("Present", eventID, userID);
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            String message = task.getException().toString();
                                                                                            Toast.makeText(AdminVolunteerList.this, "Error :" + message, Toast.LENGTH_SHORT ).show();

                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });


                                                            }
                                                        }
                                                    }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                        else {
                                            Log.d("MAIN", "Attendees:" + users.getUsername());
                                            String catchedUsername = users.getUsername();

                                            // 2) Retrieve Volunteer ID
                                            DatabaseReference eventRefUser = FirebaseDatabase.getInstance().getReference()
                                                    .child("Volunteer List")
                                                    .child("Admin View")
                                                    .child("Events")
                                                    .child(eventID)
                                                    .child("Volunteers");

                                            eventRefUser.orderByChild("username").equalTo(catchedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                                        final String userID = childSnapshot.getKey();
                                                        if(userID != null){
                                                            Log.d("MAIN", "Attendees Success:" + userID);

                                                            // 3) Update Accomplishment Database

                                                            final DatabaseReference AccomplisherRef = FirebaseDatabase.getInstance().getReference()
                                                                    .child("Accomplishment")
                                                                    .child("Volunteers")
                                                                    .child(userID);

                                                            AccomplisherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot)
                                                                {
                                                                    if (dataSnapshot.exists())
                                                                    {

                                                                        String star = dataSnapshot.child("stars").getValue().toString();

                                                                        int stars = Integer.parseInt(star);
                                                                        int marks = stars + 10;

                                                                        HashMap<String, Object> eventMap = new HashMap<>();
                                                                        eventMap.put("stars", marks);

                                                                        AccomplisherRef.updateChildren(eventMap)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                                    {
                                                                                        if(task.isSuccessful())
                                                                                        {
                                                                                            volunteerViewHolder.txtVolunteerAttendance.setText("Absent");
                                                                                            updateAttendanceStatus("Absent", eventID, userID);
                                                                                            attendanceStatus = "Absent";
                                                                                            Toast.makeText(AdminVolunteerList.this, "Saved !", Toast.LENGTH_SHORT ).show();
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            String message = task.getException().toString();
                                                                                            Toast.makeText(AdminVolunteerList.this, "Error :" + message, Toast.LENGTH_SHORT ).show();

                                                                                        }
                                                                                    }
                                                                                });

                                                                    }else{
                                                                        HashMap<String, Object> eventMap = new HashMap<>();
                                                                        eventMap.put("stars", 10);

                                                                        AccomplisherRef.updateChildren(eventMap)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                                    {
                                                                                        if(task.isSuccessful())
                                                                                        {
                                                                                            updateAttendanceStatus("Present", eventID, userID);
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            String message = task.getException().toString();
                                                                                            Toast.makeText(AdminVolunteerList.this, "Error :" + message, Toast.LENGTH_SHORT ).show();

                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });


                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_list_layout, parent, false);
                        VolunteerViewHolder holder = new VolunteerViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    void updateAttendanceStatus(String status, String eventID, String userID){

        final DatabaseReference eventRefUser = FirebaseDatabase.getInstance().getReference()
                .child("Volunteer List")
                .child("Admin View")
                .child("Events")
                .child(eventID)
                .child("Volunteers")
                .child(userID);

        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put("status", status);


        eventRefUser.updateChildren(eventMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Log.i("Main", "Attendance Updated");
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Log.i("Main", "Attendance Error:" + message);

                        }
                    }
                });
    }


}



