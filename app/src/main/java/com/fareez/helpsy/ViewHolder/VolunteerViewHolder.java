package com.fareez.helpsy.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.fareez.helpsy.Interface.ItemClickListener;
import com.fareez.helpsy.R;

public class VolunteerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtVolunteerName, txtVolunteerPhone, txtVolunteerEmail, txtVolunteerAttendance;
    public ImageView volunteerImage;
    private ItemClickListener itemClickListener;

    public VolunteerViewHolder(View itemView)
    {
        super(itemView);

        txtVolunteerName = itemView.findViewById(R.id.volunteer_name);
        txtVolunteerPhone = itemView.findViewById(R.id.volunteer_phone);
        txtVolunteerEmail = itemView.findViewById(R.id.volunteer_email);
        volunteerImage = itemView.findViewById(R.id.volunteer_image);
        txtVolunteerAttendance = itemView.findViewById(R.id.volunteer_attendance);
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
