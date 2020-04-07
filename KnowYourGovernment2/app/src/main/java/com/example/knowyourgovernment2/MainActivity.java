package com.example.knowyourgovernment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import static android.R.drawable.ic_dialog_alert;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private RecyclerView recyclerView;
    private ArrayList<Official> officialList = new ArrayList<>();
    private Adapter officialAdapter;
    private TextView mainLocation;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLocation = findViewById(R.id.mainLocation);

        recyclerView = findViewById(R.id.recycler);
        officialAdapter = new Adapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!appHasLocation())
        {
            requestLocationAccess();
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        String location = "null";
        if (appHasLocation())
            location = getLocation();
        if (location.equals("null")) return;

        // Download data only if there is nothing yet
        if (!officialList.isEmpty())
            return;
        downloadRepresentatives(location);
    }

    private static int MY_LOCATION_REQUEST_CODE_ID = 329;
    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID)
        {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
            else
                mainLocation.setText("Location services denied");
        }
    }
    private boolean appHasLocation()
    {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ;
    }

    public void requestLocationAccess()
    {
        ActivityCompat.requestPermissions(this, new String[]
        {
                Manifest.permission.ACCESS_FINE_LOCATION
        },
                MY_LOCATION_REQUEST_CODE_ID);
    }
    @SuppressLint("MissingPermission")
    private String getLocation()
    {
        String bestProvider = "network";
        Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation == null)
            currentLocation = new Location(bestProvider);

        // Location permission ok but GPS not activated
        if( (currentLocation == null) || (currentLocation.getLatitude()==0.0 && currentLocation.getLongitude()==0.0) )
            return "null";
        else
        {
            return String.format(Locale.getDefault(),"%.4f, %.4f",
                    currentLocation.getLatitude(),currentLocation.getLongitude());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuInfo:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.menuSearch:
                searchDialog();
                break;
            default: break;
        }
        return true;
    }


    @Override
    public void onClick(View v)
    {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official officeToShow = officialList.get(pos);

        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra(Intent.ACTION_ATTACH_DATA, officeToShow);
        intent.putExtra("IntentLocation",mainLocation.getText().toString());

        startActivity(intent);
    }

    static boolean appHasNetwork(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null)
            return netInfo.isConnected();
        else return false;
    }

    private void downloadRepresentatives(String location)
    {
        if (appHasNetwork(this))
            new AsyncDataLoader(this, officialList, location).execute();
        else noNetworkDialog();
    }



    private void noNetworkDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Turn on WiFi or Network connectivity then close and relaunch the app");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void searchDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                downloadRepresentatives(et.getText().toString());
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });

        builder.setTitle("Enter a city/state or a zipcode:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updateRecyclerView()
    {
        officialAdapter.notifyDataSetChanged();
    }
    public void updateLocation(String location)
    {
        mainLocation.setText(location);
    }

}

























