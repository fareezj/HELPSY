package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fareez.helpsy.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    CardView logout_btn, availableEvents, userSettings, joinedEvents, nfnNews, postLocation, accomplishment, aboutNFN;
    ImageButton refresh;
    TextView userName;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        logout_btn =  findViewById(R.id.logout_btn);
        userName   =  findViewById(R.id.user_name);
        availableEvents = findViewById(R.id.available_events);
        userSettings = findViewById(R.id.user_settings);
        refresh = findViewById(R.id.refresh_btn);
        joinedEvents = findViewById(R.id.joined_event_list);
        nfnNews = findViewById(R.id.nfn_news_list);
        postLocation = findViewById(R.id.donation_post_location);
        accomplishment = findViewById(R.id.accomplishment);
        aboutNFN = findViewById(R.id.aboutNFNBtn);

        userName.setText(Prevalent.currentOnlineUser.getUsername());
        CircleImageView profileImageView = findViewById(R.id.user_profile_image);
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.user).into(profileImageView);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                UsersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        CircleImageView profileImageView = findViewById(R.id.user_profile_image);
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("username").getValue().toString();

                        userName.setText(name);
                        Picasso.get().load(image).placeholder(R.drawable.user).into(profileImageView);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }


                });
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Toast.makeText(HomeActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        availableEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),EventListActivity.class));
            }
        });

        userSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),ProfileSettingsActivity.class));
            }
        });

        joinedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),JoinedEventsActivity.class));
            }
        });

        nfnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),NewsListActivity.class));
            }
        });

        postLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),DonationMapsActivity.class));
            }
        });

        accomplishment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),AccomplishmentActivity.class));
            }
        });

        aboutNFN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),AboutNFNActivity.class));
            }
        });




    }


}
