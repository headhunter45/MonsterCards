package com.majinnaibu.monstercards.importers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Open5eApiWrapper {
    private static final String BASE_URL = "https://api.open5e.com/v2/creatures/";
    
    public static class Open5ePageResult {
        public List<Monster> monsters = new ArrayList<>();
        @Nullable public String nextUrl;
        public int totalCount = 0;
    }

    @NonNull
    public static Open5ePageResult fetchPage(@Nullable String urlStr, BooleanSupplier isCancelled) throws Exception {
        if (urlStr == null || urlStr.isEmpty()) {
            urlStr = BASE_URL;
        }

        int maxRetries = 5;
        long backoffMs = 1500;
        int attempts = 0;

        while (true) {
            if (isCancelled.getAsBoolean()) {
                throw new InterruptedException("Open5e network read was cancelled.");
            }

            attempts++;
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android; Mobile)");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(20000);

                int code = conn.getResponseCode();
                if (code == 200) {
                    StringBuilder sb = new StringBuilder();
                    try (InputStream in = conn.getInputStream();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (isCancelled.getAsBoolean()) {
                                throw new InterruptedException("Open5e network read was cancelled.");
                            }
                            sb.append(line);
                        }
                    } catch (InterruptedIOException e) {
                        throw new InterruptedException("Open5e network read was cancelled.");
                    }

                    String jsonPayload = sb.toString();
                    JsonObject root = JsonParser.parseString(jsonPayload).getAsJsonObject();

                    Open5ePageResult result = new Open5ePageResult();
                    if (root.has("count") && !root.get("count").isJsonNull()) {
                        result.totalCount = root.get("count").getAsInt();
                    }
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

                } else if ((code >= 500 || code == 429) && attempts <= maxRetries) {
                    Logger.logError("Open5e API returned HTTP " + code + " (attempt " + attempts + "/" + maxRetries + "). Retrying in " + backoffMs + "ms...", null);
                    if (sleepWithCancelCheck(backoffMs, isCancelled)) {
                        throw new InterruptedException("Open5e network read was cancelled.");
                    }
                    backoffMs *= 2;
                } else {
                    throw new IllegalStateException("Open5e API returned HTTP " + code);
                }
            } catch (IOException e) {
                if (isCancelled.getAsBoolean()) {
                    throw new InterruptedException("Open5e network read was cancelled.");
                }
                if (attempts <= maxRetries) {
                    Logger.logError("Open5e network error: " + e.getMessage() + " (attempt " + attempts + "/" + maxRetries + "). Retrying in " + backoffMs + "ms...", e);
                    if (sleepWithCancelCheck(backoffMs, isCancelled)) {
                        throw new InterruptedException("Open5e network read was cancelled.");
                    }
                    backoffMs *= 2;
                } else {
                    throw e;
                }
            }
        }
    }

    private static boolean sleepWithCancelCheck(long ms, BooleanSupplier isCancelled) {
        long slept = 0;
        long step = 200;
        while (slept < ms) {
            if (isCancelled.getAsBoolean()) {
                return true;
            }
            try {
                Thread.sleep(Math.min(step, ms - slept));
            } catch (InterruptedException e) {
                return true;
            }
            slept += step;
        }
        return isCancelled.getAsBoolean();
    }
}
