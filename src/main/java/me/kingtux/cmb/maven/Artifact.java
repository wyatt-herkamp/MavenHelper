package me.kingtux.cmb.maven;

import java.util.List;

public class Artifact {
    private String artifactId;
    private String groupId;
    private Repository repository;
    private List<String> versions;
    private String latestVersion;

    public Artifact(String artifactId, String groupId, Repository repository, List<String> versions, String latestVersion) {
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.repository = repository;
        this.versions = versions;
        this.latestVersion = latestVersion;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public Repository getRepository() {
        return repository;
    }

    public List<String> getVersions() {
        return versions;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}
