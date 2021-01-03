package me.kingtux.mvnhelper;

import com.google.gson.JsonObject;

public class Config {
    private JsonObject jsonObject;

    public Config(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getColor() {
        return jsonObject.get("color").getAsString();
    }

    public String getBaseURL() {
        return jsonObject.get("base_url").getAsString();
    }

    public String getHighlightJS() {
        return getCDNs().get("highlight-js").getAsString();
    }

    public String getHighlightCSS() {
        return getCDNs().get("highlight-css").getAsString();
    }

    public String getSemanticCSS() {
        return getCDNs().get("semantic-css").getAsString();
    }

    public String getSemanticJS() {
        return getCDNs().get("semantic-js").getAsString();
    }

    public String getJquery() {
        return getCDNs().get("jquery").getAsString();
    }

    public JsonObject getCDNs() {
        return jsonObject.get("cdn").getAsJsonObject();
    }
}
