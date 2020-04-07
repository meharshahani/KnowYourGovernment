package com.example.knowyourgovernment2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder>
{
    private ArrayList<Official> officialList;
    private MainActivity mainActivity;

    public Adapter(ArrayList<Official> officialList, MainActivity mainActivity)
    {
        this.officialList = officialList;
        this.mainActivity = mainActivity;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        Official official = officialList.get(position);
        holder.officeName.setText(official.getOfficeName());
        holder.officialName.setText(official.getOfficialName());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        view.setOnClickListener(mainActivity);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount()
    {
        return officialList.size();
    }
}









