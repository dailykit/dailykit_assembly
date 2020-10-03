package org.dailykit.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.dailykit.constants.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Danish Rafique on 29-01-2019.
 */
public class AppUtil {

    public static SharedPreferences getAppPreferences(Context context){
        return context.getSharedPreferences(Constants.SETTINGS_APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getISTTime(String utcPattern , String istPattern , String dateTime){
        if(null == dateTime || dateTime.isEmpty()){
            return "";
        }
        DateFormat utcFormat = new SimpleDateFormat(utcPattern);
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat indianFormat = new SimpleDateFormat(istPattern);
        indianFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        Date timestamp = null;
        try {
            timestamp = utcFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return indianFormat.format(timestamp);
    }

}
