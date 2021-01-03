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
}
