package me.kingtux.cmb.maven;

import me.kingtux.cmb.MavenHelper;
import okhttp3.Request;
import okhttp3.Response;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MavenResolver {
    private List<Repository> repositoryList;

    public MavenResolver(List<Repository> repositoryList) {
        this.repositoryList = repositoryList;
    }

    public Optional<Artifact> artifact(String groupID, String artifactId) {
        for (Repository repository : repositoryList) {
            Optional<Artifact> artifact = artifact(groupID, artifactId, repository);
            if (artifact.isPresent()) return artifact;
        }
        return Optional.empty();
    }

    public Optional<Artifact> artifact(String groupID, String artifactId, Repository repository) {
        Document mavenMetadata = null;
        try {
            mavenMetadata = getMavenMetadata(groupID, artifactId, repository);
        } catch (IOException | DocumentException e) {
            MavenHelper.LOGGER.error("Unable to pull maven-metadata.xml", e);
            return Optional.empty();
        }
        if (mavenMetadata == null) return Optional.empty();
        ArtifactBuilder artifactBuilder = new ArtifactBuilder();
        artifactBuilder.setArtifactId(artifactId);
        artifactBuilder.setGroupId(groupID);
        List<String> versions = new ArrayList<>();
        Element root = mavenMetadata.getRootElement();
        Element versioning = root.element("versioning");
        Element latest = versioning.element("latest");
        if (latest != null) {
            artifactBuilder.setLatestVersion(latest.getStringValue());
        }
        Element versionsElement = versioning.element("versions");
        for (Element element : versionsElement.elements()) {
            versions.add(element.getStringValue());
        }
        artifactBuilder.setVersions(versions);
        return Optional.of(artifactBuilder.createArtifact());

    }

    public Optional<Repository> getRepository(String id) {
        return repositoryList.stream().filter(repository -> repository.getRepositoryID().equals(id)).findFirst();
    }

    private Document getMavenMetadata(String groupID, String artifactId, Repository repository) throws IOException, DocumentException {
        String url = getArtifactURL(groupID, artifactId, repository) + "/maven-metadata.xml";
        MavenHelper.LOGGER.debug("Maven-METADATA.xml URL: " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response execute = MavenHelper.CLIENT.newCall(request).execute();
        if (execute.code() == 404) return null;
        SAXReader reader = new SAXReader();
        Document doc = reader.read(execute.body().byteStream());
        return doc;
    }

    private String getArtifactURL(String groupID, String artifactId, Repository repository) {
        StringBuilder stringBuilder = new StringBuilder().append(repository.getURL()).append("/");
        stringBuilder.append(groupID.replace(".", "/")).append("/");
        stringBuilder.append(artifactId);

        return stringBuilder.toString();
    }

    public List<Repository> getRepositoryList() {
        return repositoryList;
    }
}
