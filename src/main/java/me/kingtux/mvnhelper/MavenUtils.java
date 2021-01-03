package me.kingtux.mvnhelper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.kingtux.mvnhelper.maven.Artifact;
import me.kingtux.mvnhelper.maven.ArtifactRequest;
import me.kingtux.mvnhelper.maven.Repository;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MavenUtils {
    private static LoadingCache<ArtifactRequest, Artifact> mavenVersionCache;


    static {
        mavenVersionCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build(
                new CacheLoader<>() {
                    public Artifact load(ArtifactRequest data) throws IOException {
                        Artifact artifact = MavenHelper.getMavenHelper().getResolver().artifact(data).orElse(null);
                        if(artifact!=null) MavenHelper.getMavenHelper().getArtifactSaver().saveArtifactIfNotFound(artifact);
                        return artifact;
                    }
                }
        );
    }

    public static Artifact getLatestVersion(String groupID, String artifactID, Repository repository) throws ExecutionException {
        return mavenVersionCache.get(new ArtifactRequest(groupID, artifactID, repository));
    }

}
