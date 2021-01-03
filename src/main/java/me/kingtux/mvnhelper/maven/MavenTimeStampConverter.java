package me.kingtux.mvnhelper.maven;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// yyyyMMddHHmmss
public class MavenTimeStampConverter {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    public static Date getTime(String time) {
        try {
            return DATE_FORMAT.parse(time.replace(".",""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
