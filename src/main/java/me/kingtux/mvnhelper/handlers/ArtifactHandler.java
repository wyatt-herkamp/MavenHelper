package me.kingtux.mvnhelper.handlers;

import io.javalin.http.Context;
import me.kingtux.mvnhelper.MavenHelper;
import me.kingtux.mvnhelper.maven.Artifact;
import me.kingtux.mvnhelper.maven.Repository;
import me.kingtux.mvnhelper.web.WebMetadataBuilder;

import java.util.Optional;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

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
        String artifactID = context.pathParam("artifact");
        Optional<Artifact> artifactOptional = mavenHelper.getResolver().artifact(groupID, artifactID, repository);
        if (artifactOptional.isEmpty()) {
            context.status(404);
            return;
        }
        Artifact artifact = artifactOptional.get();

        WebMetadataBuilder builder = new WebMetadataBuilder();
        builder.setTitle(groupID + ":" + artifactID);
        builder.setDescription("Maven Info for " + groupID + ":" + artifactID);
        builder.setImage(BadgeHandler.generateBadgeURL(artifact));
        context.render("artifact.peb", model("artifact", artifact,
                "url", mavenHelper.getConfig().getBaseURL(),
                "badge_url", BadgeHandler.generateBadgeURL(artifact),
                "artifact_url", generateArtifactURL(artifact),
                "repo_url", RepositoryHandler.generateArtifactURL(artifact.getRepository()),
                "metadata", builder.createWebMetadata()));
    }

    public void artifactInfoJson(Context context) {
        Optional<Repository> repositoryOptional = mavenHelper.getResolver().getRepository(context.pathParam("repo"));
        if (repositoryOptional.isEmpty()) {
            //TODO improve this
            context.status(404);
            return;
        }
        Repository repository = repositoryOptional.get();
        String groupID = context.pathParam("group");
        String artifactID = context.pathParam("artifact");
        Optional<Artifact> artifactOptional = mavenHelper.getResolver().artifact(groupID, artifactID, repository);
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
