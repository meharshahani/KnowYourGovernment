package com.example.knowyourgovernment2;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder
{
    public TextView officeName;
    public TextView officialName;

    public ViewHolder(View itemView)
    {
        super(itemView);
        officeName = (TextView) itemView.findViewById(R.id.officeName);
        officialName = (TextView) itemView.findViewById(R.id.photoName);
    }
}
