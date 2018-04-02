package vn.edu.hust.soict.khacsan.jobassignment.untils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by San on 03/25/2018.
 */

public class DataFormatUntils {
    public static String getDate(long milliSeconds)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
