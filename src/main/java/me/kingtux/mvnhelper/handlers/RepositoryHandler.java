package me.kingtux.mvnhelper.handlers;

import io.javalin.http.Context;
import me.kingtux.mvnhelper.MavenHelper;
import me.kingtux.mvnhelper.maven.Repository;

import java.util.Optional;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class RepositoryHandler {
    private MavenHelper mavenHelper;

    public RepositoryHandler(MavenHelper mavenHelper) {
        this.mavenHelper = mavenHelper;
    }

    public void repositoryInfo(Context context) {
        Optional<Repository> repositoryOptional = mavenHelper.getResolver().getRepository(context.pathParam("repo"));
        if (repositoryOptional.isEmpty()) {
            //TODO improve this
            context.status(404);
            return;
        }
        context.render("repository.peb", model("repository", repositoryOptional.get()));
    }

    public static String generateArtifactURL(Repository repository) {
        return MavenHelper.getMavenHelper().getConfig().getBaseURL() + "/" + repository.getRepositoryID();
    }
}
