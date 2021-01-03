package me.kingtux.mvnhelper.web;

public class WebMetadata {
    private String description;
    private String title;
    private String image;
    private String url;

    public WebMetadata(String description, String title, String image, String url) {
        this.description = description;
        this.title = title;
        this.image = image;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }
}
