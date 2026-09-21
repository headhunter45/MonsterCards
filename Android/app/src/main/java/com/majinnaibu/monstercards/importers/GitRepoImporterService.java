package com.majinnaibu.monstercards.importers;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.majinnaibu.monstercards.MonsterCardsApplication;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.GitRepositorySource;
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

public class GitRepoImporterService {

    public static int importFromGitRepository(Context context, GitRepositorySource source) throws Exception {
        int totalImported = 0;
        
        File tempZip = File.createTempFile("repo_download", ".zip", context.getCacheDir());
        File extractDir = new File(context.getCacheDir(), "repo_extracted_" + System.currentTimeMillis());
        
        try {
            downloadZip(source.downloadUrl, tempZip);
            
            if (!extractDir.exists()) {
                extractDir.mkdirs();
            }
            
            unzipAndFilter(tempZip, extractDir, source.fileExtension);
            
            // Instantiate Importer dynamically
            Class<?> clazz = Class.forName(source.importerClassName);
            @SuppressWarnings("unchecked")
            EntityImporter<Monster> importer = (EntityImporter<Monster>) clazz.newInstance();
            
            MonsterRepository repository = ((MonsterCardsApplication) context.getApplicationContext()).getMonsterRepository();
            
            // Process files
            totalImported = processDirectory(extractDir, importer, repository);

        } finally {
            deleteFileOrDir(tempZip);
            deleteFileOrDir(extractDir);
        }
        
        return totalImported;
    }

    private static void downloadZip(String urlStr, File dest) throws IOException {
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
                out.write(buffer, 0, count);
            }
        }
    }

    private static void unzipAndFilter(File zipFile, File extractDir, String filterExtension) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String name = entry.getName();
                    if (filterExtension == null || name.endsWith(filterExtension)) {
                        File outFile = new File(extractDir, new File(name).getName());
                        if (outFile.exists()) {
                            // in case there are multiple files with same name in different folders
                            outFile = new File(extractDir, System.currentTimeMillis() + "_" + new File(name).getName());
                        }
                        try (FileOutputStream fos = new FileOutputStream(outFile)) {
                            byte[] buffer = new byte[8192];
                            int count;
                            while ((count = zis.read(buffer)) != -1) {
                                fos.write(buffer, 0, count);
                            }
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

    private static int processDirectory(File dir, EntityImporter<Monster> importer, MonsterRepository repository) {
        int importedCount = 0;
        File[] files = dir.listFiles();
        if (files == null) return 0;
        
        for (File file : files) {
            if (file.isFile()) {
                String content = readFileContent(file);
                if (content != null && importer.canImport(content)) {
                    try {
                        Monster monster = importer.parse(content);
                        repository.saveMonster(monster).blockingAwait();
                        importedCount++;
                    } catch (Exception e) {
                        Logger.logError("Failed to parse/save monster from file: " + file.getName(), e);
                    }
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
