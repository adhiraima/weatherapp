package com.weather.app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;

public class WeatherService {
    private ApplicationCache cache;
    private JsonParser parser;

    public WeatherService() {
        cache = ApplicationCache.getInstance();
        parser = new JsonParser();
    }

    public HashMap<String, String> getWeatherData(String city) {
        HashMap<String, String> response = new HashMap<String, String>();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        if (cache.check(city)) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(cache.get(city).get("dt").getAsLong() * 1000);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            if (today.get(Calendar.DATE) == cal.get(Calendar.DATE)
                && today.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                    && today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
                response.put("status", "200");
                response.put("data", cache.get(city).toString());
                return response;
            }
        }

        try {
            response = doGet(city, new URL(ApplicationConstants.WEATHER_ENDPOINT
                    + "?q=" + city + "&APPID="
                    + ApplicationConstants.API_KEY));
        } catch (MalformedURLException e) {
            response.put("status", "500");
        } finally {
            return response;
        }
    }

    private HashMap<String, String> doGet(String city, URL url) {
        HashMap<String, String> response = new HashMap<String, String>();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = connection.getResponseCode();
            response.put("status", Integer.toString(responseCode));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                res.append(inputLine);
            }
            in.close();
            JsonObject json = (JsonObject)parser.parse(res.toString());
            response.put("data", res.toString());
            cache.update(city, json);
        } catch (IOException e) {
            System.out.println("exp goget");
            response.put("status", "500");
            response.put("data", "Error occourred in processing request!");
        } finally {
            return response;
        }
    }
}
