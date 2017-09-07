package com.weather.app;

import static spark.Spark.get;

import java.util.HashMap;
public class WeatherApp {
    public static void main(String[] args) {

        get("/test", (request, response) -> "test");
        get("/getData/:city", (request, response) -> {
            HashMap<String, String> resp = new WeatherService().getWeatherData(request.params(":city"));
            response.status(Integer.parseInt(resp.get("status")));
            response.body(resp.get("data"));
            return response;
        });
    }

}
