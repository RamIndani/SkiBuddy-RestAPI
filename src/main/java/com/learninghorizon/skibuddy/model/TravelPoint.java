package com.learninghorizon.skibuddy.model;

public class TravelPoint {
	private String latitude;
	private String longitude;
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public TravelPoint(){
		
	}
	
	public TravelPoint(final String latitude, final String longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
