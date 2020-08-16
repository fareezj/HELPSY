package com.fareez.helpsy.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fareez.helpsy.Interface.ItemClickListener;
import com.fareez.helpsy.R;

public class JoinedEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtEventName, txtEventDate;
    public ImageView joinedEventImage;
    private ItemClickListener itemClickListener;

    public JoinedEventViewHolder(View itemView)
    {
        super(itemView);

        txtEventName = itemView.findViewById(R.id.joined_event_name);
        txtEventDate = itemView.findViewById(R.id.joined_event_date);
        joinedEventImage = itemView.findViewById(R.id.joined_event_image);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }


}
