package com.fareez.helpsy.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fareez.helpsy.Interface.ItemClickListener;
import com.fareez.helpsy.R;

public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtEventName, txtEventDate, txtEventPax, txtPublishDate;
    public ImageView imageView;
    public ItemClickListener listener;

    public EventViewHolder(View itemView) {
        super(itemView);


        imageView = itemView.findViewById(R.id.event_image);
        txtEventName = itemView.findViewById(R.id.event_name);
        txtEventDate = itemView.findViewById(R.id.event_date);
        txtEventPax = itemView.findViewById(R.id.event_pax);
        txtPublishDate = itemView.findViewById(R.id.publish_date);
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
