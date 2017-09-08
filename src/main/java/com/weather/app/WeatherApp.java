package com.weather.app;

import static spark.Spark.get;

import java.util.HashMap;
public class WeatherApp {
    private static WeatherService weatherService = new WeatherService();
    public static void main(String[] args) {

        get("/test", (request, response) -> "test");
        get("/getData/:city", (request, response) ->
            weatherService.getWeatherData(request.params(":city"))
        );
    }

}
