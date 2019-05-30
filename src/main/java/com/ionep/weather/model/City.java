package com.ionep.weather.model;

public class City {

	private long id;
    
	private String name;
	
    private String postalCode;
    private int countryCode;
    private double latitude;
    private double longitude;
    
    public City() {
    	this(0, "novalue", "novalue", 1, 1.1, 1.1);
    }

    public City(long id, String name, String postalCode, int countryCode, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return this.id;
    }
    
    public void setId(long i) {
        this.id = i;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String n) {
        this.name = n;
    }
    
    public int getCountryCode() {
        return this.countryCode;
    }
    
    public void setCountryCode(int cc) {
        this.countryCode = cc;
    }
    
    public String getPostalCode() {
        return this.postalCode;
    }
    
    public void setPostalCode(String pc) {
        this.postalCode = pc;
    }
    
    public double getLatitude() {
        return this.latitude;
    }
    
    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public double getLongitude() {
        return this.longitude;
    }
    
    public void setLongitude(double lon) {
        this.longitude = lon;
    }
}
