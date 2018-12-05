package com.app.voicetotextapp.speechRecognizer.handler;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import com.app.voicetotextapp.MainActivity;
import com.app.voicetotextapp.speechRecognizer.source.GoogleVoiceRecognitionListener;

public class GoogleSpeechHandler {
    static GoogleVoiceRecognitionListener listener;

    public static synchronized GoogleVoiceRecognitionListener getInstance(MainActivity activity) {
        if (listener == null) {
            listener = new GoogleVoiceRecognitionListener(activity);
        }
        return listener;
    }

    public static void requestRecordAudioPermission(MainActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;
            if (activity.checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                activity.requestPermissions(new String[]{requiredPermission}, 101);
            } else {
                GoogleSpeechHandler.getInstance(activity).startListening(activity);
            }
        }
    }
}
