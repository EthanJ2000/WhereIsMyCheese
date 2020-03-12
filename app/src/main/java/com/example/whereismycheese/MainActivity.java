package com.example.whereismycheese;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static Map mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("WhereIsMyCheese");
        initMap();
        LocationService locationService = new LocationService();
        locationService.buildGoogleApiClient(this);
        startService(new Intent(MainActivity.this,LocationService.class));
    }

    private void initMap() {
        mMap = new Map(this, MainActivity.this);
        mMap.initMap();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (LocationService.mGoogleApiClient.isConnecting() || !LocationService.mGoogleApiClient.isConnected()){
            LocationService.mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        startService(new Intent(getApplicationContext(),LocationService.class));
        super.onStop();
    }

}
