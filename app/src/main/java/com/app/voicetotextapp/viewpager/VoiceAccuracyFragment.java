package com.app.voicetotextapp.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.voicetotextapp.R;
import com.app.voicetotextapp.api.AccurcyCheckApi;
import com.app.voicetotextapp.api.callback.AccuracyCheckCallback;
import com.app.voicetotextapp.speechRecognizer.handler.GoogleSpeechHandler;
import com.app.voicetotextapp.speechRecognizer.source.VoiceCallbacks;

import java.util.ArrayList;
import java.util.List;

public class VoiceAccuracyFragment extends Fragment implements VoiceCallbacks, View.OnTouchListener, AccuracyCheckCallback {
    private TextView textView1;
    private TextView textView2;
    private StringBuilder stringBuilder;
    private TextView question;
    private List<View> clickableItems = new ArrayList<>();
    private ProgressBar progressBar;
    private GoogleSpeechHandler googleSpeechHandler = new GoogleSpeechHandler();
    private String questionTxt;
    private String tokenTxt;
    private TextView match;
    private TextView latency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myfragment_accuracy, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = getView().findViewById(R.id.progressBar1);
        question = getView().findViewById(R.id.question);
        textView1 = getView().findViewById(R.id.textView1);
        textView2 = getView().findViewById(R.id.textView2);
        match = getView().findViewById(R.id.match);
        latency = getView().findViewById(R.id.lateny);

        clickableItems.add(question);
        clickableItems.add(textView1);
        clickableItems.add(textView2);

        stringBuilder = new StringBuilder();
        textView2.setOnTouchListener(this);
        question.setText(questionTxt);
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
                    latency.setText(getText(R.string.latency));
                    match.setText(getText(R.string.match));
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

    @Override
    public void onAccurcyFetched(String matched, String time) {
        if (TextUtils.isEmpty(matched) && TextUtils.isEmpty(time)) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        latency.setText(getText(R.string.latency) + time);
        match.setText(getText(R.string.match) + matched);
    }

    public void setData(String question, String token) {
        this.questionTxt = question;
        this.tokenTxt = token;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.voice_accuracy, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                String[] requestOptions = new String[3];

                String questionStr = question.getText().toString();
                String answerStr = textView1.getText().toString();

                if (TextUtils.isEmpty(questionStr) || TextUtils.isEmpty(answerStr)) {
                    Toast.makeText(getActivity(), "Question and Answer can't be empty", Toast.LENGTH_SHORT).show();
                    return true;
                }

                questionStr = questionStr.trim();
                answerStr = answerStr.trim();

                questionStr = questionStr.contains("\\n") ? questionStr.replace("\\n", "") : questionStr;
                questionStr = questionStr.replace(" ", "%20");
                answerStr = answerStr.replace(" ", "%20");

                requestOptions[0] = questionStr;
                requestOptions[1] = answerStr;
                requestOptions[2] = tokenTxt;

                new AccurcyCheckApi(this, requestOptions).execute("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}