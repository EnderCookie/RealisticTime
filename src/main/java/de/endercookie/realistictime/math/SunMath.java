package de.endercookie.realistictime.math;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

/**
 *
 * @author EnderCookie
 */
public class SunMath {

    private static final double H = -(50.0 / 60.0) * (Math.PI / 180.0);

    private static LocalTime hoursToTime(double hours) {
        return LocalTime.of((int) Math.floor(hours), (int) (60 * (hours % 1.0)));
    }

    public static OffsetTime getSunrise(double latitude, double longitude, int dayofyear) {
        double dekl = 0.409526325277017 * Math.sin(0.0169060504029192 * (dayofyear - 80.0856919827619));
        double diff = 12.0 * Math.acos((Math.sin(H) - Math.sin(latitude) * Math.sin(dekl)) / (Math.cos(latitude) * Math.cos(dekl))) / Math.PI;

        double gleich = (-0.170869921174742 * Math.sin(0.0336997028793971 * dayofyear + 0.465419984181394)) - (0.129890681040717 * Math.sin(0.0178674832556871 * dayofyear - 0.167936777524864));

        return OffsetTime.of(hoursToTime((12 - diff - gleich) - (longitude / 15.0)), ZoneOffset.UTC);
    }

    public static OffsetTime getSunset(double latitude, double longitude, int dayofyear) {
        double dekl = 0.409526325277017 * Math.sin(0.0169060504029192 * (dayofyear - 80.0856919827619));
        double diff = 12.0 * Math.acos((Math.sin(H) - Math.sin(latitude) * Math.sin(dekl)) / (Math.cos(latitude) * Math.cos(dekl))) / Math.PI;

        double gleich = (-0.170869921174742 * Math.sin(0.0336997028793971 * dayofyear + 0.465419984181394)) - (0.129890681040717 * Math.sin(0.0178674832556871 * dayofyear - 0.167936777524864));

        return OffsetTime.of(hoursToTime((12 + diff - gleich) - (longitude / 15.0)), ZoneOffset.UTC);
    }
}
