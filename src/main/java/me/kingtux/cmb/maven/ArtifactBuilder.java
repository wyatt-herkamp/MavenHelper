package me.kingtux.cmb.maven;

import java.util.List;

public class ArtifactBuilder {
    private String artifactId;
    private String groupId;
    private Repository repository;
    private List<String> versions;
    private String latestVersion;

    public ArtifactBuilder setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public ArtifactBuilder setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public ArtifactBuilder setRepository(Repository repository) {
        this.repository = repository;
        return this;
    }

    public ArtifactBuilder setVersions(List<String> versions) {
        this.versions = versions;
        return this;
    }

    public ArtifactBuilder setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
        return this;
    }

    public Artifact createArtifact() {
        if (latestVersion == null) latestVersion = versions.get(0);
        return new Artifact(artifactId, groupId, repository, versions, latestVersion);
    }
}