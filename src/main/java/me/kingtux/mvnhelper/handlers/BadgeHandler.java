package me.kingtux.mvnhelper.handlers;

import io.javalin.http.Context;
import me.kingtux.mvnhelper.BadgeUtils;
import me.kingtux.mvnhelper.MavenHelper;
import me.kingtux.mvnhelper.MavenUtils;
import me.kingtux.mvnhelper.maven.Artifact;
import me.kingtux.mvnhelper.maven.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class BadgeHandler {
    private MavenHelper mavenHelper;

    public BadgeHandler(MavenHelper mavenHelper) {
        this.mavenHelper = mavenHelper;
    }


    public void getBadge(Context context) {
        System.out.println("HI");
        Optional<Repository> repositoryOptional = mavenHelper.getResolver().getRepository(context.pathParam("repo"));
        if (repositoryOptional.isEmpty()) {
            System.out.println("Null");
            context.status(404);
            return;
        }
        System.out.println("HI2");
        Repository repository = repositoryOptional.get();
        String groupID = context.pathParam("group");
        String artifactID = context.pathParam("artifact");

        try {
            Artifact artifact = MavenUtils.getLatestVersion(groupID, artifactID, repository);
            File badge = BadgeUtils.getBadge(artifact.getLatestVersion(), repository.getName(), mavenHelper.getConfig().getColor());
            context.contentType("image/png");
            context.result(new FileInputStream(badge));
        } catch (ExecutionException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String generateBadgeURL(Artifact artifact) {
        return MavenHelper.getMavenHelper().getConfig().getBaseURL() + "/" + artifact.getRepository().getRepositoryID() + "/" + artifact.getGroupId() + "/" + artifact.getArtifactId() + "/badge.png";
    }
}
