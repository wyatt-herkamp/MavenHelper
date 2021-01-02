package me.kingtux.cmb;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CMBMain {
    public static void main(String[] args) {
        try {

            File file = new File("config.json");
            if (!file.exists()) {
                IOUtils.copy(CMBMain.class.getResourceAsStream("/config.json"), new FileOutputStream(file));
            }

            new CustomMavenBadges(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
