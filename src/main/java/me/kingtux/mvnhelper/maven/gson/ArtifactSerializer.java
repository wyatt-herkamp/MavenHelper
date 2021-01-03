package me.kingtux.mvnhelper.maven.gson;

import com.google.gson.*;
import me.kingtux.mvnhelper.maven.Artifact;
import me.kingtux.mvnhelper.maven.ArtifactBuilder;
import me.kingtux.mvnhelper.maven.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArtifactSerializer implements JsonSerializer<Artifact>, JsonDeserializer<Artifact> {
    @Override
    public Artifact deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        ArtifactBuilder builder = new ArtifactBuilder();
        builder.setRepository(context.deserialize(jo.get("repository"), Repository.class));
        builder.setGroupId(jo.get("groupID").getAsString());
        builder.setArtifactId(jo.get("artifactID").getAsString());
        builder.setLatestVersion(jo.get("latestVersion").getAsString());
        List<String> versions = new ArrayList<>();
        JsonArray versionsJA = jo.get("versions").getAsJsonArray();
        for (JsonElement version : versionsJA) {
            versions.add(version.getAsString());
        }
        builder.setVersions(versions);
        return builder.createArtifact();
    }

    @Override
    public JsonElement serialize(Artifact src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("groupID", src.getGroupId());
        jsonObject.addProperty("artifactID", src.getArtifactId());
        jsonObject.addProperty("latestVersion", src.getLatestVersion());
        jsonObject.add("repository", context.serialize(src.getRepository()));
        JsonArray jsonElements = new JsonArray();
        for (String version : src.getVersions()) {
            jsonElements.add(version);
        }
        jsonObject.add("versions", jsonElements);

        return jsonObject;
    }
}
