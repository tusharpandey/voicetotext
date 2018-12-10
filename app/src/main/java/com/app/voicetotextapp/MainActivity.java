package com.app.voicetotextapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.app.voicetotextapp.speechRecognizer.handler.GoogleSpeechHandler;
import com.app.voicetotextapp.speechRecognizer.source.VoiceCallbacks;

public class MainActivity extends AppCompatActivity implements VoiceCallbacks, View.OnTouchListener {

    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView2.setOnTouchListener(this);
    }

    @Override
    public void onTextReceived(String text) {
        textView1.setText(text);
    }

    @Override
    public void onVoiceStatus(String status) {
        textView2.setText(status);
        if(!status.equals(getString(R.string.start))){
            textView2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }else{
            textView2.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    public void showLoader(boolean bool) {
        findViewById(R.id.progressBar1).setVisibility(bool ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.textView2:

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    GoogleSpeechHandler.requestRecordAudioPermission(this);
                    onVoiceStatus(getString(R.string.stop));
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    GoogleSpeechHandler.getInstance(this).stopListening();
                    showLoader(false);
                    onVoiceStatus(getString(R.string.start));
                    textView1.setText("");
                }

                break;

        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GoogleSpeechHandler.getInstance(this).stopListening();
    }
}
