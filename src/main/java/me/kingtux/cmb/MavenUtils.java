package me.kingtux.cmb;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import okhttp3.Request;
import okhttp3.Response;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MavenUtils {
    private static LoadingCache<String, String> mavenVersionCache;


    static {
        mavenVersionCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build(
                new CacheLoader<String, String>() {
                    public String load(String string) throws Exception {

                        Request request = new Request.Builder()
                                .url(string)
                                .build();
                        Response execute = MavenHelper.CLIENT.newCall(request).execute();
                        SAXReader reader = new SAXReader();
                        Document doc = reader.read(execute.body().byteStream());
                        Element root = doc.getRootElement();
                        Element versioning = root.element("versioning");
                        Element latest = versioning.element("latest");
                        if (latest == null) {
                            //TODO Improve this.
                            Element versions = versioning.element("versions");
                            latest = versions.elements().get(0);
                        }
                        String stringValue = latest.getStringValue();
                        return stringValue;
                    }
                }
        );
    }

    public static String getLatestVersion(String path) throws ExecutionException {
        return mavenVersionCache.get(path);
    }
}
