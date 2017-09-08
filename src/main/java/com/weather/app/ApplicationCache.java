package com.weather.app;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class ApplicationCache {

    private transient final HashMap<String, JsonObject> appCache = new HashMap<String, JsonObject>();;

    private static ApplicationCache cache = null;

    private ApplicationCache() { }

    public static ApplicationCache getInstance() {
        if (cache == null) {
            return new ApplicationCache();
        }
        return cache;
    }

    public void update(String key, JsonObject value) {
        appCache.put(key, value);
    }

    public JsonObject get(String key) {
        return appCache.get(key);
    }

    public boolean check(String key) {
        return appCache.containsKey(key);
    }
}
