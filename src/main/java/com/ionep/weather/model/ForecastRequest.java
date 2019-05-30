package com.ionep.weather.model;

import com.ionep.weather.model.City;
import com.ionep.weather.model.ForecastRequest;

public class ForecastRequest {

    private String cityName;
    private String requestId;

    public String getCityName() {
        return cityName;
    }

    public ForecastRequest setCityName(City city) {
        this.cityName = city.getName();
        return this;
    }
    
    public ForecastRequest setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public ForecastRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}
