package com.app.voicetotextapp.speechRecognizer.source;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.app.voicetotextapp.util.ShowLogs;

import java.util.ArrayList;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class RecognizeService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private SpeechRecognizer recognizer;

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            startVoiceRecognition();
        }
    }

    private void startVoiceRecognition(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
        recognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        recognizer.setRecognitionListener(new GoogleVoiceRecognitionListener());
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        intent.putExtra("android.speech.extra.DICTATION_MODE", true);
        recognizer.startListening(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class GoogleVoiceRecognitionListener implements RecognitionListener {

        public GoogleVoiceRecognitionListener() {
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> voiceResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = TextUtils.isEmpty(voiceResults.get(0)) ? "" : voiceResults.get(0);
            ShowLogs.showLogs("Ready for speech: " + text);
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            ShowLogs.showLogs("Ready for speech: ");
        }

        @Override
        public void onError(int error) {
            ShowLogs.showLogs("Error listening for speech: " + error);
            recognizer.destroy();
        }

        @Override
        public void onBeginningOfSpeech() {
            ShowLogs.showLogs("onBeginningOfSpeech");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            ShowLogs.showLogs("onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            ShowLogs.showLogs("onEndOfSpeech");
            recognizer.destroy();
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            ShowLogs.showLogs("onEvent");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            ShowLogs.showLogs("onPartialResults");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            ShowLogs.showLogs("onRmsChanged");
        }
    }
}
