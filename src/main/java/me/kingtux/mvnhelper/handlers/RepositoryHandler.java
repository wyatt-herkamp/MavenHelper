package me.kingtux.mvnhelper.handlers;

import io.javalin.http.Context;
import me.kingtux.mvnhelper.MavenHelper;
import me.kingtux.mvnhelper.maven.Repository;
import me.kingtux.mvnhelper.web.WebMetadataBuilder;

import java.util.Optional;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class RepositoryHandler {
    private MavenHelper mavenHelper;

    public RepositoryHandler(MavenHelper mavenHelper) {
        this.mavenHelper = mavenHelper;
    }

    public void repositoryInfo(Context context) {
        String repo = context.pathParam("repo");
        Optional<Repository> repositoryOptional = mavenHelper.getResolver().getRepository(repo);
        if (repositoryOptional.isEmpty()) {
            WebMetadataBuilder builder = new WebMetadataBuilder();
            builder.setTitle("MavenHelper");
            builder.setDescription("404 page");
            context.render("error/404.peb", model(
                    "metadata", builder.createWebMetadata(),
                    "config", mavenHelper.getConfig(),
                    "missing", "The repository " + repo + " is not found",
                    "more_info", "Missing Repository"));
            context.status(404);
            return;
        }
        Repository repository = repositoryOptional.get();
        WebMetadataBuilder builder = new WebMetadataBuilder();
        builder.setTitle(repository.getName() + " Repo");
        builder.setDescription("Maven Info for " + repository.getName() + " at " + repository.getURL());
        context.render("repository.peb", model("repository", repository,
                "url", MavenHelper.getMavenHelper().getConfig().getBaseURL(),
                "metadata", builder.createWebMetadata(),
                "config", mavenHelper.getConfig()));
    }

    public void repositoryInfoJson(Context context) {
        Optional<Repository> repositoryOptional = mavenHelper.getResolver().getRepository(context.pathParam("repo"));
        if (repositoryOptional.isEmpty()) {
            //TODO improve this
            context.status(404);
            return;
        }
        String jsonObject = mavenHelper.getGson().toJson(repositoryOptional.get());
        context.contentType("application/json");
        context.result(jsonObject);

    }

    public static String generateArtifactURL(Repository repository) {
        return MavenHelper.getMavenHelper().getConfig().getBaseURL() + "/" + repository.getRepositoryID();
    }
}
