package com.example.knowyourgovernment2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import static android.R.drawable.ic_dialog_alert;

public class PhotoActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        ImageView photoPhoto = findViewById(R.id.photoDetailPortrait);
        TextView photoOffice = findViewById(R.id.photoOffice);
        TextView photoName = findViewById(R.id.photoName);

        ConstraintLayout photoLayout = findViewById(R.id.photoLayout);

        ImageView photoLogo = findViewById(R.id.photoLogo);
        TextView photoLocation = findViewById(R.id.photoLocation);


        Intent intent = getIntent();

        if (intent.hasExtra(Intent.ACTION_ATTACH_DATA))
        {
            Official thisOffice = (Official) intent.getSerializableExtra(Intent.ACTION_ATTACH_DATA);
            photoLocation.setText(intent.getStringExtra("IntentLocation"));
            photoOffice.setText(thisOffice.getOfficeName());
            photoName.setText(thisOffice.getOfficialName());

            Picasso picasso = new Picasso.Builder(this).build();
            if (MainActivity.appHasNetwork(this))
                picasso.load(thisOffice.getImageURL()).error(R.drawable.missing)
                        .placeholder(R.drawable.placeholder)
                        .into(photoPhoto);
            else
                picasso.load(thisOffice.getImageURL()).error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(photoPhoto);

            String party = thisOffice.getParty();
            if (party.contains("Democratic Party"))
            {
                photoLayout.setBackgroundColor(Color.BLUE);
                photoLogo.setImageDrawable(getDrawable(R.drawable.dem_logo));
            }
            else if (party.contains("Republican Party"))
            {
                photoLayout.setBackgroundColor(Color.RED);
                photoLogo.setImageDrawable(getDrawable(R.drawable.rep_logo));
            }
            else
                photoLayout.setBackgroundColor(Color.BLACK);
        }
    }
}
