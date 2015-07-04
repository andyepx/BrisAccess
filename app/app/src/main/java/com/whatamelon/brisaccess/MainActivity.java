package com.whatamelon.brisaccess;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    final String TAG = "BrisAccess";
    private static final LatLng DEFAULT_LOCATION = new LatLng(-27.498037,153.017823);
    private LatLng userLatLng;
    private Marker userMarker;

    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;
    private SupportMapFragment mapFrag;
    private GoogleMap mMap;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mapInit();
    }

    public void mapInit() {
        LatLng center = DEFAULT_LOCATION;

        mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mMap = mapFrag.getMap();

        while (mMap == null) {
            // The application is still unable to load the map.
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location)
    {
        userLatLng = new LatLng(location.getLatitude(),
                location.getLongitude());

        userMarker = mMap.addMarker(new MarkerOptions()
                .position(DEFAULT_LOCATION)
                .title("Current location")
                .visible(false)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.location_geo_border)));

        userMarker.setVisible(true);
        userMarker.setPosition(userLatLng);

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
}
