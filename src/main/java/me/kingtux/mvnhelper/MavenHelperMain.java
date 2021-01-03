package me.kingtux.mvnhelper;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MavenHelperMain {
    public static void main(String[] args) {
        try {

            File file = new File("config.json");
            if (!file.exists()) {
                IOUtils.copy(MavenHelperMain.class.getResourceAsStream("/config.json"), new FileOutputStream(file));
            }

            new MavenHelper(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
