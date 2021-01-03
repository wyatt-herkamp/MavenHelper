package me.kingtux.cmb.maven;

import java.util.Objects;

public class ArtifactRequest {
    private String groupID;
    private String artifactID;
    private Repository repository;

    public ArtifactRequest(String groupID, String artifactID, Repository repository) {
        this.groupID = groupID;
        this.artifactID = artifactID;
        this.repository = repository;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getArtifactID() {
        return artifactID;
    }

    public Repository getRepository() {
        return repository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactRequest that = (ArtifactRequest) o;
        return Objects.equals(getGroupID(), that.getGroupID()) && Objects.equals(getArtifactID(), that.getArtifactID()) && Objects.equals(getRepository(), that.getRepository());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupID(), getArtifactID(), getRepository());
    }

    @Override
    public String toString() {
        return "ArtifactRequest{" +
                "groupID='" + groupID + '\'' +
                ", artifactID='" + artifactID + '\'' +
                ", repository=" + repository +
                '}';
    }
}
