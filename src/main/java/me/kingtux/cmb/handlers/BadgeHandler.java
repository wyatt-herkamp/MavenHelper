package me.kingtux.cmb.handlers;

import com.google.gson.JsonObject;
import io.javalin.http.Context;
import me.kingtux.cmb.BadgeUtils;
import me.kingtux.cmb.MavenHelper;
import me.kingtux.cmb.MavenUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

public class BadgeHandler {
    private MavenHelper mavenHelper;

    public BadgeHandler(MavenHelper mavenHelper) {
        this.mavenHelper = mavenHelper;
    }


    public void getBadge(Context context) {
        JsonObject jsonObject = mavenHelper.getRepositories().get(context.pathParam("repo"));
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
            File badge = BadgeUtils.getBadge(latestVersion, jsonObject.get("name").getAsString(), mavenHelper.getConfig().getColor());
            context.contentType("image/png");
            context.result(new FileInputStream(badge));
        } catch (ExecutionException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
