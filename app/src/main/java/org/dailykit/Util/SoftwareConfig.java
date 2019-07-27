package org.dailykit.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SoftwareConfig {

    private SharedPreferences preferences;
    private static final String PARTIAL_PACKING = "PARTIAL_PACKING";
    private static final String RAPID_SCANNING = "RAPID_SCANNING";

    public SoftwareConfig(Context context) {
        preferences = AppUtil.getAppPreferences(context);
    }

    public void enablePartialPacking(boolean b) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PARTIAL_PACKING, b);
        editor.apply();
    }

    public boolean isPartialPackingEnabled() {
        return preferences.getBoolean(PARTIAL_PACKING, false);
    }

    public void enableRapidScanning(boolean b) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(RAPID_SCANNING, b);
        editor.apply();
    }

    public boolean isRapidScanningEnabled() {
        return preferences.getBoolean(RAPID_SCANNING, false);
    }

}
