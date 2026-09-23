package com.majinnaibu.monstercards.importers;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.majinnaibu.monstercards.MonsterCardsApplication;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.ImportSource;
import com.majinnaibu.monstercards.models.Monster;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.function.BooleanSupplier;

public class GitRepoImporterService {

    public static int importFromGitRepository(Context context, ImportSource source, BooleanSupplier isCancelled) throws Exception {
        int totalImported = 0;
        
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }
        
        File tempZip = null;
        File[] existingZips = downloadsDir.listFiles((dir, name) -> name.startsWith(source.id + "_download") && name.endsWith(".zip"));
        if (existingZips != null && existingZips.length > 0) {
            tempZip = existingZips[0];
        } else {
            tempZip = new File(downloadsDir, source.id + "_download_" + System.currentTimeMillis() + ".zip");
            downloadZip(source.downloadUrl, tempZip, isCancelled);
        }
        
        File extractDir = new File(context.getCacheDir(), "repo_extracted_" + System.currentTimeMillis());
        
        try {
            if (isCancelled.getAsBoolean()) return 0;
            
            if (!extractDir.exists()) {
                extractDir.mkdirs();
            }
            
            int extractedFiles = unzipAndFilter(tempZip, extractDir, source.fileExtension, source.subfolder, isCancelled);
            Logger.logWTF("Extracted " + extractedFiles + " files to " + extractDir.getAbsolutePath());
            
            if (isCancelled.getAsBoolean()) return 0;
            
            // Instantiate Importer dynamically
            Class<?> clazz = Class.forName(source.importerClassName);
            @SuppressWarnings("unchecked")
            EntityImporter<Monster> importer = (EntityImporter<Monster>) clazz.newInstance();
            
            MonsterRepository repository = ((MonsterCardsApplication) context.getApplicationContext()).getMonsterRepository();
            
            // Process files
            totalImported = processDirectory(extractDir, importer, repository, isCancelled);

        } finally {
            // We no longer delete tempZip since the user wants to keep the downloaded zip
            // deleteFileOrDir(tempZip);
            // DO NOT DELETE EXTRACT DIR FOR NOW
            // deleteFileOrDir(extractDir);
            Logger.logWTF("Skipping deletion of extract directory: " + extractDir.getAbsolutePath());
        }
        
        return totalImported;
    }

    private static void downloadZip(String urlStr, File dest, BooleanSupplier isCancelled) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(30000);

        int code = conn.getResponseCode();
        // GitHub might redirect archive links, so handle redirect
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

        try (InputStream in = new BufferedInputStream(conn.getInputStream());
             FileOutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = in.read(buffer)) != -1) {
                if (isCancelled.getAsBoolean()) break;
                out.write(buffer, 0, count);
            }
        }
    }

    private static int unzipAndFilter(File zipFile, File extractDir, String filterExtension, String subfolder, BooleanSupplier isCancelled) throws IOException {
        int extractedCount = 0;
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (isCancelled.getAsBoolean()) break;
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
                                if (isCancelled.getAsBoolean()) break;
                                fos.write(buffer, 0, count);
                            }
                        }
                        extractedCount++;
                    }
                }
                zis.closeEntry();
            }
        }
        return extractedCount;
    }

    private static int processDirectory(File dir, EntityImporter<Monster> importer, MonsterRepository repository, BooleanSupplier isCancelled) {
        int importedCount = 0;
        File[] files = dir.listFiles();
        if (files == null) {
            Logger.logWTF("processDirectory: files array is null for " + dir.getAbsolutePath());
            return 0;
        }
        
        Logger.logWTF("Attempting to process " + files.length + " JSON files.");
        
        for (File file : files) {
            if (isCancelled.getAsBoolean()) break;
            if (file.isFile()) {
                String content = readFileContent(file);
                if (content != null) {
                    if (importer.canImport(content)) {
                        try {
                            Monster monster = importer.parse(content);
                            repository.saveMonster(monster).blockingAwait();
                            importedCount++;
                        } catch (Exception e) {
                            Logger.logError("Failed to parse/save monster from file: " + file.getName(), e);
                        }
                    } else {
                        Logger.logWTF("Importer rejected file: " + file.getName());
                    }
                } else {
                    Logger.logError("Failed to read content from file: " + file.getName());
                }
            }
        }
        return importedCount;
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
            Logger.logError("Error reading extracted file", e);
            return null;
        }
        return sb.toString();
    }

    private static void deleteFileOrDir(File fileOrDir) {
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
