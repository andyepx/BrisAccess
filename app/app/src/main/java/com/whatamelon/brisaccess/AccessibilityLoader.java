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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abvincita on 4/07/2015.
 */
public class AccessibilityLoader implements JSONRequest.NetworkListener
{
    private ArrayList<Stop> trainStops;
    private HashMap<String, Route> stopRouteHashMap;
    private JSONRequest request;
    private GoogleMap map;
    private static ArrayList<Marker> stopMarkers;

    public AccessibilityLoader (GoogleMap map)
    {
        this.map = map;

        trainStops = new ArrayList<>();
        stopMarkers = new ArrayList<>();
        stopRouteHashMap = new HashMap<>();
    }

    public AccessibilityLoader (ArrayList<Stop> stops, ArrayList<Marker> markers, GoogleMap map)
    {
        this.trainStops = stops;
        this.stopMarkers = markers;
        this.map = map;

        stopRouteHashMap = new HashMap<>();
    }

    public void loadAllTrainStationsAccessibility()
    {
        String urlString = "http://dev-cloud.teardesign.com:3030/data/qr";

        Log.d("Accessibility request: ", urlString);
        request = new JSONRequest();
        request.setListener(this);
        request.execute(urlString);
    }

    public void loadStopsAccessibility()
    {
        for(Stop stop : trainStops)
        {
            stopRouteHashMap.put(stop.getParentId(), stop.getRoute());

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
        JSONArray resultsArray = (JSONArray) JSONValue.parse(result);

        for (int i = 0; i < resultsArray.size(); i++)
        {
            JSONObject obj = (JSONObject) resultsArray.get(i);

            String stopID = (String) obj.get("stationid");
            String stationName = obj.get("Station") + " Station";
            int accessibility = ((Long) obj.get("Station_access")).intValue();
            boolean helpPhoneExists = Integer.parseInt((String) obj.get("Help_phones")) == 1;

            JSONObject latLng = (JSONObject) obj.get("position");
            LatLng position = new LatLng((Double) latLng.get("Lat"), (Double) latLng.get("Lng"));
            Route route = stopRouteHashMap.isEmpty() ? null : stopRouteHashMap.get(stopID);

            Stop completeStop;

            switch (accessibility) {
                case 3: //Lift
                    completeStop = new Stop(stopID, stationName, 2, position, Stop.Accessibility.Independent, helpPhoneExists, route);
                    addTrainMarker(completeStop);
                    break;
                case 1: //Assist
                    completeStop = new Stop(stopID, stationName, 2, position, Stop.Accessibility.Assist, helpPhoneExists, route);
                    addAssistMarker(completeStop);
                    break;
                default: //Stairs (or worse)
                    completeStop = new Stop(stopID, stationName, 2, position, Stop.Accessibility.Stairs, helpPhoneExists, route);
                    addStairsMarker(completeStop);
                    break;
            }
        }

        MainActivity.showProgressBar(false);
    }

    private void addAssistMarker(Stop stop)
    {
        String trainCodeAndTime = trainCodeAndTimeGenerator(stop.getRoute());

        Marker m = map.addMarker(new MarkerOptions()
                .position(stop.getPosition())
                .title(stop.getDescription())
                .snippet(trainCodeAndTime + "ASSIST REQUIRED")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.assist_marker)));

        stopMarkers.add(m);
    }

    private void addStairsMarker(Stop stop)
    {
        String trainCodeAndTime = trainCodeAndTimeGenerator(stop.getRoute());

        Marker m = map.addMarker(new MarkerOptions()
                .position(stop.getPosition())
                .title(stop.getDescription())
                .snippet(trainCodeAndTime + "WARNING: STAIRS")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stairs_marker)));

        stopMarkers.add(m);
    }

    private void addTrainMarker(Stop stop)
    {
        String trainCodeAndTime = trainCodeAndTimeGenerator(stop.getRoute());

        Marker m = map.addMarker(new MarkerOptions()
                .position(stop.getPosition())
                .title(stop.getDescription())
                .snippet(trainCodeAndTime + "Lift available")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.lift_marker)));

        stopMarkers.add(m);
    }

    public static void removeAllStopMarkers()
    {
        for(Marker m : stopMarkers)
        {
            m.setVisible(false);
            m.remove();
        }

        stopMarkers.clear();
    }

    private String trainCodeAndTimeGenerator(Route route)
    {
        String trainCodeAndTime = "";
        if(route != null)
        {
            String trainName = route.getRouteCode();
            String[] getTo = trainName.split(" to ");
            String destination = getTo[1];
            if(destination.contains(" - "))
            {
                destination = destination.split(" - ")[0];
            }

            trainCodeAndTime = destination + " line at " +
                               (new SimpleDateFormat("HH:mm").format(route.getDepartureTime()) + " - ");
        }

        return trainCodeAndTime;
    }
}
