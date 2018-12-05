package com.app.voicetotextapp.speechRecognizer.source;

public interface OnTextFetched {
    void onTextReceived(String text);

    void onVoiceStatus(String status);
}
