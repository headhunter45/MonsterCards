package com.majinnaibu.monstercards.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.majinnaibu.monstercards.AppDatabase;
import com.majinnaibu.monstercards.MonsterCardsApplication;
import com.majinnaibu.monstercards.importers.EntityImporter;
import com.majinnaibu.monstercards.importers.Open5eApiWrapper;
import com.majinnaibu.monstercards.models.ImportSource;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.ReferenceMonster;
import com.majinnaibu.monstercards.utils.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CompendiumSourceManager {

    private static final String PREFS_NAME = "compendium_sources_prefs";
    private static final String KEY_DOWNLOADED_PREFIX = "downloaded_";
    private static final String KEY_COUNT_PREFIX = "count_";
    private static final String KEY_TIMESTAMP_PREFIX = "timestamp_";
    private static final String KEY_SHA_PREFIX = "sha_";
    private static final String KEY_ETAG_PREFIX = "etag_";

    public interface ProgressListener {
        void onProgress(int current, int total, String statusMessage);
    }

    public static boolean isSourceDownloaded(@NonNull Context context, @NonNull String sourceId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DOWNLOADED_PREFIX + sourceId, false);
    }

    public static int getSourceMonsterCount(@NonNull Context context, @NonNull String sourceId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_COUNT_PREFIX + sourceId, 0);
    }

    public static String getSourceSha(@NonNull Context context, @NonNull String sourceId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_SHA_PREFIX + sourceId, "");
    }

    public static void setSourceSha(@NonNull Context context, @NonNull String sourceId, String sha) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_SHA_PREFIX + sourceId, sha != null ? sha : "").apply();
    }

    public static String getSourceEtag(@NonNull Context context, @NonNull String sourceId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_ETAG_PREFIX + sourceId, "");
    }

    public static void setSourceEtag(@NonNull Context context, @NonNull String sourceId, String etag) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_ETAG_PREFIX + sourceId, etag != null ? etag : "").apply();
    }

    public static void markSourceDownloaded(@NonNull Context context, @NonNull String sourceId, int count) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean(KEY_DOWNLOADED_PREFIX + sourceId, true)
                .putInt(KEY_COUNT_PREFIX + sourceId, count)
                .putLong(KEY_TIMESTAMP_PREFIX + sourceId, System.currentTimeMillis())
                .apply();
    }

    public static void clearSource(@NonNull Context context, @NonNull AppDatabase db, @NonNull String sourceId) {
        db.referenceMonsterDAO().deleteBySourceIdSync(sourceId);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .remove(KEY_DOWNLOADED_PREFIX + sourceId)
                .remove(KEY_COUNT_PREFIX + sourceId)
                .remove(KEY_TIMESTAMP_PREFIX + sourceId)
                .remove(KEY_SHA_PREFIX + sourceId)
                .remove(KEY_ETAG_PREFIX + sourceId)
                .apply();
    }

    public static class UpdateCheckResult {
        public final boolean hasUpdate;
        public final String currentShaOrEtag;
        public final String remoteShaOrEtag;
        public final String message;

        public UpdateCheckResult(boolean hasUpdate, String currentShaOrEtag, String remoteShaOrEtag, String message) {
            this.hasUpdate = hasUpdate;
            this.currentShaOrEtag = currentShaOrEtag;
            this.remoteShaOrEtag = remoteShaOrEtag;
            this.message = message;
        }
    }

    public static UpdateCheckResult checkForUpdate(@NonNull Context context, @NonNull ImportSource source) {
        boolean isDownloaded = isSourceDownloaded(context, source.id);
        if (!isDownloaded) {
            return new UpdateCheckResult(false, "", "", "Source is not yet downloaded.");
        }

        String currentSha = getSourceSha(context, source.id);
        String currentEtag = getSourceEtag(context, source.id);

        if (source.importType == ImportSource.ImportType.OPEN5E_API) {
            try {
                URL url = new URL("https://api.open5e.com/v2/creatures/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("HEAD");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                String remoteEtag = conn.getHeaderField("ETag");
                String lastModified = conn.getHeaderField("Last-Modified");
                String remoteTag = remoteEtag != null ? remoteEtag : (lastModified != null ? lastModified : "");

                if (!remoteTag.isEmpty() && !currentEtag.isEmpty() && !remoteTag.equals(currentEtag)) {
                    return new UpdateCheckResult(true, currentEtag, remoteTag, "New Open5e creatures available.");
                }
                return new UpdateCheckResult(false, currentEtag, remoteTag, "Open5e compendium is up to date.");
            } catch (Exception e) {
                Logger.logError("Error checking Open5e update", e);
                return new UpdateCheckResult(false, currentEtag, "", "Could not reach Open5e API.");
            }
        } else {
            // Check Git commit or ETag for Git Archive
            String remoteSha = fetchRemoteGitCommitSha(source);
            if (remoteSha != null && !remoteSha.isEmpty()) {
                if (!currentSha.isEmpty() && !currentSha.equals(remoteSha)) {
                    return new UpdateCheckResult(true, currentSha, remoteSha, "Upstream repository updated (commit " + remoteSha.substring(0, Math.min(7, remoteSha.length())) + ").");
                }
                return new UpdateCheckResult(false, currentSha, remoteSha, "Compendium is up to date with latest commit.");
            }

            // Fallback to HTTP ETag
            try {
                if (source.downloadUrl != null) {
                    URL url = new URL(source.downloadUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("HEAD");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    String remoteEtag = conn.getHeaderField("ETag");
                    if (remoteEtag != null && !remoteEtag.isEmpty()) {
                        if (!currentEtag.isEmpty() && !currentEtag.equals(remoteEtag)) {
                            return new UpdateCheckResult(true, currentEtag, remoteEtag, "Upstream archive updated.");
                        }
                    }
                }
            } catch (Exception e) {
                Logger.logError("Error checking Git archive ETag", e);
            }

            return new UpdateCheckResult(false, currentSha, "", "Compendium is up to date.");
        }
    }

    private static String fetchRemoteGitCommitSha(ImportSource source) {
        if (source.downloadUrl == null) return null;
        try {
            // E.g., https://github.com/foundryvtt/pf2e/archive/refs/heads/v14-dev.zip
            // repo: foundryvtt/pf2e, branch: v14-dev
            String url = source.downloadUrl;
            if (url.contains("github.com/")) {
                String clean = url.replace("https://github.com/", "");
                String[] parts = clean.split("/");
                if (parts.length >= 2) {
                    String owner = parts[0];
                    String repo = parts[1];
                    String branch = "master";
                    if (url.contains("/heads/")) {
                        branch = url.substring(url.indexOf("/heads/") + 7).replace(".zip", "");
                    }
                    String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo + "/commits/" + branch;
                    URL api = new URL(apiUrl);
                    HttpURLConnection conn = (HttpURLConnection) api.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "MonsterCards-Android");
                    conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);

                    if (conn.getResponseCode() == 200) {
                        String body = readStream(conn.getInputStream());
                        com.google.gson.JsonObject obj = com.google.gson.JsonParser.parseString(body).getAsJsonObject();
                        if (obj.has("sha")) {
                            return obj.get("sha").getAsString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logError("Failed to fetch remote Git commit SHA", e);
        }
        return null;
    }

    private static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    public static int downloadAndIngest(@NonNull Context context, @NonNull AppDatabase db, @NonNull ImportSource source,
                                        @NonNull BooleanSupplier isCancelled, ProgressListener progressListener) throws Exception {
        int result;
        String latestSha = fetchRemoteGitCommitSha(source);
        if (source.importType == ImportSource.ImportType.OPEN5E_API) {
            result = ingestOpen5eApi(context, db, source, isCancelled, progressListener);
        } else {
            result = ingestGitArchive(context, db, source, isCancelled, progressListener);
        }

        if (latestSha != null) {
            setSourceSha(context, source.id, latestSha);
        }
        return result;
    }

    private static int ingestOpen5eApi(Context context, AppDatabase db, ImportSource source,
                                      BooleanSupplier isCancelled, ProgressListener progressListener) throws Exception {
        if (progressListener != null) progressListener.onProgress(0, 0, "Querying Open5e API…");
        
        // Start fresh for this source
        db.referenceMonsterDAO().deleteBySourceIdSync(source.id);

        int totalIngested = 0;
        int totalCount = 0;
        String nextUrl = null;

        do {
            if (isCancelled.getAsBoolean()) throw new InterruptedException("Compendium download was cancelled");
            
            Open5eApiWrapper.Open5ePageResult page = Open5eApiWrapper.fetchPage(nextUrl, isCancelled);
            if (page.totalCount > 0) {
                totalCount = page.totalCount;
            }
            
            List<ReferenceMonster> pageMonsters = new ArrayList<>();
            for (Monster monster : page.monsters) {
                if (isCancelled.getAsBoolean()) throw new InterruptedException("Compendium download was cancelled");
                ReferenceMonster rm = ReferenceMonster.fromMonster(monster, source.id, source.bookSource);
                rm.gameSystem = source.gameSystem;
                if (!source.sourceLabel.isEmpty()) {
                    rm.sourceLabel = source.sourceLabel;
                }
                pageMonsters.add(rm);
            }

            if (!pageMonsters.isEmpty()) {
                db.referenceMonsterDAO().insertAllSync(pageMonsters);
                totalIngested += pageMonsters.size();
                markSourceDownloaded(context, source.id, totalIngested);
            }

            if (progressListener != null) {
                String status = totalCount > 0 
                        ? "Fetched " + totalIngested + " of " + totalCount + " monsters…" 
                        : "Fetched " + totalIngested + " monsters…";
                progressListener.onProgress(totalIngested, totalCount, status);
            }

            nextUrl = page.nextUrl;
        } while (nextUrl != null && !nextUrl.isEmpty());

        if (isCancelled.getAsBoolean()) throw new InterruptedException("Compendium download was cancelled");

        int finalCount = db.referenceMonsterDAO().countBySourceId(source.id);
        markSourceDownloaded(context, source.id, finalCount);
        return finalCount;
    }

    private static int ingestGitArchive(Context context, AppDatabase db, ImportSource source,
                                       BooleanSupplier isCancelled, ProgressListener progressListener) throws Exception {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }

        File tempZip = null;
        File[] existingZips = downloadsDir.listFiles((dir, name) -> name.startsWith(source.id + "_download") && name.endsWith(".zip"));
        if (existingZips != null && existingZips.length > 0) {
            tempZip = existingZips[0];
            if (progressListener != null) progressListener.onProgress(0, 0, "Using cached archive…");
        } else {
            tempZip = new File(downloadsDir, source.id + "_download_" + System.currentTimeMillis() + ".zip");
            if (progressListener != null) progressListener.onProgress(0, 0, "Downloading repository zip…");
            downloadZip(source.downloadUrl, tempZip, isCancelled, progressListener);
        }

        File extractDir = new File(context.getCacheDir(), "ref_extracted_" + source.id + "_" + System.currentTimeMillis());
        try {
            if (isCancelled.getAsBoolean()) throw new InterruptedException("Compendium extraction was cancelled");
            if (!extractDir.exists()) extractDir.mkdirs();

            if (progressListener != null) progressListener.onProgress(0, 0, "Extracting files…");
            unzipAndFilter(tempZip, extractDir, source.fileExtension, source.subfolder, isCancelled);

            if (isCancelled.getAsBoolean()) throw new InterruptedException("Compendium extraction was cancelled");

            Class<?> clazz = Class.forName(source.importerClassName);
            @SuppressWarnings("unchecked")
            EntityImporter<Monster> importer = (EntityImporter<Monster>) clazz.newInstance();

            File[] files = extractDir.listFiles();
            int totalFiles = files != null ? files.length : 0;
            if (progressListener != null) progressListener.onProgress(0, totalFiles, "Parsing " + totalFiles + " reference monsters…");

            List<ReferenceMonster> allMonsters = new ArrayList<>();
            int count = 0;
            if (files != null) {
                for (File file : files) {
                    if (isCancelled.getAsBoolean()) throw new InterruptedException("Compendium parsing was cancelled");
                    if (file.isFile()) {
                        String content = readFileContent(file);
                        if (content != null && importer.canImport(content)) {
                            try {
                                Monster monster = importer.parse(content);
                                ReferenceMonster rm = ReferenceMonster.fromMonster(monster, source.id, source.bookSource);
                                rm.gameSystem = source.gameSystem;
                                if (!source.sourceLabel.isEmpty()) {
                                    rm.sourceLabel = source.sourceLabel;
                                }
                                allMonsters.add(rm);
                                count++;

                                if (progressListener != null && count % 50 == 0) {
                                    progressListener.onProgress(count, totalFiles, "Parsed " + count + " of " + totalFiles + "…");
                                }
                            } catch (Exception e) {
                                Logger.logError("Failed to parse reference monster: " + file.getName(), e);
                            }
                        }
                    }
                }
            }

            if (isCancelled.getAsBoolean()) throw new InterruptedException("Compendium parsing was cancelled");

            if (progressListener != null) progressListener.onProgress(count, totalFiles, "Saving reference monsters…");
            // Atomic transaction replacement
            db.referenceMonsterDAO().replaceSourceMonstersSync(source.id, allMonsters);

            markSourceDownloaded(context, source.id, count);
            return count;
        } finally {
            deleteFileOrDir(extractDir);
        }
    }

    private static void downloadZip(String urlStr, File dest, BooleanSupplier isCancelled, ProgressListener progressListener) throws IOException, InterruptedException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(30000);

        int code = conn.getResponseCode();
        if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER) {
            String newUrl = conn.getHeaderField("Location");
            conn = (HttpURLConnection) new URL(newUrl).openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(30000);
            code = conn.getResponseCode();
        }

        if (code != 200) {
            throw new IllegalStateException("Git repo download failed with HTTP " + code);
        }

        int contentLength = conn.getContentLength();
        try (InputStream in = new BufferedInputStream(conn.getInputStream());
             FileOutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[8192];
            int count;
            int totalDownloaded = 0;
            while ((count = in.read(buffer)) != -1) {
                if (isCancelled.getAsBoolean()) {
                    throw new InterruptedException("Git download was cancelled.");
                }
                out.write(buffer, 0, count);
                totalDownloaded += count;
                if (progressListener != null && contentLength > 0) {
                    int percent = (int) ((totalDownloaded / (float) contentLength) * 100);
                    progressListener.onProgress(percent, 100, "Downloading zip (" + (totalDownloaded / 1024 / 1024) + " MB)…");
                }
            }
        }
    }

    private static void unzipAndFilter(File zipFile, File extractDir, String filterExtension, String subfolder, BooleanSupplier isCancelled) throws InterruptedException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (isCancelled.getAsBoolean()) throw new InterruptedException("Git extraction was cancelled.");
                if (!entry.isDirectory()) {
                    String name = entry.getName();
                    boolean inSubfolder = subfolder == null || subfolder.isEmpty() || name.contains("/" + subfolder + "/") || name.startsWith(subfolder + "/");
                    if (inSubfolder && (filterExtension == null || name.endsWith(filterExtension))) {
                        File outFile = new File(extractDir, new File(name).getName());
                        if (outFile.exists()) {
                            outFile = new File(extractDir, System.currentTimeMillis() + "_" + new File(name).getName());
                        }
                        try (FileOutputStream fos = new FileOutputStream(outFile)) {
                            byte[] buffer = new byte[8192];
                            int count;
                            while ((count = zis.read(buffer)) != -1) {
                                if (isCancelled.getAsBoolean()) throw new InterruptedException("Extraction cancelled.");
                                fos.write(buffer, 0, count);
                            }
                        } catch (InterruptedException e) {
                            throw e;
                        } catch (Exception e) {
                            Logger.logError("Failed to write extracted file: " + name, e);
                        }
                    }
                }
                zis.closeEntry();
            }
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            Logger.logError("Failed during unzip stream processing", e);
        }
    }

    private static String readFileContent(File file) {
        StringBuilder sb = new StringBuilder();
        try (InputStream in = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            return null;
        }
        return sb.toString();
    }

    private static void deleteFileOrDir(File fileOrDir) {
        if (fileOrDir == null || !fileOrDir.exists()) return;
        if (fileOrDir.isDirectory()) {
            File[] children = fileOrDir.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteFileOrDir(child);
                }
            }
        }
        fileOrDir.delete();
    }
}
