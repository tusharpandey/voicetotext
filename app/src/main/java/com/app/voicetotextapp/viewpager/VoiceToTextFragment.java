package com.app.voicetotextapp.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.voicetotextapp.R;
import com.app.voicetotextapp.speechRecognizer.handler.GoogleSpeechHandler;
import com.app.voicetotextapp.speechRecognizer.source.VoiceCallbacks;

import java.util.ArrayList;
import java.util.List;

public class VoiceToTextFragment extends Fragment implements VoiceCallbacks, View.OnTouchListener {
    private TextView textView1;
    private TextView textView2;
    private StringBuilder stringBuilder;
    private TextView question;
    private List<View> clickableItems = new ArrayList<>();
    private ProgressBar progressBar;
    private GoogleSpeechHandler googleSpeechHandler = new GoogleSpeechHandler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myfragment_layout, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = getView().findViewById(R.id.progressBar1);
        question = getView().findViewById(R.id.question);
        textView1 = getView().findViewById(R.id.textView1);
        textView2 = getView().findViewById(R.id.textView2);

        clickableItems.add(question);
        clickableItems.add(textView1);
        clickableItems.add(textView2);

        stringBuilder = new StringBuilder();
        textView2.setOnTouchListener(this);
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
        progressBar.setVisibility(bool ? View.VISIBLE : View.GONE);
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
                    googleSpeechHandler.requestRecordAudioPermission(this, getActivity());
                    onVoiceStatus(getString(R.string.stop));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    googleSpeechHandler.getInstance(this).stopListening();
                    showLoader(false);
                    onVoiceStatus(getString(R.string.start));
                }

                break;

        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        googleSpeechHandler.getInstance(this).stopListening();
    }

    private void lockClickableItems(boolean lock) {
        for (View clickableItem : clickableItems) {
            clickableItem.setEnabled(!lock);
            clickableItem.setAlpha(lock ? .2f : 1f);
        }
    }
}