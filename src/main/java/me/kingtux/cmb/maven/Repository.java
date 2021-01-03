package me.kingtux.cmb.maven;

import com.google.gson.JsonObject;

import java.util.Objects;

public class Repository {
    private String url;
    private String repositoryID;
    private String name;

    public Repository(JsonObject jsonObject) {
        url = jsonObject.get("url").getAsString();
        repositoryID = jsonObject.get("id").getAsString();
        name = jsonObject.get("name").getAsString();
    }

    public String getURL() {
        return url;
    }

    public String getRepositoryID() {
        return repositoryID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "url='" + url + '\'' +
                ", repositoryID='" + repositoryID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repository that = (Repository) o;
        return Objects.equals(url, that.url) && Objects.equals(getRepositoryID(), that.getRepositoryID()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, getRepositoryID(), getName());
    }
}
