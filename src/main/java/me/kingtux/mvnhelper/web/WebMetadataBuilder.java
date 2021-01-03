package me.kingtux.mvnhelper.web;

import me.kingtux.mvnhelper.MavenHelper;

public class WebMetadataBuilder {
    private String description;
    private String title;
    private String image;
    private String url = MavenHelper.getMavenHelper().getConfig().getBaseURL();

    public WebMetadataBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public WebMetadataBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public WebMetadataBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public WebMetadataBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public WebMetadata createWebMetadata() {
        return new WebMetadata(description, title, image, url);
    }
}