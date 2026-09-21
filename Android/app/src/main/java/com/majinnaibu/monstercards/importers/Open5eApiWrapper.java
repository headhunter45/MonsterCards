package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majinnaibu.monstercards.models.Monster;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.majinnaibu.monstercards.utils.Logger;

public class Open5eApiWrapper {
    private static final String BASE_URL = "https://api.open5e.com/v2/creatures/";
    
    public static class Open5ePageResult {
        public List<Monster> monsters = new ArrayList<>();
        @Nullable public String nextUrl;
    }

    @NonNull
    public static Open5ePageResult fetchPage(@Nullable String urlStr) throws Exception {
        if (urlStr == null || urlStr.isEmpty()) {
            urlStr = BASE_URL;
        }

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android; Mobile)");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(15000);

        int code = conn.getResponseCode();
        if (code != 200) {
            throw new IllegalStateException("Open5e API returned HTTP " + code);
        }

        StringBuilder sb = new StringBuilder();
        try (InputStream in = conn.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        
        String jsonPayload = sb.toString();
        JsonObject root = JsonParser.parseString(jsonPayload).getAsJsonObject();
        
        Open5ePageResult result = new Open5ePageResult();
        if (root.has("next") && !root.get("next").isJsonNull()) {
            result.nextUrl = root.get("next").getAsString();
        }
        
        if (root.has("results") && root.get("results").isJsonArray()) {
            JsonArray results = root.getAsJsonArray("results");
            Open5eImporter importer = new Open5eImporter();
            for (int i = 0; i < results.size(); i++) {
                JsonElement el = results.get(i);
                if (el.isJsonObject()) {
                    try {
                        Monster monster = importer.parse(el.toString());
                        result.monsters.add(monster);
                    } catch (Exception e) {
                        Logger.logError("Failed to parse Open5e monster", e);
                    }
                }
            }
        }
        
        return result;
    }
}
