package com.majinnaibu.monstercards.models;

import com.majinnaibu.monstercards.data.enums.GameSystem;

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

    // Compendium metadata
    public GameSystem gameSystem;
    public String sourceLabel;
    public String bookSource;
    public String description;
    public String estimatedSize;

    // Optional attributes specific to Git archive
    public String downloadUrl;
    public String importerClassName;
    public String fileExtension;
    public String subfolder;

    public ImportSource(String id, String projectName, String creatorName, String creatorPageLink, ImportType importType, String downloadUrl, String importerClassName, String fileExtension, String subfolder) {
        this(id, projectName, creatorName, creatorPageLink, importType, GameSystem.DND_5E, "", "", "", "~10 MB", downloadUrl, importerClassName, fileExtension, subfolder);
    }

    public ImportSource(String id, String projectName, String creatorName, String creatorPageLink, ImportType importType,
                        GameSystem gameSystem, String sourceLabel, String bookSource, String description, String estimatedSize,
                        String downloadUrl, String importerClassName, String fileExtension, String subfolder) {
        this.id = id;
        this.projectName = projectName;
        this.creatorName = creatorName;
        this.creatorPageLink = creatorPageLink;
        this.importType = importType;
        this.gameSystem = gameSystem != null ? gameSystem : GameSystem.DND_5E;
        this.sourceLabel = sourceLabel != null ? sourceLabel : "";
        this.bookSource = bookSource != null ? bookSource : "";
        this.description = description != null ? description : "";
        this.estimatedSize = estimatedSize != null ? estimatedSize : "";
        this.downloadUrl = downloadUrl;
        this.importerClassName = importerClassName;
        this.fileExtension = fileExtension;
        this.subfolder = subfolder;
    }
}
