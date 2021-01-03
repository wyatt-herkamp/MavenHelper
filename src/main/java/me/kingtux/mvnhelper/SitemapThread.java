package me.kingtux.mvnhelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.kingtux.mvnhelper.maven.Repository;
import me.kingtux.tuxjsitemap.SiteMapGenerator;

import java.io.File;
import java.io.IOException;

public class SitemapThread extends Thread {
    private MavenHelper mavenHelper;
    private File sitemap = new File("sitemap");

    public SitemapThread(MavenHelper mavenHelper) {
        this.mavenHelper = mavenHelper;
        start();
    }

    @Override
    public void run() {
        while (true) {
            deleteFolderContents(sitemap);
            SiteMapGenerator siteMapGenerator = new SiteMapGenerator(mavenHelper.getConfig().getBaseURL());
            for (Repository repository : mavenHelper.getResolver().getRepositoryList()) {
                File repoFile = mavenHelper.getArtifactSaver().getRepoFile(repository);
                if (!repoFile.exists()) continue;
                try {
                    JsonArray artifacts = mavenHelper.getArtifactSaver().getArtifacts(repoFile);
                    for (JsonElement artifact : artifacts) {
                        System.out.println("artifacts.getAsString() = " + artifact.getAsString());
                        String[] string = artifact.getAsString().split(":");
                        siteMapGenerator.addURL(repository.getRepositoryID()+"/" + string[0] + "/" + string[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                siteMapGenerator.build().writeToFolder(sitemap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(mavenHelper.getConfig().getSitemapTimeout());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFolderContents(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolderContents(f);
                }
                f.delete();

            }
        }
    }
}
