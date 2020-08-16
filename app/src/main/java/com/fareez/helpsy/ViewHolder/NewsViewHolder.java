package com.fareez.helpsy.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fareez.helpsy.Interface.ItemClickListener;
import com.fareez.helpsy.R;

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtNewsDate, txtNewsTitle, txtNewsContent;
    public ImageView newsImage;
    public CardView newsList;
    public ItemClickListener listener;

    public NewsViewHolder(View itemView) {
        super(itemView);

        newsImage = itemView.findViewById(R.id.thumb_img);
        txtNewsDate = itemView.findViewById(R.id.date_text);
        txtNewsTitle = itemView.findViewById(R.id.title_text);
        txtNewsContent = itemView.findViewById(R.id.content_text);
        newsList = itemView.findViewById(R.id.cardview);
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
