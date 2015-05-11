package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {


    /**
     *
     * @param date
     * @return
     * @throws java.text.ParseException
     * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a> $
     */
    public static Date parseIso8601(String date) throws java.text.ParseException {

        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );

        //this is zero time so we need to add that TZ indicator for
        if ( date.endsWith( "Z" ) ) {
            date = date.substring( 0, date.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = date.substring( 0, date.length() - inset );
            String s1 = date.substring( date.length() - inset, date.length() );

            date = s0 + "GMT" + s1;
        }

        return df.parse(date);

    }

    /**
     *
     * @param date
     * @return
     * @throws java.text.ParseException
     * @author http://stackoverflow.com/a/2705611/1349766
     */
    public static Date parseRSS(String date) throws java.text.ParseException {

        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        return formatter.parse(date);

    }

    public static int addHoursMinutesSeconds(int hours, int minutes, int seconds) {
        return 3600 * hours + 60 * minutes + seconds;
    }

}
