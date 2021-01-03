package me.kingtux.mvnhelper.handlers;

import io.javalin.http.Context;
import me.kingtux.mvnhelper.MavenHelper;
import me.kingtux.mvnhelper.maven.Artifact;
import me.kingtux.mvnhelper.maven.Repository;
import me.kingtux.mvnhelper.web.WebMetadataBuilder;

import java.io.IOException;
import java.util.Optional;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class ArtifactHandler {
    private MavenHelper mavenHelper;

    public ArtifactHandler(MavenHelper mavenHelper) {
        this.mavenHelper = mavenHelper;
    }

    public void artifactInfo(Context context) {
        String repo = context.pathParam("repo");
        Repository repository = null;
        if (!repo.equals("null")) {
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
            repository = repositoryOptional.get();
        }
        String groupID = context.pathParam("group");
        String artifactID = context.pathParam("artifact");
        Optional<Artifact> artifactOptional;
        if (repository != null) {
            artifactOptional = mavenHelper.getResolver().artifact(groupID, artifactID, repository);
        } else {
            artifactOptional = mavenHelper.getResolver().artifact(groupID, artifactID);
        }
        if (artifactOptional.isEmpty()) {
            WebMetadataBuilder builder = new WebMetadataBuilder();
            builder.setTitle("MavenHelper");
            builder.setDescription("404 page");
            context.render("error/404.peb", model(
                    "metadata", builder.createWebMetadata(),
                    "config", mavenHelper.getConfig(),
                    "missing", "The artifact " + groupID + ":" + artifactID + " is not found",
                    "more_info", repo == null ? "Artifact is not available in any registered repo" : "Artifact is not available in " + repo));
            context.status(404);
            return;
        }
        Artifact artifact = artifactOptional.get();
        try {
            mavenHelper.getArtifactSaver().saveArtifactIfNotFound(artifact);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebMetadataBuilder builder = new WebMetadataBuilder();
        builder.setTitle(groupID + ":" + artifactID);
        builder.setDescription("Maven Info for " + groupID + ":" + artifactID);
        builder.setImage(BadgeHandler.generateBadgeURL(artifact));
        context.render("artifact.peb", model("artifact", artifact,
                "url", mavenHelper.getConfig().getBaseURL(),
                "badge_url", BadgeHandler.generateBadgeURL(artifact),
                "artifact_url", generateArtifactURL(artifact),
                "repo_url", RepositoryHandler.generateArtifactURL(artifact.getRepository()),
                "metadata", builder.createWebMetadata(),
                "config", mavenHelper.getConfig()));
    }

    public void artifactInfoJson(Context context) {
        String repo = context.pathParam("repo");
        Repository repository = null;
        if (!repo.equals("null")) {
            Optional<Repository> repositoryOptional = mavenHelper.getResolver().getRepository(repo);
            if (repositoryOptional.isEmpty()) {
                //TODO improve this
                context.status(404);
                return;
            }
            repository = repositoryOptional.get();
        }
        String groupID = context.pathParam("group");
        String artifactID = context.pathParam("artifact");
        Optional<Artifact> artifactOptional;
        if (repository != null) {
            artifactOptional = mavenHelper.getResolver().artifact(groupID, artifactID, repository);
        } else {
            artifactOptional = mavenHelper.getResolver().artifact(groupID, artifactID);
        }
        if (artifactOptional.isEmpty()) {
            context.status(404);
            return;
        }
        Artifact artifact = artifactOptional.get();
        String jsonObject = mavenHelper.getGson().toJson(artifact);
        context.contentType("application/json");
        context.result(jsonObject);
    }

    public static String generateArtifactURL(Artifact artifact) {
        return MavenHelper.getMavenHelper().getConfig().getBaseURL() + "/" + artifact.getRepository().getRepositoryID() + "/" + artifact.getGroupId() + "/" + artifact.getArtifactId();
    }
}
