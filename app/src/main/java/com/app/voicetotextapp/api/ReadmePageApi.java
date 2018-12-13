package com.app.voicetotextapp.api;

import com.app.voicetotextapp.api.callback.ReadmePageCallback;
import com.app.voicetotextapp.api.data.AccuracyQuestions;
import com.google.gson.Gson;

public class ReadmePageApi extends ApiHit {

    ReadmePageCallback docCallback;

    public ReadmePageApi(ReadmePageCallback docCallback) {
        super("https://raw.githubusercontent.com/livecareetest/VoiceToText/master/samplefile?raw=true");
        this.docCallback = docCallback;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        if (docCallback == null || stringBuilder == null) {
            return;
        }
        AccuracyQuestions response = new Gson().fromJson(stringBuilder.toString(), AccuracyQuestions.class);
        docCallback.onQuestionFetched(response);
    }
}
