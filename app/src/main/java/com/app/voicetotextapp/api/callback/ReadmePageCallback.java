package com.app.voicetotextapp.api.callback;

import com.app.voicetotextapp.api.data.AccuracyQuestions;

public interface ReadmePageCallback {
    void onQuestionFetched(AccuracyQuestions accuracyQuestions);
}
