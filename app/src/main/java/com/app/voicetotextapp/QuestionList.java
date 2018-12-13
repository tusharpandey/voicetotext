package com.app.voicetotextapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.voicetotextapp.api.ReadmePageApi;
import com.app.voicetotextapp.api.callback.ReadmePageCallback;
import com.app.voicetotextapp.api.data.AccuracyQuestions;
import com.app.voicetotextapp.util.Constants;

public class QuestionList extends AppCompatActivity implements ReadmePageCallback, AdapterView.OnItemClickListener {

    private AccuracyQuestions accuracyQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        new ReadmePageApi(this).execute("");
        getSupportActionBar().setTitle(getString(R.string.question_list));
    }

    @Override
    public void onQuestionFetched(AccuracyQuestions accuracyQuestions) {
        this.accuracyQuestions = accuracyQuestions;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, accuracyQuestions.getParagraphs());
        ListView listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, CheckAccuracy.class);
        intent.putExtra(Constants.QUESTION, accuracyQuestions.getParagraphs().get(i));
        intent.putExtra(Constants.TOKEN, accuracyQuestions.getToken());
        startActivity(intent);
    }
}
