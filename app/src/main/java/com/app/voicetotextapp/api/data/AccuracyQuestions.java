package com.app.voicetotextapp.api.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccuracyQuestions {

    @SerializedName("Token")
    private String token;
    @SerializedName("paragraphs")
    private List<String> paragraphs = null;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }
}
