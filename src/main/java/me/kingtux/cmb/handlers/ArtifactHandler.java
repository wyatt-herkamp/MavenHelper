package me.kingtux.cmb.handlers;

import com.google.gson.JsonObject;
import io.javalin.http.Context;
import me.kingtux.cmb.BadgeUtils;
import me.kingtux.cmb.MavenHelper;
import me.kingtux.cmb.MavenUtils;
import me.kingtux.cmb.maven.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ArtifactHandler {
    private MavenHelper mavenHelper;

    public ArtifactHandler(MavenHelper mavenHelper) {
        this.mavenHelper = mavenHelper;
    }
    public void artifactInfo(Context context) {
        Optional<Repository> repositoryOptional = mavenHelper.getResolver().getRepository(context.pathParam("repo"));
        if (repositoryOptional.isEmpty()) {
            //TODO improve this
            context.status(404);
            return;
        }
        Repository repository = repositoryOptional.get();
        String groupID = context.pathParam("group");
        String artifact = context.pathParam("artifact");

    }
}
