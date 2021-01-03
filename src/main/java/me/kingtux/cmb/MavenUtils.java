package me.kingtux.cmb;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.kingtux.cmb.maven.Artifact;
import me.kingtux.cmb.maven.ArtifactRequest;
import me.kingtux.cmb.maven.Repository;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MavenUtils {
    private static LoadingCache<ArtifactRequest, Artifact> mavenVersionCache;


    static {
        mavenVersionCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build(
                new CacheLoader<>() {
                    public Artifact load(ArtifactRequest data){
                        return MavenHelper.getMavenHelper().getResolver().artifact(data).orElse(null);
                    }
                }
        );
    }

    public static Artifact getLatestVersion(String groupID, String artifactID, Repository repository) throws ExecutionException {
        return mavenVersionCache.get(new ArtifactRequest(groupID, artifactID, repository));
    }

}
