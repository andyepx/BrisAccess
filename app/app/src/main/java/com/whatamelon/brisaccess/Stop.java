package com.whatamelon.brisaccess;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

/**
 * The java class that represents a Stop object. 
 * A stop can either be a bus stop, ferry stop, or train stop.
 * 
 * @author Transponders
 * @version 1.0
 */
public class Stop
{
	public enum Accessibility
	{ Independent, Assist, Stairs }

	private String stopId;
	private int serviceType;
	private LatLng position;
	private String parentId;
	private LatLng parentPosition;
	private String description;
	private Accessibility accessibility;
	private boolean helpPhoneExists;

	Route route;
	
	public Stop (String stopId, String decription, int serviceType, LatLng position)
	{
		this.stopId = stopId;
		this.serviceType = serviceType;
		this.position = position;
		this.parentId = "";
		this.description = decription;

		accessibility = Accessibility.Stairs;
		helpPhoneExists = false;
	}

    public Stop (String stopId, String decription, int serviceType, LatLng position, Route route)
    {
        this.stopId = stopId;
        this.serviceType = serviceType;
        this.position = position;
        this.parentId = "";
        this.description = decription;
        this.route = route;

        accessibility = Accessibility.Stairs;
        helpPhoneExists = false;
    }

	public Stop (String stopId, String decription, int serviceType, LatLng position, Accessibility a, boolean isExist)
	{
		this.stopId = stopId;
		this.serviceType = serviceType;
		this.position = position;
		this.parentId = "";
		this.description = decription;

		accessibility = a;
		helpPhoneExists = isExist;
	}

    public void setRoute (Route legRoute)
    {
        route = legRoute;
    }

    public Route getRoute ()
    {
        return route;
    }

	public void setAccessibility (Accessibility a)
	{
		accessibility = a;
	}

	public Accessibility getAccessibility()
	{
		return accessibility;
	}

	public void setHelpPhoneExists (boolean isExist)
	{
		helpPhoneExists = isExist;
	}

	public boolean isHelpPhoneExists()
	{
		return helpPhoneExists;
	}

	/**
     * Getter method of the stop id.
     *
     * @return String the String representing the ID of the stop.
     */
	public String getId() {
		return stopId;
	}
	
	/**
     * Getter method of the stop location.
     *
     * @return LatLng the latitude longitude of the position of the stop.
     */
	public LatLng getPosition() {
		return position;
	}
	
	/**
     * Set the position of the parent stop if the stop is grouped together.
     *
     * @param parentId the stop ID of the parent 
     * @param parentPosition the latitude longitude coordinate of the parent stop.
     */
	public void setParentPosition(String parentId, LatLng parentPosition) {
		this.parentId = parentId;
		this.parentPosition = parentPosition;
	}
	
	/**
     * A method to check whether a stop is grouped together (has a parent).
     *
     * @return boolean true if the stop has a parent stop.
     */
	public boolean hasParent() {
		if (parentId.equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
     * Getter method of the parent stop's position.
     *
     * @return LatLng the latitude longitude of the parent stop's position.
     */
	public LatLng getParentPosition() {
		if (hasParent()) {
			return parentPosition;
		} else {
			return null;
		}
	}
	
	/**
     * Getter method of the parent's stop ID
     *
     * @return String the parent's stop ID
     */
	public String getParentId() {
		if (hasParent()) {
			return parentId;
		} else {
			return null;
		}
	}
	
	/**
     * Getter method description of this stop.
     *
     * @return String the description of this stop.
     */
	public String getDescription() {
		return description;
	}
	
	public int getServiceType()
	{
		return serviceType;
	}
}
