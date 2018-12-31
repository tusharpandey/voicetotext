package com.app.voicetotextapp.speechRecognizer.source;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;

import com.app.voicetotextapp.util.ShowLogs;

import java.util.ArrayList;

public class GoogleVoiceRecognitionListener implements RecognitionListener {
    private Intent recognizerIntent;
    private SpeechRecognizer recognizer;
    private VoiceCallbacks callback;
    private Activity act;
    private String partialResult = "";

    public void startListening(Activity activity) {
        this.act = activity;
        createSpeechAgain();
    }

    public void stopListening() {
        if (recognizer == null) {
            return;
        }
        recognizer.stopListening();
        recognizer.destroy();
    }

    private void createSpeechAgain() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(act);
        recognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, act.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
        startListening();
    }

    public GoogleVoiceRecognitionListener(VoiceCallbacks onTextFetched) {
        this.callback = onTextFetched;
    }

    private void startListening() {
        AudioManager audio = (AudioManager) act.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.ADJUST_MUTE);
        recognizer.startListening(recognizerIntent);
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        partialResult = "";
        ShowLogs.showLogs("onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        ShowLogs.showLogs("onBeginningOfSpeech");
        callback.showLoader(true);
    }

    @Override
    public void onRmsChanged(float v) {
//        ShowLogs.showLogs("onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        ShowLogs.showLogs("onBufferReceived");
    }

    @Override
    public void onError(int i) {
        callback.onTextReceived(getErrorText(i));
    }

    public String getErrorText(int errorCode) {
        String message = "";
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                ShowLogs.showErrorLogs("SpeechRecognizer.ERROR_AUDIO");
                startListening();
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                ShowLogs.showErrorLogs("SpeechRecognizer.ERROR_CLIENT");
                startListening();
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                ShowLogs.showErrorLogs("SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS");
                startListening();
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                ShowLogs.showErrorLogs("SpeechRecognizer.ERROR_NETWORK");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                ShowLogs.showErrorLogs("SpeechRecognizer.ERROR_NETWORK_TIMEOUT");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                ShowLogs.showErrorLogs("SpeechRecognizer.ERROR_NO_MATCH");
                startListening();
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                ShowLogs.showErrorLogs("SpeechRecognizer.ERROR_RECOGNIZER_BUSY");
                recognizer.stopListening();
                recognizer.destroy();
                createSpeechAgain();
                break;
            case SpeechRecognizer.ERROR_SERVER:
                ShowLogs.showErrorLogs("SpeechRecognizer.ERROR_SERVER");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                ShowLogs.showErrorLogs("SpeechRecognizer.ERROR_SPEECH_TIMEOUT");
                recognizer.stopListening();
                recognizer.destroy();
                createSpeechAgain();
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    @Override
    public void onResults(Bundle bundle) {
        recognizer.startListening(recognizerIntent);
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        partialResult = matches.get(matches.size()-1) + " ";
        if (TextUtils.isEmpty(partialResult)) {
            return;
        }
        ShowLogs.showLogs(partialResult);
        callback.onTextReceived(partialResult);
    }

    @Override
    public void onEndOfSpeech() {
        ShowLogs.showLogs("onEndOfSpeech");
        callback.showLoader(false);
        recognizer.stopListening();
        callback.onEndSpeech(partialResult);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        ShowLogs.showLogs("onEvent");
    }
}