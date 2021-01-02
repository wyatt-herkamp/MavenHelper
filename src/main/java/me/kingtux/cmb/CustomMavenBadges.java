package me.kingtux.cmb;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CustomMavenBadges {
    public static Logger LOGGER = LoggerFactory.getLogger(CustomMavenBadges.class);
    private Javalin javalin;
    private Gson gson = new Gson();
    private JsonObject jsonObject;
    private final String BADGE_URL = "https://img.shields.io/badge/%1$s-%2$s-%3$s";
    private Map<String, JsonObject> repositories = new HashMap<>();
    public static final OkHttpClient CLIENT = new OkHttpClient();

    public CustomMavenBadges(File file) throws FileNotFoundException {
        jsonObject = gson.fromJson(new FileReader(file), JsonObject.class);
        javalin = Javalin.create(javalinConfig -> {


        }).start(jsonObject.get("port").getAsInt());
        loadRepos();
        javalin.get("/", this::index);
        javalin.get("/:repo/:group/:artifact/badge.png", this::getBadge);
    }

    private void loadRepos() {
        for (JsonElement jsonElement : jsonObject.get("repositories").getAsJsonArray()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            repositories.put(asJsonObject.get("id").getAsString(), asJsonObject);
        }
    }

    private void getBadge(Context context) {
        JsonObject jsonObject = repositories.get(context.pathParam("repo"));
        if (jsonObject == null) {
            //TODO improve this
            context.status(404);
            return;
        }
        String groupID = context.pathParam("group");
        String artifact = context.pathParam("artifact");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(jsonObject.get("url").getAsString()).append("/");
        stringBuilder.append(groupID.replace(".", "/")).append("/");
        stringBuilder.append(artifact).append("/");
        stringBuilder.append("maven-metadata.xml");
        String path = stringBuilder.toString();
        try {
            String latestVersion = MavenUtils.getLatestVersion(path);
            File badge = BadgeUtils.getBadge(latestVersion, jsonObject.get("name").getAsString(), this.jsonObject.get("color").getAsString());
            context.contentType("image/png");
            context.result(new FileInputStream(badge));
        } catch (ExecutionException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void index(Context context) {
        context.result("Hi!");

    }
}
