package com.fareez.helpsy.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fareez.helpsy.Interface.ItemClickListener;
import com.fareez.helpsy.R;

public class DonationPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtPostName, txtFoodLvl, txtBookLvl, txtClothLvl, txtStatus;
    public ItemClickListener listener;

    public DonationPostViewHolder(View itemView) {
        super(itemView);


        txtPostName = itemView.findViewById(R.id.post_Name_Input);
        txtFoodLvl = itemView.findViewById(R.id.post_FoodLevel_Input);
        txtBookLvl = itemView.findViewById(R.id.post_BookLevel_Input);
        txtClothLvl = itemView.findViewById(R.id.post_ClothLevel_Input);
        txtStatus = itemView.findViewById(R.id.post_Status_Input);

    }


    public void setItemClickListner(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
