package me.kingtux.mvnhelper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinPebble;
import me.kingtux.mvnhelper.handlers.ArtifactHandler;
import me.kingtux.mvnhelper.handlers.BadgeHandler;
import me.kingtux.mvnhelper.handlers.RepositoryHandler;
import me.kingtux.mvnhelper.maven.MavenResolver;
import me.kingtux.mvnhelper.maven.Repository;
import me.kingtux.mvnhelper.web.WebMetadataBuilder;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class MavenHelper {
    public static Logger LOGGER = LoggerFactory.getLogger(MavenHelper.class);
    private Javalin javalin;
    private Gson gson = new Gson();
    private JsonObject jsonObject;
    public static final OkHttpClient CLIENT = new OkHttpClient();
    private Config config;
    private MavenResolver resolver;
    private static MavenHelper mavenHelper;

    public MavenHelper(File file) throws FileNotFoundException {
        mavenHelper = this;
        jsonObject = gson.fromJson(new FileReader(file), JsonObject.class);
        javalin = Javalin.create(javalinConfig -> {

        }).start(jsonObject.get("port").getAsInt());
        JavalinRenderer.register(JavalinPebble.INSTANCE, "peb");
        loadRepos();
        config = new Config(jsonObject);
        javalin.get("/", this::index);
        BadgeHandler badgeHandler = new BadgeHandler(this);
        javalin.get("/:repo/:group/:artifact/badge.png", badgeHandler::getBadge);
        ArtifactHandler artifactHandler = new ArtifactHandler(this);
        javalin.get("/:repo/:group/:artifact", artifactHandler::artifactInfo);
        RepositoryHandler repositoryHandler = new RepositoryHandler(this);
        javalin.get("/:repo", repositoryHandler::repositoryInfo);
    }

    public static MavenHelper getMavenHelper() {
        return mavenHelper;
    }

    private void loadRepos() {
        List<Repository> repositoryList = new ArrayList<>();
        for (JsonElement jsonElement : jsonObject.get("repositories").getAsJsonArray()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            repositoryList.add(new Repository(asJsonObject));
        }
        resolver = new MavenResolver(repositoryList);
    }


    private void index(Context context) {
        WebMetadataBuilder builder = new WebMetadataBuilder();
        builder.setTitle("MavenHelper");
        builder.setDescription("A tool for having displaying data about Maven Repos and Depends from self-hosted Maven Repos");
        context.render("index.peb", model("url", jsonObject.get("base_url").getAsString(),
                "repos", resolver.getRepositoryList(),
                "metadata", builder.createWebMetadata()));
    }


    public Config getConfig() {
        return config;
    }

    public MavenResolver getResolver() {
        return resolver;
    }
}
