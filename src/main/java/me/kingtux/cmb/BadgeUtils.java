package me.kingtux.cmb;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.tuple.Triple;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class BadgeUtils {
    private static LoadingCache<Triple<String, String, String>, File> mavenVersionCache;
    private static final String BADGE_URL = "https://img.shields.io/badge/%1$s-%2$s-%3$s.png";
    private static final String cacheFile = "%1$s-%2$s.png";

    private static File cacheFolder = new File("cache");

    static {
        cacheFolder.mkdir();
        cacheFolder.deleteOnExit();
        mavenVersionCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build(
                new CacheLoader<>() {
                    public File load(Triple<String, String, String> value) throws Exception {

                        String format = String.format(BADGE_URL, value.getLeft(), value.getMiddle(), value.getRight());
                        Request request = new Request.Builder()
                                .url(format)
                                .build();
                        CustomMavenBadges.LOGGER.debug("Badge URL: "+format);
                        Response execute = CustomMavenBadges.CLIENT.newCall(request).execute();
                        File file = new File(cacheFolder, String.format(cacheFile, value.getLeft(), value.getMiddle()));
                        if (!file.exists()) {

                            BufferedInputStream input = new BufferedInputStream(execute.body().byteStream());
                            OutputStream output = new FileOutputStream(file);

                            byte[] data = new byte[1024];

                            long total = 0;
                            int count=0;
                            while ((count = input.read(data)) != -1) {
                                total += count;
                                output.write(data, 0, count);
                            }

                            output.flush();
                            output.close();
                            input.close();
                        }
                        return file;
                    }
                }
        );
    }

    public static File getBadge(String version, String name, String color) throws ExecutionException {
        return mavenVersionCache.get(Triple.of(name,version.replace("-","--"), color));
    }

}
