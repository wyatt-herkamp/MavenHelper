package me.kingtux.cmb.handlers;

import io.javalin.http.Context;
import me.kingtux.cmb.MavenHelper;
import me.kingtux.cmb.maven.Artifact;
import me.kingtux.cmb.maven.Repository;

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
        Optional<Artifact> artifact = mavenHelper.getResolver().artifact(groupID, artifactID, repository);
        if (artifact.isEmpty()) {
            context.status(404);
            return;
        }
        context.render("artifact.peb", model(artifact, artifact.get()));
    }
}
