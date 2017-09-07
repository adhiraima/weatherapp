package com.weather.app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
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
        //System.out.println("1");
        HashMap<String, String> response = new HashMap<String, String>();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        if (null != cache.get(city)) {
            //System.out.println("2");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(cache.get(city).get("dt").getAsLong());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            if (Instant.ofEpochMilli(cal.getTimeInMillis()).equals(today.getTimeInMillis())) {
                response.put("status", "200");
                response.put("data", cache.get(city).getAsString());
                return response;
            }
        }
        //System.out.println("3");
        try {
            return doGet(city, new URL(ApplicationConstants.WEATHER_ENDPOINT
                    + "?q=" + city + "&APPID="
                    + ApplicationConstants.API_KEY));
        } catch (MalformedURLException e) {
            //System.out.println("4");
            response.put("status", "500");
        } finally {
            //System.out.println("5");
            return response;
        }
    }

    private HashMap<String, String> doGet(String city, URL url) {
        //System.out.println("6 doGet");
        HashMap<String, String> response = new HashMap<String, String>();
        try {
            //System.out.println("the url:.  " + url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            System.out.println("coming here!");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            //int responseCode = connection.getResponseCode();
            //System.out.println("Status " + responseCode);
            response.put("status", "200");
            //System.out.println("Status ");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            //System.out.println("Status!! ");
            String inputLine;
            StringBuffer res = new StringBuffer();
            //System.out.println("coming here too!!");
            while ((inputLine = in.readLine()) != null) {
                res.append(inputLine);
            }
            in.close();
            //System.out.println("inter");
            JsonObject json = (JsonObject)parser.parse(res.toString());
            response.put("data", res.toString());
            cache.update(city, json);
        } catch (IOException e) {
            //System.out.println("exp goget");
            response.put("status", "500");
        } finally {
            return response;
        }
    }
}
