package com.app.voicetotextapp.speechRecognizer.source;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;

import com.app.voicetotextapp.MainActivity;
import com.app.voicetotextapp.util.ShowLogs;

import java.util.ArrayList;

public class GoogleVoiceRecognitionListener implements RecognitionListener {
    private Intent recognizerIntent;
    private SpeechRecognizer recognizer;
    private OnTextFetched onTextFetched;
    private MainActivity act;

    public void startListening(MainActivity activity) {
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
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
        startListening();
    }

    public GoogleVoiceRecognitionListener(OnTextFetched onTextFetched) {
        this.onTextFetched = onTextFetched;
    }

    private void startListening() {
        recognizer.startListening(recognizerIntent);
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        ShowLogs.showLogs("onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        ShowLogs.showLogs("onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float v) {
        ShowLogs.showLogs("onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        ShowLogs.showLogs("onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        ShowLogs.showLogs("onEndOfSpeech");
        recognizer.stopListening();
    }

    @Override
    public void onError(int i) {
        onTextFetched.onTextReceived(getErrorText(i));
    }

    public String getErrorText(int errorCode) {
        String message = "";
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                ShowLogs.showLogs("SpeechRecognizer.ERROR_AUDIO");
                startListening();
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                ShowLogs.showLogs("SpeechRecognizer.ERROR_CLIENT");
                startListening();
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                ShowLogs.showLogs("SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS");
                startListening();
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                ShowLogs.showLogs("SpeechRecognizer.ERROR_NETWORK");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                ShowLogs.showLogs("SpeechRecognizer.ERROR_NETWORK_TIMEOUT");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                ShowLogs.showLogs("SpeechRecognizer.ERROR_NO_MATCH");
                startListening();
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                ShowLogs.showLogs("SpeechRecognizer.ERROR_RECOGNIZER_BUSY");
                recognizer.stopListening();
                recognizer.destroy();
                createSpeechAgain();
                break;
            case SpeechRecognizer.ERROR_SERVER:
                ShowLogs.showLogs("SpeechRecognizer.ERROR_SERVER");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                ShowLogs.showLogs("SpeechRecognizer.ERROR_SPEECH_TIMEOUT");
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
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = TextUtils.isEmpty(matches.get(0)) ? "" : matches.get(0);
        onTextFetched.onTextReceived(text);
        recognizer.startListening(recognizerIntent);
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        ShowLogs.showLogs("onPartialResults");
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        ShowLogs.showLogs("onEvent");
    }
}