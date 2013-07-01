package com.addvalsolutions.mymap;

import com.google.android.gms.maps.model.LatLng;


public class Route {
	
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	private LatLng strtpoint;
	public LatLng getStrtpoint() {
		return strtpoint;
	}
	public void setStrtpoint(LatLng strtpoint) {
		this.strtpoint = strtpoint;
	}
	public LatLng getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(LatLng endpoint) {
		this.endpoint = endpoint;
	}
	private LatLng endpoint;
	private String distance;
	private String duration;
	private String instruction;
	
	
	
}
	
	