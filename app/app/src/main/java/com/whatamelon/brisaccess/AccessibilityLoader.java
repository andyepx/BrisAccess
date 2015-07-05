package com.whatamelon.brisaccess;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;

/**
 * Created by abvincita on 4/07/2015.
 */
public class AccessibilityLoader implements JSONRequest.NetworkListener
{
    private ArrayList<Stop> trainStops;
    private ArrayList<Marker> stopMarkers;
    private JSONRequest request;
    private GoogleMap map;

    private ArrayList<Stop> completeTrainStops;

    private int[] markerIcons = { R.drawable.train_geo_border, R.drawable.warning_mark };

    public AccessibilityLoader (ArrayList<Stop> stops, ArrayList<Marker> markers, GoogleMap map)
    {
        this.trainStops = stops;
        this.stopMarkers = markers;
        this.map = map;

        completeTrainStops = new ArrayList<>();
    }

    public void loadStopsAccessibility()
    {
        for(Stop stop : trainStops)
        {
            String urlString = "http://dev-cloud.teardesign.com:3030/data/qr?sid="
                    + Uri.encode(stop.getId());

            Log.d("Accessibility request: ", urlString);
            request = new JSONRequest();
            request.setListener(this);
            request.execute(urlString);
        }
    }

    @Override
    public void networkRequestCompleted(String result)
    {
        JSONArray raw = (JSONArray) JSONValue.parse(result);
        JSONObject obj = (JSONObject) raw.get(0);

        String stopID = (String) obj.get("stationid");
        String stationName = obj.get("Station") + " Station";
        int accessibility = ((Long) obj.get("Station_access")).intValue();
        boolean helpPhoneExists = Integer.parseInt((String) obj.get("Help_phones")) == 1;

        JSONObject latLng = (JSONObject) obj.get("position");
        LatLng position = new LatLng((Double) latLng.get("Lat"), (Double) latLng.get("Lng"));

        Stop completeStop;

        switch (accessibility)
        {
            case 3: //Lift
                completeStop = new Stop(stopID, stationName, 2, position, Stop.Accessibility.Independent, helpPhoneExists);
                addTrainMarker(completeStop);
                break;
            case 1: //Assist
                completeStop = new Stop(stopID, stationName, 2, position, Stop.Accessibility.Assist, helpPhoneExists);
                addAssistMarker(completeStop);
                break;
            default: //Stairs (or worse)
                completeStop = new Stop(stopID, stationName, 2, position, Stop.Accessibility.Stairs, helpPhoneExists);
                addStairsMarker(completeStop);
                break;
        }

        completeTrainStops.add(completeStop);

        MainActivity.showProgressBar(false);
    }

    private void addAssistMarker(Stop stop)
    {
        Marker m = map.addMarker(new MarkerOptions()
                .position(stop.getPosition())
                .title(stop.getDescription())
                .snippet("ASSIST REQUIRED")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning_mark)));

        stopMarkers.add(m);
    }

    private void addStairsMarker(Stop stop)
    {
        Marker m = map.addMarker(new MarkerOptions()
                .position(stop.getPosition())
                .title(stop.getDescription())
                .snippet("WARNING: STAIRS")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning_mark)));

        stopMarkers.add(m);
    }

    private void addTrainMarker(Stop stop)
    {
        Marker m = map.addMarker(new MarkerOptions()
                .position(stop.getPosition())
                .title(stop.getDescription())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.train_geo_border)));

        stopMarkers.add(m);
    }
}