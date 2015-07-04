package com.whatamelon.brisaccess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.provider.SyncStateContract.Columns;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.location.Location;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FilterQueryProvider;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        JSONRequest.NetworkListener,
        OnClickListener,
        OnItemClickListener
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
    private AutoCompleteTextView destInput;

    private JSONRequest request;
    private HashMap<String, String> locationsToID = new HashMap<>();
    private List<String> requestParameters = new ArrayList<>();

    // Date/Time Settings
    private static Button dateButton;
    private static Button timeButton;
    static String date;
    static String time;
    static String dateDisplay;
    final static Calendar c = Calendar.getInstance();
    static int year;
    static int month;
    static int day;
    static int hour;
    static int minute;
    static Calendar currentDate = Calendar.getInstance();
    static Calendar selectedDate = Calendar.getInstance();

    static ProgressBar progressBar;

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

        SimpleCursorAdapter cursorAdapter = getSimpleCursorAdapter();
        destInput = (AutoCompleteTextView) findViewById(R.id.dest_input);
        destInput.setAdapter(cursorAdapter);
        destInput.setThreshold(3);
        destInput.setOnItemClickListener(this);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        currentDate.clear();
        currentDate.set(year, month, day);

        selectedDate.clear();
        selectedDate.set(year, month, day);

        date = (month+1) + "/" + day + "/" + year;
        dateDisplay = day + " / " + (month+1) + " / " + year;

        String temp;
        if(minute < 10)
            temp = "0" + minute;
        else
            temp = "" + minute;

        time = hour + ":" + temp;

        dateButton = (Button) findViewById(R.id.dateSpinner);
        timeButton = (Button) findViewById(R.id.timeSpinner);
        dateButton.setText(dateDisplay);
        timeButton.setText(time);
        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);

        Button goButton = (Button) findViewById(R.id.go_button);
        goButton.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
    }

    public static void showProgressBar(boolean visible)
    {
        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
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

    private SimpleCursorAdapter getSimpleCursorAdapter()
    {
        String[] from = { "locations" };
        int[] to = { android.R.id.text1 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, null, from, to, 0);
        adapter.setStringConversionColumn(1);

        FilterQueryProvider provider = new FilterQueryProvider()
        {
            @Override
            public Cursor runQuery(CharSequence constraint)
            {
                if (constraint == null || constraint.toString().equals("Current location"))
                    return null;

                String[] columnLocations = { Columns._ID, "locations" };
                MatrixCursor locationMatrix = new MatrixCursor(columnLocations);
                try
                {
                    String url = "http://dev-cloud.teardesign.com:3030/data/translink/suggest?input="
                            + Uri.encode(constraint.toString())
                            + "&maxResults=" + Uri.encode("5");

                    request = new JSONRequest();
                    request.setListener(MainActivity.this);
                    request.execute(url);

                    String result = request.get();

                    Object obj = JSONValue.parse(result);
                    JSONArray array = (JSONArray)((JSONObject)obj).get("Suggestions");

                    for(int i = 0; i < array.size(); i++)
                    {
                        JSONObject location = (JSONObject) array.get(i);
                        String desc = (String) location.get("Description");
                        String id = (String) location.get("Id");

                        String[] splitted = desc.split(" \\(");
                        if (splitted.length > 1)
                            desc = splitted[0];

                        locationsToID.put(desc, id);
                        locationMatrix.newRow().add(i).add(desc);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return locationMatrix;
            }
        };

        adapter.setFilterQueryProvider(provider);
        return adapter;
    }

    @Override
    public void onClick(View v)
    {
        DialogFragment newFragment;

        switch (v.getId())
        {
            case R.id.dateSpinner:
                newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;

            case R.id.timeSpinner:
                newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
                break;

            case R.id.go_button:

                requestParameters.add(0, "GP:" + userLatLng.latitude + "," + userLatLng.longitude);

                String destination = destInput.getText().toString();
                String destinationID = locationsToID.get(destination);

                if(destinationID == null)
                {
                    Toast.makeText(this,
                            "Please enter a valid destination.", Toast.LENGTH_SHORT).show();
                    return;
                }

                requestParameters.add(1, destinationID);
                requestParameters.add(2, date + " " + time);

                JourneyLoader.removeAllLines();
                StopsLoader.removeAllStopMarkers();

                JourneyLoader loader = new JourneyLoader(
                            requestParameters.get(0),
                            requestParameters.get(1),
                            requestParameters.get(2),
                            mMap);

                showProgressBar(true);

                loader.loadJourney();

                break;
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //Just dismissing keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText())
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void networkRequestCompleted(String result)
    {
        Log.d("NETWORK REQ COMPLETED", result);
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            selectedDate.clear();
            selectedDate.set(year, month, day);

            // Do something with the date chosen by the user
            date = (month+1) + "/" + day + "/" + year;
            dateDisplay = day + " / " + (month+1) + " / " + year;
            dateButton.setText(dateDisplay);
        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            String temp;
            if(minute < 10)
                temp = "0" + minute;
            else
                temp = "" + minute;

            time = hourOfDay + ":" + temp;
            timeButton.setText(time);
        }
    }
}
