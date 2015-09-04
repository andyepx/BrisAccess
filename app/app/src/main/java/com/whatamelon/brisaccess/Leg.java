package com.whatamelon.brisaccess;

/**
 * Created by abvincita on 4/07/2015.
 */
public class Leg
{
    String polyline;
    String fromStopId;
    String toStopId;

    Route route;

    public Leg (String poly, String from, String to)
    {
        polyline = poly;
        fromStopId = from;
        toStopId = to;
    }

    public Leg (String poly, String from, String to, Route route)
    {
        polyline = poly;
        fromStopId = from;
        toStopId = to;

        this.route = route;
    }

    public String getPolyline ()
    {
        return polyline;
    }

    public String getFromStopId ()
    {
        return fromStopId;
    }

    public String getToStopId ()
    {
        return toStopId;
    }

    public void setRoute (Route legRoute)
    {
        route = legRoute;
    }

    public Route getRoute ()
    {
        return route;
    }
}
