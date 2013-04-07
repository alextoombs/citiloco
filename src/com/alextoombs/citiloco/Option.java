package com.alextoombs.citiloco;

import java.util.ArrayList;

/**
 * Part of the Citiloco app.
 * @author Shane MacQuillan
 * @date 4/6/2013
 * @version 1.0
 *
 */
public class Option {
	
	private ArrayList<Location> locations;
	private double startTime;
	private double endTime;
	
	public Option(ArrayList<Location> locations, double startTime, double endTime) {
		this.locations = locations;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public ArrayList<Location> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

}
