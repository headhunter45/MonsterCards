package com.majinnaibu.monstercards.models;

public class ImportSource {
    public enum ImportType {
        GIT_ARCHIVE,
        OPEN5E_API
    }

    public String id;
    public String projectName;
    public String creatorName;
    public String creatorPageLink;
    public ImportType importType;

    // Optional attributes specific to Git archive
    public String downloadUrl;
    public String importerClassName;
    public String fileExtension;

    public ImportSource(String id, String projectName, String creatorName, String creatorPageLink, ImportType importType, String downloadUrl, String importerClassName, String fileExtension) {
        this.id = id;
        this.projectName = projectName;
        this.creatorName = creatorName;
        this.creatorPageLink = creatorPageLink;
        this.importType = importType;
        this.downloadUrl = downloadUrl;
        this.importerClassName = importerClassName;
        this.fileExtension = fileExtension;
    }
}
