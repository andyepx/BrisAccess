package com.whatamelon.brisaccess;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;

/**
 * Created by abvincita on 4/07/2015.
 */
public class JourneyLoader implements JSONRequest.NetworkListener
{
    private String fromId, destId, date;
    private JSONRequest request;

    private GoogleMap map;
    private ArrayList<Leg> legsList;
    private static ArrayList<Polyline> polylines = new ArrayList<> ();

    public JourneyLoader(String fromId, String destId, String date, GoogleMap map)
    {
        this.fromId = fromId;
        this.destId = destId;
        this.date = date;
        this.map = map;

        legsList = new ArrayList<> ();
    }

    public void loadJourney()
    {
        //showProgressBar(true);

        String urlString = "http://dev-cloud.teardesign.com:3030/data/translink/travel?from="
                + Uri.encode(fromId) + "&to=" + Uri.encode(destId) + "&at=" + Uri.encode(date);

        Log.d("JourneyLoader request: ", urlString);
        request = new JSONRequest();
        request.setListener(this);
        request.execute(urlString);
    }

    @Override
    public void networkRequestCompleted(String result)
    {
        Log.d("NETWORK REQ COMPLETED", result);

        JSONObject obj = (JSONObject) JSONValue.parse(result);
        JSONObject travelOption = (JSONObject) obj.get("TravelOptions");
        JSONArray array = (JSONArray) travelOption.get("Itineraries");
        JSONObject itinerary = (JSONObject) array.get(0);
        JSONArray legs = (JSONArray) itinerary.get("Legs");

        for (int i = 0; i < legs.size(); i++)
        {
            JSONObject leg = (JSONObject) legs.get(i);

            String poly = (String) leg.get("Polyline");
            String fromStop = (String) leg.get("FromStopId");
            String toStop = (String) leg.get("ToStopId");

            legsList.add(new Leg(poly, fromStop, toStop));

            addLineToMap(poly);
        }

        StopsLoader stopsLoader = new StopsLoader(legsList, map);
        stopsLoader.loadStops();
    }

    public void addLineToMap(String line)
    {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(5).color(Color.RED);
        polylineOptions.addAll(PolylineDecoder.decodePoly(line));

        polylines.add(map.addPolyline(polylineOptions));
    }

    public static void removeAllLines ()
    {
        for (Polyline line : polylines)
        {
            line.setVisible(false);
        }

        polylines.clear();
    }
}
