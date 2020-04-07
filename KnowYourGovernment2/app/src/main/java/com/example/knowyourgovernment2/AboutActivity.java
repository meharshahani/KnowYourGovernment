package com.example.knowyourgovernment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView googleApiInfo = findViewById(R.id.googleApiInfo);

        googleApiInfo.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        googleApiInfo.setText("̲\uD835\uDDA6̲\uD835\uDDC8̲\uD835\uDDC8̲\uD835\uDDC0̲\uD835\uDDC5̲\uD835\uDDBE̲ ̲\uD835\uDDA2̲\uD835\uDDC2̲\uD835\uDDCF̲\uD835\uDDC2̲\uD835\uDDBC̲ ̲\uD835\uDDA8̲\uD835\uDDC7̲\uD835\uDDBF̲\uD835\uDDC8̲\uD835\uDDCB̲\uD835\uDDC6̲\uD835\uDDBA̲\uD835\uDDCD̲\uD835\uDDC2̲\uD835\uDDC8̲\uD835\uDDC7̲ ̲\uD835\uDDA0̲\uD835\uDDAF̲\uD835\uDDA8̲");

        googleApiInfo.setTextSize(23);
        googleApiInfo.setTextColor(Color.WHITE);
        googleApiInfo.setGravity(Gravity.CENTER);

        final String apiUrl = "https://developers.google.com/civic-information/";

        googleApiInfo.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(apiUrl));
                startActivity(i);
            }
        });
    }
}
