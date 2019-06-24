package com.app.voicetotextapp.speechRecognizer.source;

public interface VoiceCallbacks {
    void onTextReceived(String text);

    void onVoiceStatus(String status);

    void showLoader(boolean bool);

    void onEndSpeech(String string);

    void onBeginSpeech();
}
