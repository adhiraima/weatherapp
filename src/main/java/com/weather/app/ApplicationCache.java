package com.weather.app;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class ApplicationCache {

    private transient final HashMap<String, JsonObject> appCache;

    private static ApplicationCache cache = null;

    private ApplicationCache() {
        appCache = new HashMap<String, JsonObject>();
    }

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
}
