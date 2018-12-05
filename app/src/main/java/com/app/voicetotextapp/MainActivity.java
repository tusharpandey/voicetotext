package com.app.voicetotextapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.voicetotextapp.speechRecognizer.handler.GoogleSpeechHandler;
import com.app.voicetotextapp.speechRecognizer.source.OnTextFetched;

public class MainActivity extends AppCompatActivity implements OnTextFetched, View.OnClickListener {

    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView2.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView2:
                String text = textView2.getText().toString();

                if (getString(R.string.start).equals(text)) {
                    GoogleSpeechHandler.requestRecordAudioPermission(this);
                } else {
                    GoogleSpeechHandler.getInstance(this).stopListening();
                    textView1.setText("");
                }

                onVoiceStatus(text.equals(getString(R.string.start)) ? getString(R.string.stop) : getString(R.string.start));

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GoogleSpeechHandler.getInstance(this).stopListening();
    }
}
