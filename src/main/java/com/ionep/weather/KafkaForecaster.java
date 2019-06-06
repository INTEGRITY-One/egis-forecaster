package com.ionep.weather;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.ionep.weather.model.Forecast;
import com.ionep.weather.model.ForecastRequest;
import com.ionep.weather.model.PreparationState;

import io.smallrye.reactive.messaging.annotations.Emitter;
import io.smallrye.reactive.messaging.annotations.Stream;

@ApplicationScoped
public class KafkaForecaster {

    @ConfigProperty(name = "forecaster.name")
    String baseName;
    
    String name = null;
    
    // Generate pseudo-random name
    private void assignName() {
    	double rd = Math.random();
        String s = String.valueOf(rd);
        
    	this.name = baseName + s.substring(2, 4);
    }

    private Jsonb jsonb = JsonbBuilder.create();
    private Random random = new Random();
    
    @Inject
    @Stream("queue2")
    Emitter<String> forecasts;

    @Incoming("requests")
    @Outgoing("queue")
    public CompletionStage<String> prepare(String message) {
        // Only do this once!
    	if (this.name == null)
        	assignName();
    	
    	ForecastRequest request = jsonb.fromJson(message, ForecastRequest.class);
        System.out.println("Forecaster " + name + " is going to make a prediction for " + request.getCityName());
        
        // reflect the fact that the request has been picked up & is being processed...
        forecasts.send(PreparationState.underPreparation(request));
        return predict(request)
                .thenApply(forecast -> PreparationState.ready(request, forecast));
    }

    private CompletionStage<Forecast> predict(ForecastRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Preparing " + name + "'s forecast for " + request.getCityName());
            nap();
            return new Forecast(request, name);
        });
    }

    private void nap() {
        try {
            Thread.sleep(random.nextInt(5000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
