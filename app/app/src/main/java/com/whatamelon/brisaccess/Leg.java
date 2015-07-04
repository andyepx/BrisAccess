package com.whatamelon.brisaccess;

/**
 * Created by abvincita on 4/07/2015.
 */
public class Leg
{
    String polyline;
    String fromStopId;
    String toStopId;

    public Leg (String poly, String from, String to)
    {
        polyline = poly;
        fromStopId = from;
        toStopId = to;
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
}
