package me.kingtux.cmb.maven;

import com.google.gson.JsonObject;

public class Repository {
    private String url;
    private String repositoryID;
    private String name;

    public Repository(JsonObject jsonObject) {
        url = jsonObject.get("url").getAsString();
        repositoryID = jsonObject.get("url").getAsString();
        name = jsonObject.get("url").getAsString();
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
}
