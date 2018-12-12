package com.app.voicetotextapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.voicetotextapp.questionCreator.DocCallback;
import com.app.voicetotextapp.questionCreator.DocReader;
import com.app.voicetotextapp.speechRecognizer.handler.GoogleSpeechHandler;
import com.app.voicetotextapp.speechRecognizer.source.VoiceCallbacks;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VoiceCallbacks, View.OnTouchListener, DocCallback {

    private TextView textView1;
    private TextView textView2;
    private StringBuilder stringBuilder;
    private TextView question;
    private List<View> clickableItems = new ArrayList<>();
    private View menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        question = findViewById(R.id.question);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);

        clickableItems.add(question);
        clickableItems.add(textView1);
        clickableItems.add(textView2);

        stringBuilder = new StringBuilder();

        lockClickableItems(true);
        new DocReader(this).execute("");
    }

    @Override
    public void onTextReceived(String text) {
        textView1.setText(stringBuilder);
    }

    @Override
    public void onVoiceStatus(String status) {
        textView2.setText(status);
        if (!status.equals(getString(R.string.start))) {
            textView2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            stringBuilder.setLength(0);
            textView1.setText("");
        } else {
            textView2.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    public void showLoader(boolean bool) {
        findViewById(R.id.progressBar1).setVisibility(bool ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEndSpeech(String string) {
        stringBuilder.append(string);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.textView2:

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    GoogleSpeechHandler.requestRecordAudioPermission(this);
                    onVoiceStatus(getString(R.string.stop));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    GoogleSpeechHandler.getInstance(this).stopListening();
                    showLoader(false);
                    onVoiceStatus(getString(R.string.start));
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

    @Override
    public void onQuestionFetched(String text) {
        lockClickableItems(false);
        textView2.setOnTouchListener(this);
        question.setText(text);
    }

    private void lockClickableItems(boolean lock) {
        for (View clickableItem : clickableItems) {
            clickableItem.setEnabled(!lock);
            clickableItem.setAlpha(lock ? .2f : 1f);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem, menu);
        menuItem = findViewById(R.id.item1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
