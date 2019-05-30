package com.ionep.weather.model;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class PreparationState {

    Forecast forecast;

    ForecastRequest request;

    State state;

    public enum State {
        IN_QUEUE,
        PREDICTING,
        READY;
    }

    private static final Jsonb JSON = JsonbBuilder.create();

    public static String ready(ForecastRequest request, Forecast forecast) {
        return JSON.toJson(new PreparationState().setForecast(forecast).setRequest(request).setState(State.READY));
    }

    public static String queued(ForecastRequest request) {
        return JSON.toJson(new PreparationState().setRequest(request).setState(State.IN_QUEUE));
    }

    public static String underPreparation(ForecastRequest request) {
        return JSON.toJson(new PreparationState().setRequest(request).setState(State.PREDICTING));
    }

    public Forecast getForecast() {
        return forecast;
    }

    public PreparationState setForecast(Forecast forecast) {
        this.forecast = forecast;
        return this;
    }

    public ForecastRequest getRequest() {
        return request;
    }

    public PreparationState setRequest(ForecastRequest request) {
        this.request = request;
        return this;
    }

    public State getState() {
        return state;
    }

    public PreparationState setState(State state) {
        this.state = state;
        return this;
    }
}
