package com.whatamelon.brisaccess;

import java.util.Date;

/**
 * Created by abvincita on 9/07/2015.
 */
public class Route
{
    String routeCode;
    Date departureTime;
    int vehicleType;

    public Route (String code, Date time, int type)
    {
        routeCode = code;
        departureTime = time;
        vehicleType = type;
    }

    public String getRouteCode () { return routeCode; }

    public Date getDepartureTime ()
    {
        return departureTime;
    }

    public int getVehicleType ()
    {
        return vehicleType;
    }
}
