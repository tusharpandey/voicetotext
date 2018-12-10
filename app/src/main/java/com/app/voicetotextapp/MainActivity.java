package com.app.voicetotextapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.app.voicetotextapp.speechRecognizer.handler.GoogleSpeechHandler;
import com.app.voicetotextapp.speechRecognizer.source.OnTextFetched;

public class MainActivity extends AppCompatActivity implements OnTextFetched, View.OnTouchListener {

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
        String oldText = TextUtils.isEmpty(textView1.getText().toString()) ? "" : textView1.getText().toString();
        textView1.setText(oldText + " " + text);
    }

    @Override
    public void onVoiceStatus(String status) {
        textView2.setText(status);
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
