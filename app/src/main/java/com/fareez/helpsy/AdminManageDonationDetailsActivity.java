package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fareez.helpsy.Model.Events;
import com.fareez.helpsy.Model.PostMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminManageDonationDetailsActivity extends AppCompatActivity {

    public TextView txtPostName, txtFoodLvl, txtBookLvl, txtClothLvl, txtStatus, txtSSID, txtSignalStrength, txtSecurity;

    Button enablePostBtn, disablePostBtn;
    private String postID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_donation_details);

        postID = getIntent().getStringExtra("postID");

        txtPostName = findViewById(R.id.post_Name_Details);
        txtFoodLvl = findViewById(R.id.post_FoodLevel_Details);
        txtBookLvl = findViewById(R.id.post_BookLevel_Details);
        txtClothLvl = findViewById(R.id.post_ClothLevel_Details);
        txtStatus = findViewById(R.id.post_Status_Details);
        txtSSID = findViewById(R.id.post_SSID_Details);
        txtSignalStrength = findViewById(R.id.post_SignalStrength_Details);
        txtSecurity = findViewById(R.id.post_wirelessSecurity_Details);
        enablePostBtn = findViewById(R.id.enable_post_btn);
        disablePostBtn = findViewById(R.id.disable_post_btn);

        getPostDetails(postID);

        enablePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String enable = "Online";
                updatePostStatus(enable);
            }
        });

        disablePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String disable = "Offline";
                updatePostStatus(disable);
            }
        });

    }

    private void updatePostStatus(String status)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Donation Post")
                .child("Locations")
                .child(postID);

        HashMap<String, Object> post = new HashMap<>();
        post.put("status", status);
        ref.updateChildren(post);

    }

    private void getPostDetails(String postID)
    {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Donation Post").child("Locations");

        postRef.child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    PostMap post = dataSnapshot.getValue(PostMap.class);


                    txtPostName.setText(post.getPid());
                    txtFoodLvl.setText(post.getFoodLevel());
                    txtBookLvl.setText(post.getBookLevel());
                    txtClothLvl.setText(post.getClothLevel());
                    txtStatus.setText(post.getStatus());
                    txtSSID.setText(post.getNetworkSSID());
                    txtSignalStrength.setText(post.getSignalStrength());
                    txtSecurity.setText(post.getSecurity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}
