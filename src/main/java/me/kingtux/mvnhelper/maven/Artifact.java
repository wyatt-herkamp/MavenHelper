package me.kingtux.mvnhelper.maven;

import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Artifact{" +
                "artifactId='" + artifactId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", repository=" + repository.toString() +
                ", latestVersion='" + latestVersion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return Objects.equals(getArtifactId(), artifact.getArtifactId()) && Objects.equals(getGroupId(), artifact.getGroupId()) && Objects.equals(getRepository(), artifact.getRepository()) && Objects.equals(getVersions(), artifact.getVersions()) && Objects.equals(getLatestVersion(), artifact.getLatestVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getArtifactId(), getGroupId(), getRepository(), getVersions(), getLatestVersion());
    }
}
