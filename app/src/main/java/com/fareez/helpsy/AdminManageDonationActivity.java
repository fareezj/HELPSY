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
import com.fareez.helpsy.Model.PostMap;
import com.fareez.helpsy.ViewHolder.DonationPostViewHolder;
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

public class AdminManageDonationActivity extends AppCompatActivity {

    private DatabaseReference PostRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_donation);

        PostRef = FirebaseDatabase.getInstance().getReference().child("Donation Post").child("Locations");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<PostMap> options =
                new FirebaseRecyclerOptions.Builder<PostMap>()
                        .setQuery(PostRef, PostMap.class)
                        .build();

        FirebaseRecyclerAdapter<PostMap, DonationPostViewHolder> adapter =
                new FirebaseRecyclerAdapter<PostMap, DonationPostViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull DonationPostViewHolder holder, int position, @NonNull final PostMap model)
                    {

                        holder.txtPostName.setText(model.getPid());
                        holder.txtFoodLvl.setText(model.getFoodLevel());
                        holder.txtBookLvl.setText(model.getBookLevel());
                        holder.txtClothLvl.setText(model.getClothLevel());
                        holder.txtStatus.setText(model.getStatus());


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(AdminManageDonationActivity.this, AdminManageDonationDetailsActivity.class);
                                intent.putExtra("postID", model.getPid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public DonationPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_post_layout, parent, false);
                        DonationPostViewHolder holder = new DonationPostViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}

