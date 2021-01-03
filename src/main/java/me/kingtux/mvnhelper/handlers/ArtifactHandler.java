package me.kingtux.mvnhelper.handlers;

import io.javalin.http.Context;
import me.kingtux.mvnhelper.MavenHelper;
import me.kingtux.mvnhelper.maven.Artifact;
import me.kingtux.mvnhelper.maven.Repository;

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
        Artifact artifact1 = artifact.get();
        context.render("artifact.peb", model("artifact", artifact1));
    }
}
