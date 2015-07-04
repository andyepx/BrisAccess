package com.whatamelon.brisaccess;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
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
public class StopsLoader implements JSONRequest.NetworkListener
{
    private ArrayList<Leg> legs;
    private ArrayList<Stop> nonTrainStops;
    private ArrayList<Stop> trainStops;
    private static ArrayList<Marker> stopMarkers = new ArrayList<>();

    private JSONRequest request;
    private GoogleMap map;

    private int[] markerIcons = { R.drawable.bus_geo_border,
            R.drawable.train_geo_border,
            R.drawable.ferry_geo_border};

    public StopsLoader (ArrayList<Leg> legs, GoogleMap map)
    {
        this.legs = legs;
        this.map = map;

        nonTrainStops = new ArrayList<>();
        trainStops = new ArrayList<>();
    }

    public void loadStops ()
    {
        String stopIds = legs.get(0).toStopId;

        for(int i = 1; i < legs.size(); i++)
        {
            stopIds = stopIds.concat("," + legs.get(i).toStopId);
        }

        String urlString = "http://dev-cloud.teardesign.com:3030/data/translink/stops?stops="
                            + Uri.encode(stopIds);

        Log.d("StopsLoader request: ", urlString);
        request = new JSONRequest();
        request.setListener(this);
        request.execute(urlString);
    }

    @Override
    public void networkRequestCompleted(String result)
    {
        Log.d("NETWORK REQ COMPLETED", result);

        JSONObject obj = (JSONObject) JSONValue.parse(result);
        JSONArray stopsArray = (JSONArray) obj.get("Stops");

        for (int i = 0; i < stopsArray.size(); i++)
        {
            JSONObject stop = (JSONObject) stopsArray.get(i);

            String stopID = ((String) stop.get("StopId")).replaceFirst("^0+(?!$)", "");
            String name = (String) stop.get("Description");
            int type = ((Long) stop.get("ServiceType")).intValue();

            JSONObject latLng = (JSONObject) stop.get("Position");
            LatLng position = new LatLng((Double) latLng.get("Lat"), (Double) latLng.get("Lng"));

            if(type == 2)
                trainStops.add(new Stop(stopID, name, type, position));
            else
                nonTrainStops.add(new Stop(stopID, name, type, position));
        }

        addNonTrainStopsMarkerToMap();

        AccessibilityLoader accLoader = new AccessibilityLoader(trainStops, stopMarkers, map);
        accLoader.loadStopsAccessibility();
    }

    private void addNonTrainStopsMarkerToMap()
    {
        for(Stop stop : nonTrainStops)
        {
            int serviceType = stop.getServiceType();

            Marker m = map.addMarker(new MarkerOptions()
                    .position(stop.getPosition())
                    .title(stop.getDescription())
                    .icon(BitmapDescriptorFactory.fromResource(markerIcons[serviceType - 1])));

            stopMarkers.add(m);
        }
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
}
