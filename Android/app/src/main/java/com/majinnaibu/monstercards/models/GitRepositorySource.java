package com.majinnaibu.monstercards.models;

public class GitRepositorySource {
    public String id;
    public String projectName;
    public String creatorGithubName;
    public String creatorPageLink;
    public String downloadUrl;
    public String importerClassName;
    public String fileExtension;

    public GitRepositorySource(String id, String projectName, String creatorGithubName, String creatorPageLink, String downloadUrl, String importerClassName, String fileExtension) {
        this.id = id;
        this.projectName = projectName;
        this.creatorGithubName = creatorGithubName;
        this.creatorPageLink = creatorPageLink;
        this.downloadUrl = downloadUrl;
        this.importerClassName = importerClassName;
        this.fileExtension = fileExtension;
    }
}
