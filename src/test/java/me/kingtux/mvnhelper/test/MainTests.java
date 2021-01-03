package me.kingtux.mvnhelper.test;

import me.kingtux.mvnhelper.maven.MavenTimeStampConverter;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MainTests {
    @Test
    public void testTimestampConverter() {
        SimpleDateFormat myFormatObj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        {
            Date time = MavenTimeStampConverter.getTime("20201228015634");
            System.out.println("myFormatObj.format(time1) = " + myFormatObj.format(time));

        }

        {
            Date time = MavenTimeStampConverter.getTime("20210103.052808");
            System.out.println("myFormatObj.format(time1) = " + myFormatObj.format(time));
        }
    }
}
