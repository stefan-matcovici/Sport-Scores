package ro.uaic.info.tppa.sportscores.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    private static String parseDateToDesiredFormat(String time, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDisplayableDateTime(String date, String time) {
        String inputPattern = "dd/MM/yy";
        String outputPattern = "yyyy-MM-dd";

        String displayableDate = parseDateToDesiredFormat(date, inputPattern, outputPattern);

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(displayableDate + "T" + time);

        return DateTimeFormatter.ofPattern("dd MMM yy hh:mm").format(zonedDateTime);
    }
}
