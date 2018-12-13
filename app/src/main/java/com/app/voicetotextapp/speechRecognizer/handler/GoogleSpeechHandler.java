package com.app.voicetotextapp.speechRecognizer.handler;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.app.voicetotextapp.speechRecognizer.source.GoogleVoiceRecognitionListener;
import com.app.voicetotextapp.speechRecognizer.source.VoiceCallbacks;

public class GoogleSpeechHandler {
    GoogleVoiceRecognitionListener listener;
    VoiceCallbacks callback;

    public synchronized GoogleVoiceRecognitionListener getInstance(VoiceCallbacks callback) {
        if (listener == null) {
            listener = new GoogleVoiceRecognitionListener(callback);
        }
        return listener;
    }

    public void requestRecordAudioPermission(VoiceCallbacks callback, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;
            if (activity.checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                activity.requestPermissions(new String[]{requiredPermission}, 101);
            } else {
                getInstance(callback).startListening(activity);
            }
        }
    }
}
