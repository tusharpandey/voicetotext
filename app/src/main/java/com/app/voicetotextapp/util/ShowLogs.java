package com.app.voicetotextapp.util;

import android.util.Log;

public class ShowLogs {
    public static void showLogs(String msg) {
        Log.i("ShowLogs", msg);
    }

    public static void showErrorLogs(String msg) {
        Log.i("ShowErrorLogs", msg);
    }

    public static void showErrorLogs(String tag, String msg) {
        Log.i("data : "+tag, msg);
    }

}
