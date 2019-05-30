package com.ionep.weather.model;

import com.ionep.weather.model.City;
import com.ionep.weather.model.Forecast;

public class Forecast {

	private String requestId;
	private String cityName;
    private String forecast;
    private String forecaster;

    public Forecast() {

    }

    public Forecast(ForecastRequest request, String forecaster) {
        this.requestId = request.getRequestId();
    	this.cityName = request.getCityName();
        this.forecaster = forecaster;
    }
    
    public String getRequestId() {
        return requestId;
    }

    public Forecast setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public Forecast setCityName(City city) {
        this.cityName = city.getName();
        return this;
    }
    
    public Forecast setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getForecast() {
        return forecast;
    }

    public Forecast setForecast(String forecast) {
        this.forecast = forecast;
        return this;
    }
    
    public String getForecaster() {
        return forecaster;
    }

    public Forecast setForecaster(String forecaster) {
        this.forecast = forecaster;
        return this;
    }
}
