package me.kingtux.cmb;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinPebble;
import me.kingtux.cmb.handlers.BadgeHandler;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class MavenHelper {
    public static Logger LOGGER = LoggerFactory.getLogger(MavenHelper.class);
    private Javalin javalin;
    private Gson gson = new Gson();
    private JsonObject jsonObject;
    private Map<String, JsonObject> repositories = new HashMap<>();
    public static final OkHttpClient CLIENT = new OkHttpClient();
    private Config config;

    public MavenHelper(File file) throws FileNotFoundException {
        jsonObject = gson.fromJson(new FileReader(file), JsonObject.class);
        javalin = Javalin.create(javalinConfig -> {

        }).start(jsonObject.get("port").getAsInt());
        JavalinRenderer.register(JavalinPebble.INSTANCE, "peb");
        loadRepos();
        config = new Config(jsonObject);
        javalin.get("/", this::index);
        BadgeHandler badgeHandler = new BadgeHandler(this);
        javalin.get("/:repo/:group/:artifact/badge.png", badgeHandler::getBadge);
    }

    private void loadRepos() {
        for (JsonElement jsonElement : jsonObject.get("repositories").getAsJsonArray()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            repositories.put(asJsonObject.get("id").getAsString(), asJsonObject);
        }
    }


    private void index(Context context) {
        context.render("index.peb", model("url", jsonObject.get("base_url").getAsString(), "repos", repositories.keySet()));
    }

    public Map<String, JsonObject> getRepositories() {
        return repositories;
    }

    public Config getConfig() {
        return null;
    }
}
