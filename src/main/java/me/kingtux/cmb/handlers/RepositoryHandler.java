package me.kingtux.cmb.handlers;

import io.javalin.http.Context;
import me.kingtux.cmb.MavenHelper;
import me.kingtux.cmb.maven.Repository;

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
}
