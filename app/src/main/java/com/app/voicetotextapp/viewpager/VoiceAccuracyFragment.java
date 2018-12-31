package com.app.voicetotextapp.viewpager;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.voicetotextapp.R;
import com.app.voicetotextapp.api.AccurcyCheckApi;
import com.app.voicetotextapp.api.callback.AccuracyCheckCallback;
import com.app.voicetotextapp.speechRecognizer.handler.GoogleSpeechHandler;
import com.app.voicetotextapp.speechRecognizer.source.VoiceCallbacks;

import java.util.ArrayList;
import java.util.List;

public class VoiceAccuracyFragment extends Fragment implements VoiceCallbacks, View.OnClickListener, AccuracyCheckCallback {
    private TextView textView1;
    private Button button;
    private StringBuilder stringBuilder;
    private TextView question;
    private List<View> clickableItems = new ArrayList<>();
    private GoogleSpeechHandler googleSpeechHandler = new GoogleSpeechHandler();
    private String questionTxt;
    private String tokenTxt;
    private TextView match;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myfragment_accuracy, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        question = getView().findViewById(R.id.question);
        textView1 = getView().findViewById(R.id.textView1);
        button = getView().findViewById(R.id.textView2);
        match = getView().findViewById(R.id.match);

        clickableItems.add(question);
        clickableItems.add(textView1);
        clickableItems.add(button);

        stringBuilder = new StringBuilder();
        button.setOnClickListener(this);
        question.setText(questionTxt);
    }

    @Override
    public void onTextReceived(String text) {

        try {
            String lastAppended = stringBuilder.substring(start);
            if (lastAppended.equals(text)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        textView1.setText(stringBuilder + text);
    }


    int start = 0;
    int end = 0;

    @Override
    public void onEndSpeech(String string) {
        start = stringBuilder.length();
        end = start + string.length();
        stringBuilder.append(string);
    }

    @Override
    public void onVoiceStatus(String status) {
        button.setText(status);


        if (!status.equals(getString(R.string.start))) {
            button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            stringBuilder.setLength(0);
            textView1.setText("");
        } else {
            button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }

    }

    @Override
    public void showLoader(boolean bool) {
//        progressBar.setVisibility(bool ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView2:

                Drawable mDrawable = getActivity().getResources().getDrawable(R.drawable.microphone_black).mutate();
                String buttonText = button.getText().toString();

                if (buttonText.equals(getString(R.string.start))) {
                    googleSpeechHandler.requestRecordAudioPermission(this, getActivity());
                    onVoiceStatus(getString(R.string.stop));
                    match.setText(getText(R.string.match));
                    mDrawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.greenColor), PorterDuff.Mode.SRC_IN ));
                } else {
                    googleSpeechHandler.getInstance(this).stopListening();
                    showLoader(false);
                    onVoiceStatus(getString(R.string.start));
                    mDrawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.blackcolor), PorterDuff.Mode.SRC_IN ));
                }

                button.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);

                break;
        }
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