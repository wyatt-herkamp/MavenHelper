package me.kingtux.mvnhelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.kingtux.mvnhelper.maven.Artifact;
import me.kingtux.mvnhelper.maven.Repository;

import java.io.*;

public class ArtifactSaver {
    private File repos = new File("repos");

    public ArtifactSaver() {
        if (!repos.exists()) repos.mkdir();
    }

    public void saveArtifactIfNotFound(Artifact artifact) throws IOException {
        if (doesArtifactExist(artifact)) return;
        File repoFile = getRepoFile(artifact.getRepository());
        JsonObject jsonObject = repoFile.exists() ? MavenHelper.getMavenHelper().getGson().fromJson(new FileReader(repoFile), JsonObject.class) : new JsonObject();
        if (jsonObject == null) jsonObject = new JsonObject();
        JsonArray artifacts = jsonObject.has("artifacts") ? jsonObject.get("artifacts").getAsJsonArray() : new JsonArray();
        artifacts.add(artifact.getGroupId() + ":" + artifact.getArtifactId());
        jsonObject.add("artifacts", artifacts);
        if (!repoFile.exists()) repoFile.delete();
        repoFile.createNewFile();
        FileWriter fileWriter = new FileWriter(repoFile);
        MavenHelper.getMavenHelper().getGson().toJson(jsonObject, fileWriter);
        fileWriter.close();
    }

    public boolean doesArtifactExist(Artifact artifact) throws FileNotFoundException {
        File repoFile = getRepoFile(artifact.getRepository());

        if (!repoFile.exists()) return false;

        JsonObject jsonObject = MavenHelper.getMavenHelper().getGson().fromJson(new FileReader(repoFile), JsonObject.class);
        if (jsonObject == null) return false;
        if (!jsonObject.has("artifacts")) return false;
        JsonArray artifacts = jsonObject.get("artifacts").getAsJsonArray();
        for (JsonElement jsonElement : artifacts) {
            if (jsonElement.getAsString().equals(artifact.getGroupId() + ":" + artifact.getArtifactId())) {
                return true;
            }
        }
        return false;
    }

    public JsonArray getArtifacts(File file) throws FileNotFoundException {
        JsonObject jsonObject = file.exists() ? MavenHelper.getMavenHelper().getGson().fromJson(new FileReader(file), JsonObject.class) : new JsonObject();
        if (jsonObject == null) jsonObject = new JsonObject();
        JsonArray artifacts = jsonObject.has("artifacts") ? jsonObject.get("artifacts").getAsJsonArray() : new JsonArray();
        return artifacts;
    }

    public File getRepoFile(Repository repository) {
        return new File(repos, repository.getRepositoryID() + ".json");
    }
}
