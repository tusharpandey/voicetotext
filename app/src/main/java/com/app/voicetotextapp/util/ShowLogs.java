package com.app.voicetotextapp.util;

import android.util.Log;

public class ShowLogs {
    public static void showLogs(String msg) {
        Log.i("ShowLogs", msg);
    }

    public static void showErrorLogs(String msg) {
        Log.i("ShowErrorLogs", msg);
    }
}
