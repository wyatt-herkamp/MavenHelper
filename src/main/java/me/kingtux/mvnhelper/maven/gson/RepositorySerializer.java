package me.kingtux.mvnhelper.maven.gson;

import com.google.gson.*;
import me.kingtux.mvnhelper.maven.Repository;

import java.lang.reflect.Type;

public class RepositorySerializer implements JsonSerializer<Repository>, JsonDeserializer<Repository> {
    @Override
    public Repository deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        return new Repository(jo);
    }

    @Override
    public JsonElement serialize(Repository src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("url", src.getURL());
        jsonObject.addProperty("id", src.getRepositoryID());
        jsonObject.addProperty("name", src.getName());
        return jsonObject;
    }
}
