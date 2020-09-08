package org.dailykit.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.dailykit.constants.Constants;

/**
 * Created by Danish Rafique on 29-01-2019.
 */
public class AppUtil {

    public static SharedPreferences getAppPreferences(Context context){
        return context.getSharedPreferences(Constants.SETTINGS_APP_SETTINGS, Context.MODE_PRIVATE);
    }

}
